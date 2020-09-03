package com.zizhizhan.legacies.thirdparty.httpclient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formattable;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zizhizhan.legacies.thirdparty.httpclient.tools.HttpRequest;
import com.zizhizhan.legacies.util.HtmlUtils;
import junit.framework.TestCase;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

@Slf4j
public class GoogleSiteIntgTests extends TestCase {
    private static final String DEFAULT_ENCODING = "iso8859-1";
    private static final String EXPEDIA = "Expedia";
    private static final String GOOGLE_URL_JMP_PREFIX = "http://maps.google.com/local_url?q=";

    private static final String UNEXPECTED_EXCEPTION_THROWN = "Unexpected exception thrown";
    private static final String PRICE_DONT_MATCH = "The price in Google site and Expedia site are not match";
    private static final String NO_EXPEDIA_PRICE_FOR_THIS_HOTEL = "Can't find Expedia price information for this hotel.";

    private static final String IGNORE_TAG_REGEX =
            "(<(head|script|style|form|select|iframe|comment)[^>]*>.*?</\\1>)|(<img[^>]+>|<a[^>]+>|</a>)";

    private static final String AGENT_BLOCK = "<div\\s*class=goog-menuitem\\s*onclick=\"window.open\\('?([^)]+)'?\\)\">\\s*"
            + "(?:<span(?:.(?!</span>))+.</span>)?\\s*([^<]+)\\s*<br\\s*/?>(?:\\s*<span(?:.(?!</span>))+.</span>)+\\s*</div>\\s*";

    private static final String OWN_SITE = "<div\\s*class=\"[^\"]+\"\\s*onclick=\"window.open\\([^)]+\\)\">\\s*"
            + "<span(?:.(?!</span>))+.</span>\\s*<br\\s*/?>\\s*<span(?:.(?!</span>))+.</span>\\s*</div>\\s*";

    private static final String HOTEL_INFO = "<div(?:.(?!</div>))+.</div>\\s*<div[^>]+>\\s*"
            + "<div[^>]+>\\s*<span[^>]+>((?:.(?!</span>))+.)</span>\\s*(?:.(?!</div>))+.</div>\\s*"
            + "<div(?:(?:.(?!</div>))+.</div>){5}";

    private static final String GOOGLE_PATTERN = "<div\\sclass=one>\\s*<div\\sclass=booklink>(?:.(?!</div>))+.</div>\\s*"
            + "<div\\s*class=dMenu\\s*style=display:none>\\s*" + "((?:" + AGENT_BLOCK + ")+)"
            + "(?:" + OWN_SITE + ")?\\s*</div>\\s*" + HOTEL_INFO;


    private static HttpRequest s_client = new HttpRequest();

    public void testGooglePageSearch() throws Exception {
        doGooglePageSearch("Bellevue+hotels", 0, 15, 1);
        //doGooglePageSearch(searchHotelName, searchPageStart, searchPageCount, searchPageStep);
    }

    public void otestSpecifiedGoogleSearch() throws Exception {
        String[] searchHotelNames = {"Trump+Taj Mahal Casino Resort", "Boston Copley Square Hotel", "Hudson Hotel"};
        doGooglePageSearch(searchHotelNames);
    }

    private void doGooglePageSearch(String... queryStrings) throws Exception {
        final StringBuffer results = new StringBuffer();
        Formatter fmt = new Formatter(results, Locale.US);
        HttpMethod[] requests = new HttpMethod[queryStrings.length];

        for (int i = 0; i < queryStrings.length; i++) {
            requests[i] = createGoogleMapRequest(queryStrings[i], 0);
            fmt.format("\nBegin to search: %s", requests[i].getURI());
        }
        doGooglePageSearch(results, requests);
    }

    private void doGooglePageSearch(String queryString, int pageStart, int pageCount, int pageSkip) throws Exception {
        final StringBuffer results = new StringBuffer();
        Formatter fmt = new Formatter(results, Locale.US);
        fmt.format("Begin to search http://maps.google.com/maps?f=q&hl=en&ie=utf8&q=%s, start from %d to %d, step is %d.",
                queryString, pageStart, (pageStart + pageSkip * pageCount) * 10, pageSkip);

        HttpMethod[] requests = new HttpMethod[pageCount];

        int i = 0;
        do {
            int start = (i * pageSkip + pageStart) * 10;
            requests[i] = createGoogleMapRequest(queryString, start);
            i++;
        } while (i < pageCount);

        doGooglePageSearch(results, requests);
    }

    private void doGooglePageSearch(final Appendable results, HttpMethod... googleSearchRequests) throws Exception {
        final ExecutorService searchThreadPool = Executors.newFixedThreadPool(googleSearchRequests.length,
				new NamedThreadFactory("GoogleSearch"));
        final ExecutorService execThreadPool = Executors.newCachedThreadPool(new NamedThreadFactory("Deeplink"));
        final ExecutorService summaryThreadPool = Executors.newSingleThreadExecutor(new NamedThreadFactory("SummaryThread"));

        try {
            final AtomicInteger failCountHolder = new AtomicInteger();
            final AtomicInteger totalCountHolder = new AtomicInteger();
            final CountDownLatch latch = new CountDownLatch(googleSearchRequests.length);
            Formatter fmt = new Formatter(results, Locale.US);
            fmt.format("\n%-10s %-40s %-16s %-16s %s", "HotelID", "HotelName", "GooglePrice", "ExpediaPrice",
                    "Match/Didn't match");
            for (final HttpMethod request : googleSearchRequests) {
                final Future<List<Future<SearchResult>>> f = searchThreadPool.submit(new Callable<List<Future<SearchResult>>>() {
                    public List<Future<SearchResult>> call() throws Exception {
                        return lanuchGoogleSearch(request, execThreadPool);
                    }
                });

                summaryThreadPool.execute(new Runnable() {
                    public void run() {
                        try {
                            final List<Future<SearchResult>> futures = f.get();
                            int status = doSummary(futures, results);
                            update(totalCountHolder, status >> 16);
                            update(failCountHolder, status & 0xFFFF);
                        } catch (Exception e) {
                            log.info("results = {}", results);
                            throw new IllegalStateException(e);
                        } finally {
                            latch.countDown();
                        }
                    }
                });
            }

            latch.await();

            int failCount = failCountHolder.get();
            int totalCount = totalCountHolder.get();

            results.append(String.format("\n%.2f%% price compare have passed!",
                    (1 - failCount * 1.0 / totalCount) * 100));
            log.info(fmt.toString());
            if (failCount > 0) {
                fail();
            }
        } finally {
            summaryThreadPool.shutdown();
            execThreadPool.shutdown();
        }
    }

    private List<Future<SearchResult>> lanuchGoogleSearch(final HttpMethod request, ExecutorService threadPool) throws Exception {
        log.info("Lanuch google search for: {}.", request.getURI());
        String content = s_client.sendRequest(request);
        content = stripIgnoreTag(content);
        List<Callable<SearchResult>> tasks = new ArrayList<Callable<SearchResult>>();
        final Matcher m = matches(GOOGLE_PATTERN, content);
        int start = 0;
        while (m.find(start)) {
            final String hotelName = m.group(4).replaceAll("<[^>]+>", "");
            final String agentBlock = m.group(1);
            tasks.add(new Callable<SearchResult>() {
                public SearchResult call() throws Exception {
                    return doSearchResult(hotelName, agentBlock, request.getURI().toString());
                }
            });
            start = m.end();
        }

        return threadPool.invokeAll(tasks);
    }


    private int doSummary(List<Future<SearchResult>> futures, Appendable results) throws Exception {
        StringBuilder sb = new StringBuilder();
        Formatter fmt = new Formatter(sb, Locale.US);
        int failCount = 0;
        int totalCount = 0;
        for (Future<SearchResult> future : futures) {
            SearchResult result = null;
            try {
                result = future.get();
            } catch (Exception e) {
                result = new SearchResult();
                result.promptMessage = "future.get() exception," + e.getMessage();
                result.exception = e;
            }
            if (!result.succ) {
                failCount++;
            }
            totalCount++;
            String status = String.format("%B%s", result.succ,
                    result.promptMessage == null ? "" : "(" + result.promptMessage + ")");
            fmt.format("\n%-10s %-40s %-16.2s %-16.2s %s", formatObject(result.hotelId),
                    formatObject(result.hotelName, 40), formatObject(result.priceInGoogleSite),
                    formatObject(result.priceInExpediaSite), status);
        }
        fmt.format("\n-");

        results.append(sb);
        return totalCount << 16 | failCount;
    }

    public HttpMethod createGoogleMapRequest(String query, Integer start) {
        NameValuePair p1 = new NameValuePair("f", "q");
        NameValuePair p2 = new NameValuePair("hl", "en");
        NameValuePair p3 = new NameValuePair("ie", "UTF8");
        NameValuePair p4 = new NameValuePair("q", query);
        NameValuePair p5 = new NameValuePair("start", start.toString());

        return createGoogleMapRequest(p1, p2, p3, p4, p5);
    }

    public HttpMethod createGoogleMapRequest(NameValuePair... params) {
        HttpMethod get = new GetMethod("http://maps.google.com/maps");
        get.setQueryString(params);
        return get;
    }

    private SearchResult doSearchResult(final String hotelName, final String agentBlock, final String googleURL) {
        SearchResult result = new SearchResult();

        result.hotelName = HtmlUtils.htmlUnEscape(hotelName);
        result.googleURL = googleURL;
        fillSearchResult(agentBlock, result);
        result.verify();
        return result;
    }

    private void fillSearchResult(final String agentBlock, final SearchResult result) {
        Matcher m = matches(AGENT_BLOCK, agentBlock);
        int start = 0;
        while (m.find(start)) {
            if (m.group(2) != null && m.group(2).contains(EXPEDIA)) {
                try {
                    String deeplinkURL = normalizeExpediaDeeplinkURL(m.group(1));
                    result.deepLinkURL = deeplinkURL;
                    result.priceInGoogleSite = getGoogleSitePrice(m.group(2));
                    result.priceInExpediaSite = getExpediaSitePrice(deeplinkURL);
                } catch (Throwable t) {
                    log.warn("There is some exception occured!", t);
                    result.exception = t;
                }
                break;
            }
            start = m.end();
        }
    }

    private String normalizeExpediaDeeplinkURL(String url) {
        if (url.startsWith(GOOGLE_URL_JMP_PREFIX)) {
            // Strip GOOGLE URL jump prefix.
            url = url.substring(GOOGLE_URL_JMP_PREFIX.length());
        }
        try {
            // Decode the URL.
            url = URLDecoder.decode(url, DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            // Ignore.
        }
        url = url.substring(0, url.indexOf("&dq"));
        log.info("Prepare to launch deeplink {}", url);

        return url;
    }

    private double getGoogleSitePrice(String priceBlock) {
        Matcher m = matches("^\\s*[^\\d]{0,3}([\\d,.]+)[\\w\\W]+$", priceBlock);
        if (m.find()) {
            return parseWebsiteDoublePrices(m.group(1));
        } else {
            throw new IllegalStateException("Can't find expedia hotel price from google map page {"
                    + priceBlock + "}");
        }
    }

    private double getExpediaSitePrice(String deeplinkURL) throws Exception {
        //String deepLinkContent = launchMyDeeplink(deeplinkURL);
        //String taxesAndFees = launchTaxesAndFeesPage(deepLinkContent);
        //double totalPrice = getSummaryPagePriceInfo(taxesAndFees, 1)[0];
        return 0.0;
    }

    private void update(AtomicInteger holder, int increment) {
        int oldValue = 0;
        int newValue = 0;
        do {
            oldValue = holder.get();
            newValue = oldValue + increment;
            System.out.format("%d, %d\n", oldValue, newValue);
        } while (!holder.compareAndSet(oldValue, newValue));
    }

    private Object formatObject(Object o) {
        return formatObject(o, 0);
    }

    private Object formatObject(Object o, int width) {
        if (o == null) {
            return "N/A";
        }
        if (o instanceof Number) {
            final Number num = (Number) o;
            return (Formattable) (formatter, flags, width1, precision) -> {
				if (num.longValue() == 0) {
					formatter.format("%-" + width1 + "s", "N/A");
				} else {
					formatter.format("%-" + width1 + "." + precision + "f", num);
				}
			};
        }

        if (o instanceof String && width > 0) {
            String s = (String) o;
            if (s.length() > width) {
                return s.substring(0, width - 3) + "...";
            }
        }
        return o;
    }

    public static double parseWebsiteDoublePrices(String numberString) {
        String pureString = numberString.replaceAll("[,|.|\\s]", "");
        double price = Double.parseDouble(pureString);

        if (numberString.length() >= 3) {
            char c = numberString.charAt(numberString.length() - 3);
            if (c < '0' || c > '9') {
                price /= 100;
            }
        }

        return price;
    }

    public static String stripIgnoreTag(String html) {
        Matcher m = matches(IGNORE_TAG_REGEX, html);
        return m.replaceAll("");
    }

    protected static Matcher matches(String regex, String content) {
        Pattern p = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        return p.matcher(content);
    }

    private static class SearchResult {
        private final Date timestamp = new Date();

        String hotelId;
        String hotelName;
        String deepLinkURL;
        String googleURL;
        double priceInGoogleSite;
        double priceInExpediaSite;
        boolean succ;
        String promptMessage;
        Throwable exception;

        void verify() {
            hotelId = getHotelId();
            if (exception != null) {
                promptMessage = String.format("%s:%s, see deeplink:%s, time:(%4$tY-%4$tm-%4$td %4$tT)",
                        UNEXPECTED_EXCEPTION_THROWN, exception.getMessage(), deepLinkURL, timestamp);
                succ = false;
            } else if (deepLinkURL == null) {
                promptMessage = NO_EXPEDIA_PRICE_FOR_THIS_HOTEL;
                succ = true;
            } else {
                if (Math.round(priceInGoogleSite) == Math.round(priceInExpediaSite)) {
                    succ = true;
                } else {
                    promptMessage = String.format("%s, link: %s, time:(%3$tY-%3$tm-%3$td %3$tT)",
                            PRICE_DONT_MATCH, googleURL, timestamp);
                    succ = false;
                }
            }
        }

        private String getHotelId() {
            if (deepLinkURL == null) {
                return null;
            }
            Matcher m = matches("HotID=([^&]+)", deepLinkURL);
            if (m.find()) {
                return m.group(1);
            }
            return null;
        }
    }

    public static class NamedThreadFactory implements ThreadFactory {
        private final String name;
		private final AtomicInteger id = new AtomicInteger();
		private boolean daemon;

        public NamedThreadFactory(String name) {
            super();
            this.name = name;
        }

        public NamedThreadFactory(String name, boolean daemon) {
            super();
            this.name = name;
            this.daemon = daemon;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            String threadName = name;
            if (id.get() > 0) {
                threadName += String.format("-%2d", id.get());
            }
            t.setName(threadName);
            t.setDaemon(daemon);
            return t;
        }
    }

}