package com.adri0.librarian.core;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Represents an overview of a book title in a given moment.
 * An overview consists of:
 * - total number of copies of the title
 * - number of available copies of the title
 * - collection of lendings of copies of that title
 * - set of copies of that title
 */
public class TitleOverview {

    private final Title title;
    private final Collection<Book> books;
    private final Collection<Lending> currentLendings;

    TitleOverview(Title title, Collection<Book> books, Collection<Lending> currentLendings) {
        this.title = title;
        this.books = books != null ? books : Collections.emptySet();
        this.currentLendings = currentLendings != null ? currentLendings : Collections.emptySet();
    }

    public int totalCopies() {
        return books.size();
    }

    public int availableCopies() {
        return totalCopies() - currentLendings.size();
    }

    public Title getTitle() {
        return title;
    }

    public Collection<Book> getBooks() {
        return books;
    }

    public Collection<Book> getAvailableBooks() {
        return books.stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }

    public Collection<Lending> getCurrentLendings() {
        return currentLendings;
    }

    @Override
    public String toString() {
        String lendings = currentLendings.stream()
                .map(lending -> String.format("{bookId=%s -> %s}",
                        lending.getBook().getId(), lending.getUser().getName()))
                .collect(Collectors.joining(", "));
        String availableCopies = books.stream()
                .filter(Book::isAvailable)
                .map(book -> String.valueOf(book.getId()))
                .collect(Collectors.joining(", "));
        return "TitleOverview{\n\t" +
                title +
                ",\n\tnumberOfCopies=" + totalCopies() +
                ",\n\tavailableCopies(bookId)=" + (!availableCopies.isEmpty() ? "[" + availableCopies + "]" : "none") +
                ",\n\tlendings=" + (!lendings.isEmpty() ? "[" + lendings + "]" : "none") +
                "\n}";
    }
}
