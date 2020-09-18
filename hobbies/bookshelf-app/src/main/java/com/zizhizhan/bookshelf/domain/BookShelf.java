package com.zizhizhan.bookshelf.domain;

import lombok.extern.slf4j.Slf4j;

import static com.zizhizhan.bookshelf.utils.BookUtils.author;
import static com.zizhizhan.bookshelf.utils.BookUtils.bookName;
import static com.zizhizhan.bookshelf.utils.BookUtils.documentType;
import static com.zizhizhan.bookshelf.utils.BookUtils.starRating;
import static com.zizhizhan.bookshelf.utils.BookUtils.version;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class BookShelf {

    private final AtomicLong generator = new AtomicLong();
    private final List<Document> books = new ArrayList<>();
    private final File root;

    public BookShelf(String path) {
        super();
        this.root = new File(path);
    }

    public List<Document> search() {
        if (root.exists() && root.isDirectory()) {
            visit(root);
        }
        return books;
    }

    private void visit(File dir) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                visit(file);
            } else if (file.isFile()) {
                addDocument(file);
            }
        }
    }

    private void addDocument(File doc) {
        try {
            DocumentBuilder builder = new DocumentBuilder();
            String filename = doc.getName();
            String[] tags = doc.getParent().substring(root.getPath().length() + 1).split("[\\\\/]");
            builder.setId(generator.incrementAndGet()).setName(bookName(filename)).setSize(doc.length())
                    .setLastModified(doc.lastModified()).setTags(tags).setPath(doc.getCanonicalPath())
                    .setType(documentType(filename)).setStarRating(starRating(tags))
                    .setAuthors(author(filename)).setVersion(version(filename));
            books.add(builder.build());
        } catch (Exception e) {
            log.info("Unexpected Exception.", e);
        }
    }
}
