package rfsc.play.mvc;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Http {

    public static class Headers {

        private final Map<String, List<String>> headers;

        public Headers(Map<String, List<String>> headers) {
            this.headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            this.headers.putAll(headers);
        }

        public Map<String, List<String>> asMap() {
            return Collections.unmodifiableMap(headers);
        }

        public boolean contains(String headerName) {
            return headers.containsKey(headerName);
        }

        public Optional<String> get(String name) {
            return Optional.ofNullable(headers.get(name)).flatMap(headerValues -> headerValues.stream().findFirst());
        }

        public List<String> getAll(String name) {
            return headers.getOrDefault(name, Collections.emptyList());
        }

        public Headers adding(String name, String value) {
            return adding(name, Collections.singletonList(value));
        }

        public Headers adding(String name, List<String> values) {
            Map newHeaders = new HashMap<>(this.headers.size() + 1);
            newHeaders.putAll(this.headers);
            newHeaders.put(name, values);
            return new Headers(newHeaders);
        }

        public Headers removing(String name) {
            Map newHeaders = new HashMap<>(this.headers.size());
            newHeaders.putAll(this.headers);
            newHeaders.remove(name);
            return new Headers(newHeaders);
        }
    }

    public interface RequestHeader {

        String uri();

        String method();

        String version();

        String remoteAddress();

        boolean secure();

        Request withBody(RequestBody body);

        String host();

        String path();

        Optional<String> queryString(String key);

        Cookies cookies();

        Optional<Cookie> getCookie(String name);

        Headers getHeaders();

        default Optional<String> header(String headerName) {
            return getHeaders().get(headerName);
        }

        default boolean hasHeader(String headerName) {
            return getHeaders().contains(headerName);
        }

        boolean hasBody();

        Optional<String> contentType();

        Optional<String> charset();

        Optional<List<X509Certificate>> clientCertificateChain();
    }

    /** An HTTP request. */
    public interface Request extends RequestHeader {

        /** @return the request body */
        RequestBody body();

        Request withBody(RequestBody body);

    }

    /** The builder for building a request. */
    public static class RequestBuilder {

        private int partLength(
                final String boundary,
                final String dispositionType,
                final String name,
                final String filename,
                final String contentType,
                final String body) {
            final String part =
                    boundary
                            + "\r\n"
                            + "Content-Disposition: "
                            + dispositionType
                            + "; name=\""
                            + name
                            + "\""
                            + (filename != null ? "; filename=\"" + filename + "\"" : "")
                            + "\r\n"
                            + (contentType != null ? "Content-Type: " + contentType + "\r\n" : "")
                            + "\r\n"
                            + body;
            return part.getBytes(StandardCharsets.UTF_8).length;
        }
    }

    public abstract static class RawBuffer {

        public abstract Long size();

        /**
         * Returns the buffer content as a bytes array.
         *
         * @param maxLength The max length allowed to be stored in memory
         * @return null if the content is too big to fit in memory
         */
        public abstract ByteString asBytes(int maxLength);

        /** @return the buffer content as a bytes array */
        public abstract ByteString asBytes();

        /** @return the buffer content as a file */
        public abstract File asFile();
    }

    /** Multipart form data body. */
    public abstract static class MultipartFormData<A> {

        /** Info about a file part */
        public static class FileInfo {
            private final String key;
            private final String filename;
            private final String contentType;

            public FileInfo(String key, String filename, String contentType) {
                this.key = key;
                this.filename = filename;
                this.contentType = contentType;
            }

            public String getKey() {
                return key;
            }

            public String getFilename() {
                return filename;
            }

            public String getContentType() {
                return contentType;
            }
        }

        public interface Part<A> {}

        /** A file part. */
        public static class FilePart<A> implements Part<A> {

            final String key;
            final String filename;
            final String contentType;
            final A ref;
            final String dispositionType;
            final long fileSize;

            public FilePart(String key, String filename, String contentType, A ref) {
                this(key, filename, contentType, ref, -1);
            }

            public FilePart(String key, String filename, String contentType, A ref, long fileSize) {
                this(key, filename, contentType, ref, fileSize, "form-data");
            }

            public FilePart(
                    String key,
                    String filename,
                    String contentType,
                    A ref,
                    long fileSize,
                    String dispositionType) {
                this.key = key;
                this.filename = filename;
                this.contentType = contentType;
                this.ref = ref;
                this.dispositionType = dispositionType;
                this.fileSize = fileSize;
            }

            /** @return the part name */
            public String getKey() {
                return key;
            }

            /** @return the file name */
            public String getFilename() {
                return filename;
            }

            /** @return the file content type */
            public String getContentType() {
                return contentType;
            }

            /**
             * The File.
             *
             * @return the file
             */
            public A getRef() {
                return ref;
            }

            /** @return the disposition type */
            public String getDispositionType() {
                return dispositionType;
            }

            /** @return the size of the file in bytes */
            public long getFileSize() {
                return fileSize;
            }
        }

        public abstract Map<String, String[]> asFormUrlEncoded();

        public abstract List<FilePart<A>> getFiles();

        public FilePart<A> getFile(String key) {
            for (FilePart<A> filePart : getFiles()) {
                if (filePart.getKey().equals(key)) {
                    return filePart;
                }
            }
            return null;
        }
    }

    /** The request body. */
    public static final class RequestBody {

        private final Object body;

        public RequestBody(Object body) {
            this.body = body;
        }


        @SuppressWarnings("unchecked")
        public <A> MultipartFormData<A> asMultipartFormData() {
            return as(MultipartFormData.class);
        }

        @SuppressWarnings("unchecked")
        public Map<String, String[]> asFormUrlEncoded() {
            // Best effort, check if it's a map, then check if the first element in that map is String ->
            // String[].
            if (body instanceof Map) {
                if (((Map<?, ?>) body).isEmpty()) {
                    return Collections.emptyMap();
                } else {
                    Map.Entry<?, ?> first = ((Map<?, ?>) body).entrySet().iterator().next();
                    if (first.getKey() instanceof String && first.getValue() instanceof String[]) {
                        @SuppressWarnings("unchecked")
                        final Map<String, String[]> body = (Map<String, String[]>) this.body;
                        return body;
                    }
                }
            }
            return null;
        }

        /** @return The request content as Array bytes. */
        public RawBuffer asRaw() {
            return as(RawBuffer.class);
        }

        /** @return The request content as text. */
        public String asText() {
            return as(String.class);
        }

        /** @return The request content as XML. */
        public Document asXml() {
            return as(Document.class);
        }

        private String encode(String value) {
            try {
                return URLEncoder.encode(value, "utf8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        public <T> T as(Class<T> tType) {
            if (tType.isInstance(body)) {
                return tType.cast(body);
            } else {
                return null;
            }
        }

        public String toString() {
            return "RequestBody of " + (body == null ? "null" : body.getClass());
        }
    }

    /** HTTP Cookie */
    public static class Cookie {
        private final String name;
        private final String value;
        private final Integer maxAge;
        private final String path;
        private final String domain;
        private final boolean secure;
        private final boolean httpOnly;
        private final SameSite sameSite;

        public Cookie(
                String name,
                String value,
                Integer maxAge,
                String path,
                String domain,
                boolean secure,
                boolean httpOnly,
                SameSite sameSite) {
            this.name = name;
            this.value = value;
            this.maxAge = maxAge;
            this.path = path;
            this.domain = domain;
            this.secure = secure;
            this.httpOnly = httpOnly;
            this.sameSite = sameSite;
        }

        /** @return the cookie name */
        public String name() {
            return name;
        }

        /** @return the cookie value */
        public String value() {
            return value;
        }

        /**
         * @return the cookie expiration date in seconds, null for a transient cookie, a value less than
         *     zero for a cookie that expires now
         */
        public Integer maxAge() {
            return maxAge;
        }

        /** @return the cookie path */
        public String path() {
            return path;
        }

        /** @return the cookie domain, or null if not defined */
        public String domain() {
            return domain;
        }

        /** @return wether the cookie is secured, sent only for HTTPS requests */
        public boolean secure() {
            return secure;
        }

        /**
         * @return wether the cookie is HTTP only, i.e. not accessible from client-side JavaScript code
         */
        public boolean httpOnly() {
            return httpOnly;
        }

        /** @return the SameSite attribute for this cookie */
        public Optional<SameSite> sameSite() {
            return Optional.ofNullable(sameSite);
        }

        /** The cookie SameSite attribute */
        public enum SameSite {
            STRICT("Strict"),
            LAX("Lax"),
            NONE("None");

            private final String value;

            SameSite(String value) {
                this.value = value;
            }

            public String value() {
                return this.value;
            }

            public static Optional<SameSite> parse(String sameSite) {
                for (SameSite value : values()) {
                    if (value.value.equalsIgnoreCase(sameSite)) {
                        return Optional.of(value);
                    }
                }
                return Optional.empty();
            }
        }

    }

    /** HTTP Cookies set */
    public interface Cookies extends Iterable<Cookie> {

        /**
         * @param name Name of the cookie to retrieve
         * @return the cookie that is associated with the given name
         */
        Optional<Cookie> get(String name);

        /**
         * @param name Name of the cookie to retrieve
         * @return the optional cookie that is associated with the given name
         * @deprecated Deprecated as of 2.8.0. Renamed to {@link #get(String)}
         */
        @Deprecated
        default Optional<Cookie> getCookie(String name) {
            return get(name);
        }
    }

    /** Defines all standard HTTP headers. */
    public interface HeaderNames {

        String ACCEPT = "Accept";
        String ACCEPT_CHARSET = "Accept-Charset";
        String ACCEPT_ENCODING = "Accept-Encoding";
        String ACCEPT_LANGUAGE = "Accept-Language";
        String ACCEPT_RANGES = "Accept-Ranges";
        String AGE = "Age";
        String ALLOW = "Allow";
        String AUTHORIZATION = "Authorization";
        String CACHE_CONTROL = "Cache-Control";
        String CONNECTION = "Connection";
        String CONTENT_DISPOSITION = "Content-Disposition";
        String CONTENT_ENCODING = "Content-Encoding";
        String CONTENT_LANGUAGE = "Content-Language";
        String CONTENT_LENGTH = "Content-Length";
        String CONTENT_LOCATION = "Content-Location";
        String CONTENT_MD5 = "Content-MD5";
        String CONTENT_RANGE = "Content-Range";
        String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
        String CONTENT_TYPE = "Content-Type";
        String COOKIE = "Cookie";
        String DATE = "Date";
        String ETAG = "ETag";
        String EXPECT = "Expect";
        String EXPIRES = "Expires";
        String FORWARDED = "Forwarded";
        String FROM = "From";
        String HOST = "Host";
        String IF_MATCH = "If-Match";
        String IF_MODIFIED_SINCE = "If-Modified-Since";
        String IF_NONE_MATCH = "If-None-Match";
        String IF_RANGE = "If-Range";
        String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
        String LAST_MODIFIED = "Last-Modified";
        String LINK = "Link";
        String LOCATION = "Location";
        String MAX_FORWARDS = "Max-Forwards";
        String PRAGMA = "Pragma";
        String PROXY_AUTHENTICATE = "Proxy-Authenticate";
        String PROXY_AUTHORIZATION = "Proxy-Authorization";
        String RANGE = "Range";
        String REFERER = "Referer";
        String RETRY_AFTER = "Retry-After";
        String SERVER = "Server";
        String SET_COOKIE = "Set-Cookie";
        String SET_COOKIE2 = "Set-Cookie2";
        String TE = "Te";
        String TRAILER = "Trailer";
        String TRANSFER_ENCODING = "Transfer-Encoding";
        String UPGRADE = "Upgrade";
        String USER_AGENT = "User-Agent";
        String VARY = "Vary";
        String VIA = "Via";
        String WARNING = "Warning";
        String WWW_AUTHENTICATE = "WWW-Authenticate";
        String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
        String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
        String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
        String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
        String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
        String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
        String ORIGIN = "Origin";
        String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
        String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
        String X_FORWARDED_FOR = "X-Forwarded-For";
        String X_FORWARDED_HOST = "X-Forwarded-Host";
        String X_FORWARDED_PORT = "X-Forwarded-Port";
        String X_FORWARDED_PROTO = "X-Forwarded-Proto";
        String X_REQUESTED_WITH = "X-Requested-With";
        String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";
        String X_FRAME_OPTIONS = "X-Frame-Options";
        String X_XSS_PROTECTION = "X-XSS-Protection";
        String X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";
        String X_PERMITTED_CROSS_DOMAIN_POLICIES = "X-Permitted-Cross-Domain-Policies";
        String CONTENT_SECURITY_POLICY = "Content-Security-Policy";
        String CONTENT_SECURITY_POLICY_REPORT_ONLY = "Content-Security-Policy-Report-Only";
        String X_CONTENT_SECURITY_POLICY_NONCE_HEADER = "X-Content-Security-Policy-Nonce";
        String REFERRER_POLICY = "Referrer-Policy";
    }

    /**
     * Defines all standard HTTP status codes.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231">RFC 7231</a> and <a
     *     href="https://tools.ietf.org/html/rfc6585">RFC 6585</a>
     */
    public interface Status {
        int CONTINUE = 100;
        int SWITCHING_PROTOCOLS = 101;

        int OK = 200;
        int CREATED = 201;
        int ACCEPTED = 202;
        int NON_AUTHORITATIVE_INFORMATION = 203;
        int NO_CONTENT = 204;
        int RESET_CONTENT = 205;
        int PARTIAL_CONTENT = 206;
        int MULTI_STATUS = 207;

        int MULTIPLE_CHOICES = 300;
        int MOVED_PERMANENTLY = 301;
        int FOUND = 302;
        int SEE_OTHER = 303;
        int NOT_MODIFIED = 304;
        int USE_PROXY = 305;
        int TEMPORARY_REDIRECT = 307;
        int PERMANENT_REDIRECT = 308;

        int BAD_REQUEST = 400;
        int UNAUTHORIZED = 401;
        int PAYMENT_REQUIRED = 402;
        int FORBIDDEN = 403;
        int NOT_FOUND = 404;
        int METHOD_NOT_ALLOWED = 405;
        int NOT_ACCEPTABLE = 406;
        int PROXY_AUTHENTICATION_REQUIRED = 407;
        int REQUEST_TIMEOUT = 408;
        int CONFLICT = 409;
        int GONE = 410;
        int LENGTH_REQUIRED = 411;
        int PRECONDITION_FAILED = 412;
        int REQUEST_ENTITY_TOO_LARGE = 413;
        int REQUEST_URI_TOO_LONG = 414;
        int UNSUPPORTED_MEDIA_TYPE = 415;
        int REQUESTED_RANGE_NOT_SATISFIABLE = 416;
        int EXPECTATION_FAILED = 417;
        int IM_A_TEAPOT = 418;
        int UNPROCESSABLE_ENTITY = 422;
        int LOCKED = 423;
        int FAILED_DEPENDENCY = 424;
        int UPGRADE_REQUIRED = 426;

        // See https://tools.ietf.org/html/rfc6585 for the following statuses
        int PRECONDITION_REQUIRED = 428;
        int TOO_MANY_REQUESTS = 429;
        int REQUEST_HEADER_FIELDS_TOO_LARGE = 431;

        int INTERNAL_SERVER_ERROR = 500;
        int NOT_IMPLEMENTED = 501;
        int BAD_GATEWAY = 502;
        int SERVICE_UNAVAILABLE = 503;
        int GATEWAY_TIMEOUT = 504;
        int HTTP_VERSION_NOT_SUPPORTED = 505;
        int INSUFFICIENT_STORAGE = 507;

        // See https://tools.ietf.org/html/rfc6585#section-6
        int NETWORK_AUTHENTICATION_REQUIRED = 511;
    }

    /** Common HTTP MIME types */
    public interface MimeTypes {

        /** Content-Type of text. */
        String TEXT = "text/plain";

        /** Content-Type of html. */
        String HTML = "text/html";

        /** Content-Type of json. */
        String JSON = "application/json";

        /** Content-Type of xml. */
        String XML = "application/xml";

        /** Content-Type of xhtml. */
        String XHTML = "application/xhtml+xml";

        /** Content-Type of css. */
        String CSS = "text/css";

        /** Content-Type of javascript. */
        String JAVASCRIPT = "application/javascript";

        /** Content-Type of form-urlencoded. */
        String FORM = "application/x-www-form-urlencoded";

        /** Content-Type of server sent events. */
        String EVENT_STREAM = "text/event-stream";

        /** Content-Type of binary data. */
        String BINARY = "application/octet-stream";
    }

    /** Standard HTTP Verbs */
    public interface HttpVerbs {
        String GET = "GET";
        String POST = "POST";
        String PUT = "PUT";
        String PATCH = "PATCH";
        String DELETE = "DELETE";
        String HEAD = "HEAD";
        String OPTIONS = "OPTIONS";
    }
}
