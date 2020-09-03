package com.zizhizhan.legacies.thirdparty.httpclient.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

@Slf4j
public class HttpRequest {
    private static final Pattern EMPTY_LINE = Pattern.compile("\n^\\s*$", Pattern.MULTILINE | Pattern.UNIX_LINES);
    private static final String IGNORE_TAG_REGEX = "<(head|script|style|form|select|iframe|comment)[^>]*>.*?</\\1>";
    private static final String DEFAULT_ENCODING = "iso8859-1";
    private static final int BLOCK_SIZE = 2048;
    private static final Pattern[] ENCODING_PATTERN = {
            Pattern.compile("<meta[^;]+;\\s*charset\\s*=\\s*([^\"\\s]+)[^>]*>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<\\?xml.+?encoding=(?:\"|')([^\"']+)(?:\"|')[^?]*\\?>", Pattern.CASE_INSENSITIVE)};

    private HttpClient m_client;

    /**
     * Default constructor.
     */
    public HttpRequest() {
        this(new HttpClient(new MultiThreadedHttpConnectionManager()));
    }


    public HttpClient getClient() {
        return m_client;
    }


    /**
     * Default constructor using specified http client.
     *
     * @param client http client object.
     */
    public HttpRequest(HttpClient client) {
        this.m_client = client;
    }

    /**
     * Send a request method to the web server or web service, and get response.
     *
     * @param method http request method.
     * @return response string.
     * @throws UnrecoverableServiceException
     */
    public String sendRequest(HttpMethod method) throws UnrecoverableServiceException {
        String doc = null;
        try {
            int statusCode = m_client.executeMethod(method);
            switch (statusCode) {
                case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                    log.warn("Here is service internal error, " + method.getStatusLine());
                case HttpStatus.SC_OK:
                    doc = doSuccess(method);
                    break;
                case HttpStatus.SC_MOVED_PERMANENTLY:
                case HttpStatus.SC_MOVED_TEMPORARILY:
                case HttpStatus.SC_SEE_OTHER:
                case HttpStatus.SC_TEMPORARY_REDIRECT:
                    doc = doRedirect(method);
                    break;
                default:
                    handleError(method);
            }
        } catch (IOException ex) {
            throw new UnrecoverableServiceException("While comunicating with service, some error occured!", ex);
        } finally {
            method.abort();
            method.releaseConnection();
        }
        return doc;
    }

    /**
     * Create a new soap post method with specified url and soap input stream.
     *
     * @param url endpoint url.
     * @param in  post body input stream.
     * @return soap post method.
     */
    public HttpMethod createSoapMethod(String url, InputStream in) {
        RequestEntity entity = new InputStreamRequestEntity(in, "application/soap+xml");
        return createPostMethod(url, entity);
    }

    /**
     * Create a new soap post method with specified url and soap body.
     *
     * @param url      endpoint url.
     * @param postBody post body string.
     * @return soap post method.
     * @throws UnsupportedEncodingException
     */
    public HttpMethod createSoapMethod(String url, String postBody) throws UnsupportedEncodingException {
        RequestEntity entity = new StringRequestEntity(postBody, "application/soap+xml", "utf-8");
        return createPostMethod(url, entity);
    }

    /**
     * Create a new http post method with specified url, contentType and request entity.
     *
     * @param url    service url.
     * @param entity request entity.
     * @return http post method.
     */
    public HttpMethod createPostMethod(String url, RequestEntity entity) {
        PostMethod method = new PostMethod(url);
        if (entity != null) {
            method.setRequestEntity(entity);
        }
        method.setRequestHeader("Accept-Encoding", "gzip, deflate");
        return method;
    }

    /**
     * Create a new http get method with specified url and request query key-value pair.
     *
     * @param url    service url.
     * @param params query key-value pair.
     * @return http get method.
     */
    public HttpMethod createGetMethod(String url, NameValuePair[] params) {
        GetMethod method = new GetMethod(url);
        if (params != null) {
            method.setQueryString(params);
        }
        method.setRequestHeader("Accept-Encoding", "gzip, deflate");
        return method;
    }

    /**
     * Translate a input stream to a string with correct encoding, and filter the empty lines.
     *
     * @param in input stream.
     * @return string text.
     */
    public String loadFromStream(InputStream in) {
        StringBuilder sb = new StringBuilder();
        byte[] bytes = new byte[BLOCK_SIZE];
        try {
            int len = in.read(bytes);
            String encoding = resolveEncoding(bytes, len);
            if (log.isDebugEnabled()) {
                log.debug("Current encoding is " + encoding);
            }
            sb.append(stripEmptyLine(new String(bytes, 0, len, encoding)));
            while ((len = in.read(bytes)) > 0) {
                sb.append(stripEmptyLine(new String(bytes, 0, len, encoding)));
            }
        } catch (IOException e) {
            log.info("Maybe here is a chunk error, ignore it.", e);
        }
        return sb.toString();
    }


    /**
     * Filter ignoring tag of HTML content, here we will strip all invisible tags, such as
     * HEAD, STYLE, SCRIPT, COMMENT, etc...
     *
     * @param html HTML content.
     * @return pure HTML content.
     */
    public String stripIgnoreTag(String html) {
        Pattern pattern = Pattern.compile(IGNORE_TAG_REGEX, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
                | Pattern.DOTALL);
        Matcher m = pattern.matcher(html);
        return m.replaceAll("");
    }

    /**
     * Maybe we need use this method for throttling.
     */
    public void refresh() {
        HttpConnectionManager mgmt = m_client.getHttpConnectionManager();
        mgmt.closeIdleConnections(0);

        synchronized (this) {
            m_client = new HttpClient(new MultiThreadedHttpConnectionManager());
        }
    }


    private String doSuccess(HttpMethod method) throws IOException {
        InputStream in = method.getResponseBodyAsStream();
        Header contentEncodingHeader = method.getResponseHeader("Content-Encoding");
        if (contentEncodingHeader != null) {
            String contentEncoding = contentEncodingHeader.getValue();
            if (contentEncoding.toLowerCase(Locale.US).indexOf("gzip") != -1) {
                in = new GZIPInputStream(in);
            }
        }
        return loadFromStream(in);
    }

    private void handleError(HttpMethod method) throws UnrecoverableServiceException {
        log.error("Error Response: " + method.getStatusLine());
        throw new UnrecoverableServiceException(String.format("Can't get resposne, please check the status: %s", method
                .getStatusLine()));
    }

    private String doRedirect(HttpMethod method) throws IOException {
        Header locationHeader = method.getResponseHeader("location");
        if (locationHeader != null) {
            String location = locationHeader.getValue();
            if (location == null) {
                location = "/";
            }
            String redirectUrl = getRedirectUrl(method.getURI(), location);
            return sendRequest(createGetMethod(redirectUrl, null));
        } else {
            throw new UnrecoverableServiceException("Can't find the redirect URL for request : " + method.getURI());
        }
    }

    private String getRedirectUrl(URI origin, String location) throws URIException {
        String redirect = null;
        if (location.startsWith("http:")) {
            redirect = location;
        } else if (location.startsWith("/")) {
            origin.setPath(location);
            redirect = origin.getURI();
        } else {
            redirect = origin.getURI().replaceAll("(?<=/)[^/]+$", location);
        }
        return redirect;
    }

    private String resolveEncoding(byte[] bytes, int len) throws UnsupportedEncodingException {
        String detector = new String(bytes, 0, len, DEFAULT_ENCODING);
        String encoding = DEFAULT_ENCODING;
        for (Pattern p : ENCODING_PATTERN) {
            Matcher m = p.matcher(detector);
            if (m.find()) {
                encoding = m.group(1);
                break;
            }
        }
        return encoding;
    }

    private String stripEmptyLine(String input) {
        Matcher m = EMPTY_LINE.matcher(input);
        return m.replaceAll("");
    }
}
