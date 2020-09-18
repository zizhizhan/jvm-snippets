package com.zizhizhan.bookshelf;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.zizhizhan.bookshelf.domain.Document;
import com.zizhizhan.compress.lzma.Lzma;
import lombok.extern.slf4j.Slf4j;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Slf4j
public class AllBooks {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static List<Document> books;

    static {
        init();
    }

    public static List<Document> get() {
        return books;
    }

    private static void init() {
        File jsonfile = new File("./samples/Bookshelf/.books/json_final.lzma");
        Type type = new TypeToken<List<Document>>() {}.getType();
        try {
            byte[] result = Lzma.decode(jsonfile);
            books = gson.fromJson(new String(result, StandardCharsets.UTF_8), type);
        } catch (IOException ex) {
            log.warn("Can't load {}.", jsonfile, ex);
            books = new ArrayList<>();
        }
    }

}
