package com.zizhizhan.bookshelf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zizhizhan.bookshelf.domain.BookShelf;
import com.zizhizhan.bookshelf.domain.Document;
import com.zizhizhan.compress.lzma.Lzma;

public class Main {

    public static void main(String[] args) throws IOException {
        BookShelf bookShelf = new BookShelf("Y:/ebooks/Bookshelf");
        List<Document> books = bookShelf.search();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File jsonfile = new File(".books/json_final.lzma");
        if (!jsonfile.exists()) {
            Lzma.encode(gson.toJson(books).getBytes(StandardCharsets.UTF_8), jsonfile);
        } else {
            Type type = new TypeToken<List<Document>>() {
            }.getType();

            byte[] result = Lzma.decode(jsonfile);
            List<Document> list = gson.fromJson(new String(result, StandardCharsets.UTF_8), type);
            for (Document doc : list) {
                System.out.println(doc);
            }
        }
    }

    public static void normal(String[] args) throws IOException {
        BookShelf bookShelf = new BookShelf("Y:/ebooks/Bookshelf");
        List<Document> books = bookShelf.search();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File jsonfile = new File(".books/json.data");
        if (!jsonfile.exists()) {
            Writer out = new OutputStreamWriter(new FileOutputStream(jsonfile), "utf-8");
            out.write(gson.toJson(books));
            out.close();
        } else {
            Type type = new TypeToken<List<Document>>() {
            }.getType();
            List<Document> list = gson.fromJson(new InputStreamReader(new FileInputStream(jsonfile), "utf-8"), type);
            System.out.println(list);
        }
    }

}
