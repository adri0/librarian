package com.adri0.librarian.core;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

/**
 * Represents all information of a lending process.
 * Think of it as a lending receipt, or a lending
 * record. It shows which copy of a book is being lend by whom,
 * when it has been lend and, if returned, when.
 *
 * returnedAt is only set when the `finish` method has been called.
 * The `finish` method is not public, so it can only be called
 * by the `Library`.
 */
public class Lending {

    private final Book book;
    private final User user;
    private final LocalDateTime lentAt;
    private LocalDateTime returnedAt;

    Lending(Book book, User user) {
        this.book = book;
        this.user = user;
        this.lentAt = LocalDateTime.now();
    }

    public Book getBook() {
        return book;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getLentAt() {
        return lentAt;
    }

    public Title getBookTitle() {
        return book.getTitleObj();
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }

    void finish() {
        returnedAt = LocalDateTime.now();
        book.returnBook();
    }

    @Override
    public String toString() {
        return "Lending{" +
                "\n\tbookId=" + book.getId() +
                ",\n\t" + book.getTitleObj() +
                ",\n\tuser=" + user +
                ",\n\tlentAt=" + ISO_DATE_TIME.format(lentAt) +
                ",\n\treturnedAt=" + (returnedAt != null ? ISO_DATE_TIME.format(returnedAt) : "null") +
                "\n}";
    }
}
