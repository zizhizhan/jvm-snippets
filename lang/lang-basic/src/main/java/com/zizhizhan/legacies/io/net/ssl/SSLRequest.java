package com.zizhizhan.legacies.io.net.ssl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class SSLRequest {

    public static void main(String[] args) throws Exception {
        String rawUrl = "http://www.baidu.com";
        URL requestURL = new URL(rawUrl);
        HttpURLConnection request = (HttpURLConnection) requestURL.openConnection();

        request.setRequestProperty("User-Agent", "SSLRequest/1.0");
        request.setRequestMethod("GET");

        String content = "abc";
        initializeSetting(request, "GET", content, "text/html", "100");
        request.connect();

        InputStreamReader reader = null;
        try {
            OutputStream out = request.getOutputStream();
            if (null != content) {
                out.write(content.getBytes("utf-8"));
            }
            out.close();
            reader = new InputStreamReader(request.getInputStream(), "utf-8");
        } catch (ConnectException e) {
            System.err.println("\nError: HTTPClient.HTTPRequest():Could not connect to '" + rawUrl
                    + "', is the http server alive?\n");

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            reader = new InputStreamReader(((HttpURLConnection) request).getErrorStream());
        }

        StringBuffer buffer = new StringBuffer();
        BufferedReader response = new BufferedReader(reader);
        String line;
        while (null != (line = response.readLine())) {
            buffer.append(line).append("\r\n");
        }
        response.close();
        System.out.println(buffer);
    }

    private static void initializeSetting(URLConnection request, String requestType, String content,
										  String typeStr, String msTimeout) {

        if (!"GET".equals(requestType)) {
            request.setRequestProperty("Content-Length", String.valueOf(content.getBytes().length));
            request.setRequestProperty("Content-Type", typeStr);
        }

        if (null != msTimeout) {
            int timeout = 0;
            try {
                timeout = Integer.parseInt(msTimeout);
            } catch (NumberFormatException nfe) {
                timeout = 0;
            }

            if (timeout > 50) {
                // necessary for Connection: Keep-Alive, but not that useful for
                // Connection: close
                request.setReadTimeout(timeout);
            }
        }

        request.setDoOutput(true);
        request.setDoInput(true);
    }

}
