package com.zizhizhan.legacies.thirdparty.jaxws.bookshelf.web;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zizhizhan.legacies.compress.Lzma;
import com.zizhizhan.legacies.thirdparty.jaxws.bookshelf.domain.Document;
import lombok.extern.slf4j.Slf4j;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Slf4j
public class SimplyBookShelf extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String UTF8 = "UTF-8";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private List<Document> books;

    @Override
    public void init() throws ServletException {
        File jsonfile = new File(".books/json_final.lzma");
        Type type = new TypeToken<List<Document>>() {
        }.getType();
        try {
            byte[] result = Lzma.decode(jsonfile);
            books = gson.fromJson(new String(result, UTF8), type);
        } catch (IOException ex) {
            log.warn("", ex);
            books = new ArrayList<Document>();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	/*	File jsonfile = new File(".books/json_final.lzma");
		byte[] result = Lzma.decode(jsonfile);	
		resp.setCharacterEncoding(UTF8);
		resp.setContentType("application/json");
		String json = new String(result, UTF8);	
		resp.getWriter().println(json);*/

        int pagesize = 30;
        int pageid = getPageId(req);
        if (books != null) {
            int start = pagesize * pageid;
            int end = start + pagesize;
            if (start > books.size()) {
                start = books.size() - pagesize;
                if (start < 0) {
                    start = 0;
                }
            }
            if (end > books.size()) {
                end = books.size();
            }
            resp.setCharacterEncoding(UTF8);
            resp.setContentType("application/json");
            String json = gson.toJson(books.subList(start, end));
            resp.getWriter().println(json);
        }
    }

    private int getPageId(HttpServletRequest req) {
        int pageid = 0;
        String pi = req.getParameter("pageid");
        if (pi != null) {
            try {
                pageid = Integer.parseInt(pi);
            } catch (Exception e) {
                log.info("GetPageId", e);
            }
        }
        return pageid;
    }

}
