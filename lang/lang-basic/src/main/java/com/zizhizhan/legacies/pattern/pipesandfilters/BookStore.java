package com.zizhizhan.legacies.pattern.pipesandfilters;

import java.util.ArrayList;
import java.util.List;

public class BookStore {

    private final List<Book> books = new ArrayList<Book>();

    {
        int i = 0;
        books.add(new Book(i++, "ABCD", "en", 100.0, 5, "save"));
        books.add(new Book(i++, "ABDC", "en", 80.0, 3, "save"));
        books.add(new Book(i++, "ACBD", "en", 100.0, 4, "save"));
        books.add(new Book(i++, "ACDB", "en", 75.0, 1, "save"));
        books.add(new Book(i++, "ADBC", "en", 10.0, 5, "save"));
        books.add(new Book(i++, "ADCB", "en", 100.0, 3, "save"));
        books.add(new Book(i++, "BACD", "de", 300.0, 2, "save"));
        books.add(new Book(i++, "BADC", "de", 20.0, 3, "save"));
        books.add(new Book(i++, "BCAD", "de", 60.0, 5, "save"));
        books.add(new Book(i++, "BCDA", "de", 100.0, 2, "save"));
        books.add(new Book(i++, "BDAC", "de", 100.0, 3, "save"));
        books.add(new Book(i++, "BDCA", "de", 56.0, 1, "save"));
        books.add(new Book(i++, "CABD", "fr", 100.0, 3, "save"));
        books.add(new Book(i++, "CADB", "fr", 600.0, 4, "save"));
        books.add(new Book(i++, "CBAD", "fr", 25.0, 3, "save"));
        books.add(new Book(i++, "CBDA", "fr", 100.0, 5, "save"));
        books.add(new Book(i++, "CDAB", "fr", 800.0, 3, "save"));
        books.add(new Book(i++, "CDBA", "fr", 1200.0, 2, "save"));
        books.add(new Book(i++, "DABC", "zh", 100.0, 2, "save"));
        books.add(new Book(i++, "DACB", "zh", 89.0, 1, "save"));
        books.add(new Book(i++, "DBAC", "zh", 98.0, 1, "save"));
        books.add(new Book(i++, "DBCA", "zh", 35.0, 3, "save"));
        books.add(new Book(i++, "DCAB", "zh", 28.0, 4, "save"));
        books.add(new Book(i++, "DCBA", "zh", 120.0, 3, "save"));
    }

    public List<Book> getBooks() {
        return books;
    }

}
