package com.zizhizhan.legacies.thirdparty.jaxws.bookshelf.web;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.zizhizhan.legacies.thirdparty.jaxws.bookshelf.AllBooks;
import com.zizhizhan.legacies.thirdparty.jaxws.bookshelf.domain.Document;
import com.zizhizhan.legacies.util.ReflectionHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
public class BookShelf {

    private static final Pattern QUERY_PATTERN = Pattern.compile("^(\\w+)\\s*(>|<|=)\\s*(.+)$");
    private List<Document> books = AllBooks.get();

    @GET
    public List<Document> books() {
        return books;
    }

    @GET
    @Path("/{feature}")
    public Object book(@PathParam("feature") String feature) {
        if ("size".equalsIgnoreCase(feature)) {
            return books.size();
        } else {
            return 0;
        }
    }

    @GET
    @Path("/paging/{pageid}")
    public List<Document> search(@PathParam("pageid") int pageid) {
        int pagesize = 30;
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
            return books.subList(start, end);
        }
        return null;
    }

    @GET
    @Path("query/{query}")
    public List<Document> query(@PathParam("query") String queryString) {
        log.info("Query {}", queryString);
        List<Document> docs = new ArrayList<Document>();

        for (Document doc : books) {
            Matcher m = QUERY_PATTERN.matcher(queryString);
            if (m.find()) {
                String fieldName = m.group(1);
                String operator = m.group(2);
                String data = m.group(3);
                Object val = ReflectionHelper.getFieldValue(doc, fieldName);

                if (val != null && accept(val, operator, data)) {
                    docs.add(doc);
                }
            }
        }
        return docs;
    }

    private boolean accept(Object val, String operator, String data) {
        if (val instanceof String) {
            return Pattern.matches(".*" + data + ".*", val.toString());
        } else if (val instanceof Number) {
            Long l = parseLong(data);
            if (">".equals(operator)) {
                return ((Number) val).longValue() > l;
            } else if ("<".equals(operator)) {
                return ((Number) val).longValue() < l;
            } else {
                return ((Number) val).longValue() == l;
            }
        } else if (val instanceof List) {
            for (Object o : (List<?>) val) {
                if (o.equals(data)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static Long parseLong(String number) {
        try {
            return Long.parseLong(number);
        } catch (Exception e) {
            return 0L;
        }
    }

}
