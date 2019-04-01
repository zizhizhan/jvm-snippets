package me.jameszhan.rt;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * User: James Zhan
 * Email: zhiqiangzhan@gmail.com
 * Date: 2018-12-30
 * Time: 13:21
 */
@Slf4j
public class OpenBrowser {

    public static void main(String[] args) {
        String url = "https://weibo.com";
        try {
            Runtime rt = Runtime.getRuntime();
            String osName = getProperty("os.name", "linux");

            try {
                Class<?> desktopClass = Class.forName("java.awt.Desktop");
                // Desktop.isDesktopSupported()
                Boolean supported = (Boolean) desktopClass.getMethod("isDesktopSupported").
                        invoke(null, new Object[0]);
                URI uri = new URI(url);
                if (supported) {
                    // Desktop.getDesktop();
                    Object desktop = desktopClass.getMethod("getDesktop").invoke(null);
                    // desktop.browse(uri);
                    desktopClass.getMethod("browse", URI.class).invoke(desktop, uri);
                    return;
                }
            } catch (Exception e) {
                log.info("Ignore desktop start.", e);
            }
            if (osName.contains("windows")) {
                rt.exec(new String[] { "rundll32", "url.dll,FileProtocolHandler", url });
            } else if (osName.contains("mac") || osName.contains("darwin")) {
                // Mac OS: to open a page with Safari, use "open -a Safari"
                Runtime.getRuntime().exec(new String[] { "open", url });
            } else {
                String[] browsers = { "xdg-open", "chromium", "google-chrome",
                        "firefox", "mozilla-firefox", "mozilla", "konqueror",
                        "netscape", "opera", "midori" };
                boolean ok = false;
                for (String b : browsers) {
                    try {
                        rt.exec(new String[] { b, url });
                        ok = true;
                        break;
                    } catch (Exception e) {
                        log.info("Ignore rt exec {}.", b, e);
                    }
                }
                if (!ok) {
                    // No success in detection.
                    throw new Exception("Browser detection failed and system property BROWSER not set");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to start a browser to open the URL " + url + ": " + e.getMessage());
        }
    }

    private static String getProperty(String key, String defaultValue) {
        try {
            return System.getProperty(key, defaultValue);
        } catch (SecurityException se) {
            return defaultValue;
        }
    }

}
