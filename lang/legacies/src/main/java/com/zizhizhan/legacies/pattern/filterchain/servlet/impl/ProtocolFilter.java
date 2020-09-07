package com.zizhizhan.legacies.pattern.filterchain.servlet.impl;

import com.zizhizhan.legacies.pattern.filterchain.servlet.*;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@FilterConfig(order = 0)
public class ProtocolFilter implements Filter {

    private final Pattern headerPattern = Pattern.compile("^([^:]+):(.*)$");

    @Override
    public void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) {
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getIn()));
        try {
            String line = br.readLine();
            String[] bs = line.split("\\s");
            if (bs.length == 3) {

                request.setMethod(bs[0]);
                request.setResourcePath(bs[1]);
                while (((line = br.readLine()) != null) && !line.equals("")) {
                    Matcher m = headerPattern.matcher(line);
                    if (m.find()) {
                        request.put(m.group(1), m.group(2));
                        log.info("Load header {{} : {}}", m.group(1), m.group(2));
                    } else {
                        log.warn("Can't recognize the header: {}", line);
                    }
                }
                response.setStatusCode(200);
                response.setStatusText("OK");
            } else {
                log.info("Bad Request.");
                response.setStatusCode(400);
                response.setStatusText("Bad Request");
            }
        } catch (Exception ex) {
            log.info(ex.getLocalizedMessage(), ex);
            response.setStatusCode(500);
            response.setStatusText(ex.getLocalizedMessage());
        } finally {
            chain.doFilter(request, response);
        }
    }
}
