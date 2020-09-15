package com.zizhizhan.legacies.pattern.filterchain.servlet.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;

import com.zizhizhan.legacies.pattern.filterchain.servlet.*;
import com.zizhizhan.io.WorkingDirectory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FilterConfig(order = 120)
public class FileFilter implements Filter {

    final private File webroot = WorkingDirectory.getWorkingDirectory("www");

    @Override
    public void doFilter(HttpRequest request, HttpResponse response, FilterChain chain) {
        try {
            String path = request.getResourcePath();
            if (path.equals("/")) {
                path = "index.html";
            }
            if (response.getStatusCode() == 200 && path.endsWith(".html")) {

                File file = new File(webroot, path);
                log.info("{}-path: {}", file.getCanonicalPath(), path);

                PrintStream out = response.getPrintStream();

                if (file.exists() && file.isFile()) {
                    ResponseBuilder.buildHeader(response);
                    InputStream fin = new FileInputStream(file);
                    byte[] content = new byte[(int) file.length()];
                    fin.read(content);
                    out.write(content);
                    out.flush();
                    out.close();
                    fin.close();
                } else {
                    log.info("File not found: {}", file.getCanonicalFile());
                    response.setStatusCode(404);
                    response.setStatusText("Not Found");
                    ResponseBuilder.buildHeader(response);

                    out.flush();
                    out.close();
                }
            } else {
                chain.doFilter(request, response);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
