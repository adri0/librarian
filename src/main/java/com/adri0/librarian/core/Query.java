package com.adri0.librarian.core;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * A query object is instantiated when `Library.search()` method is used. The actual
 * search is only performed when the method `execute` is called.
 */
public class Query {

    private Stream<Book> queryStream;

    Query(Collection<Book> bookSet) {
        this.queryStream = bookSet.stream();
    }

    public Query by(Predicate<Book> predicate) {
        queryStream = queryStream.filter(predicate);
        return this;
    }

    public Query byAuthor(String author) {
        return by(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()));
    }

    public Query byTitle(String title) {
        return by(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()));
    }

    public Query byYear(int year) {
        return by(book -> book.getYear() == year);
    }

    public Query byAvailability(boolean isAvailable) {
        return by(book -> isAvailable == book.isAvailable());
    }

    public List<Book> execute() {
        return queryStream.collect(Collectors.toList());
    }
}
