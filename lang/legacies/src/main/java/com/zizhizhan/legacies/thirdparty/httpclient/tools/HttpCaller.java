package com.zizhizhan.legacies.thirdparty.httpclient.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

@Slf4j
public class HttpCaller {

    private static final int BLOCK_SIZE = 2048;
    private static final String DEFAULT_ENCODING = "iso8859-1";
    private static final Pattern EMPTY_LINE = Pattern.compile("\n^\\s*$", Pattern.MULTILINE | Pattern.UNIX_LINES);
    private static final String IGNORE_TAG_REGEX = "<(head|script|style|form|select|iframe|comment)[^>]*>.*?</\\1>";
    private static final Pattern CONTENT_TYPE_PATTERN = Pattern.compile("charset=(.+)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern[] ENCODING_PATTERN = {
            Pattern.compile("<meta[^;]+;\\s*charset\\s*=\\s*([^\"\\s]+)[^>]*>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<\\?xml.+?encoding=(?:\"|')([^\"']+)(?:\"|')[^?]*\\?>", Pattern.CASE_INSENSITIVE)
    };

    private HttpClient m_httpClient;
    private HttpClientParams m_params;

    private ExecutorService m_threadPool;
    private Semaphore m_semaphore;

    public HttpCaller() {
        this(20, 0, false);
    }

    public HttpCaller(int maxConcurrency, int connectTimeout, boolean singleton) {
        HttpClientParams params = getDefaultHttpClientParams();
        if (connectTimeout > 0) {
            params.setConnectionManagerTimeout(connectTimeout);
        }

        if (singleton) {
            params.setConnectionManagerClass(MultiThreadedHttpConnectionManager.class);
            m_httpClient = new HttpClient(params);
        }

        m_params = params;

        m_semaphore = new Semaphore(maxConcurrency);
        m_threadPool = new ThreadPoolExecutor(1, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(true), new DaemonThreadFactory());
    }

    public void execute(final HttpMethod method) {
        m_semaphore.acquireUninterruptibly();
        m_threadPool.execute(new Runnable() {
            public void run() {
                try {
                    callService(method);
                } finally {
                    m_semaphore.release();
                }
            }
        });
    }


    public void callService(HttpMethod method) {
        String response = String.format("Unexpected Exception for get reponse for url: %s, ", method.getUrl());
        GetMethod get = null;
        HttpClient client = getHttpClient();
        try {
            get = new GetMethod(method.getUrl());
            get.setRequestHeader("User-Agent", "HttpCaller/1.0");
            get.setRequestHeader("Accept-Encoding", "gzip, deflate");
            for (String key : method.getHeaders().keySet()) {
                get.setRequestHeader(key, method.getHeaders().get(key));
            }
            int statusCode = client.executeMethod(get);
            if (statusCode != HttpStatus.SC_OK) {
                log.warn("There is some error reponse returned, " + get.getStatusLine()
                        + "\n url: " + method.getUrl());
            }

            InputStream in = get.getResponseBodyAsStream();
            Header contentEncodingHeader = get.getResponseHeader("Content-Encoding");
            if (contentEncodingHeader != null) {
                String contentEncoding = contentEncodingHeader.getValue();
                if (contentEncoding.toLowerCase(Locale.US).indexOf("gzip") != -1) {
                    in = new GZIPInputStream(in);
                }
            }
            String encoding = resolveEncoding(get.getResponseHeader("Content-Type").getValue());
            response = loadFromStream(in, encoding);

            for (Header h : get.getResponseHeaders()) {
                mergeMap(method.getHeaders(), h.getName(), h.getValue());
            }
            for (Header h : get.getRequestHeaders()) {
                mergeMap(method.getHeaders(), h.getName(), h.getValue());
            }
            for (Header h : get.getResponseFooters()) {
                mergeMap(method.getHeaders(), h.getName(), h.getValue());
            }
        } catch (Exception e) {
            log.error("Call service error for url: " + method.getUrl(), e);
            response += e.getMessage();
        } finally {
            method.setResponse(response);
            get.abort();
            get.releaseConnection();
            client.getState().clear();
        }
    }

    public String loadFromStream(InputStream in, String encoding) {
        StringBuilder sb = new StringBuilder();
        byte[] bytes = new byte[BLOCK_SIZE];
        try {
            int len = in.read(bytes);
            if (encoding == null) {
                encoding = resolveEncoding(bytes, len);
                if (log.isDebugEnabled()) {
                    log.debug("Current encoding is " + encoding);
                }
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

    public String stripIgnoreTag(String html) {
        Pattern pattern = Pattern.compile(IGNORE_TAG_REGEX, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
                | Pattern.DOTALL);
        Matcher m = pattern.matcher(html);
        return m.replaceAll("");
    }

    private String resolveEncoding(String contentType) throws UnsupportedEncodingException {
        String encoding = null;
        if (contentType != null) {
            Matcher m = CONTENT_TYPE_PATTERN.matcher(contentType);
            if (m.find()) {
                encoding = m.group(1);
                if (log.isDebugEnabled()) {
                    log.debug("From response header, current encoding is " + encoding);
                }
            }
        }
        return encoding;
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

    private HttpClient getHttpClient() {
        if (m_httpClient != null) {
            return m_httpClient;
        } else {
            return new HttpClient(m_params);
        }
    }

    private void mergeMap(Map<String, String> map, String key, String value) {
        String v = map.get(key);
        if (v != null) {
            value = v + '\n' + value;
        }
        map.put(key, value);
    }

    public void shutdown() {
        if (m_httpClient != null) {
            m_httpClient.getHttpConnectionManager().closeIdleConnections(0);
        }
        m_threadPool.shutdown();
    }

    private static HttpClientParams getDefaultHttpClientParams() {
        HttpClientParams params = new HttpClientParams();
        params.setParameter(HttpMethodParams.USER_AGENT, "HttpCaller/1.0");
        params.setCookiePolicy(CookiePolicy.RFC_2109);
        params.setHttpElementCharset("US-ASCII");
        params.setContentCharset("ISO-8859-1");
        params.setConnectionManagerClass(SimpleHttpConnectionManager.class);
        params.setConnectionManagerTimeout(120 * 1000);
        params.setSoTimeout(300 * 1000);
        params.setVersion(HttpVersion.HTTP_1_1);
        return params;
    }

    private class DaemonThreadFactory implements ThreadFactory {
        private AtomicInteger m_threadId = new AtomicInteger();

        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setPriority(Thread.MAX_PRIORITY);
            t.setName(String.format("httpcaller-pool-%02d", m_threadId.incrementAndGet()));
            return t;
        }
    }

}
