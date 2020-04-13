package com.adri0.librarian.core;

/**
 * Book represents a unique book copy in the library.
 * A Book is composed by an id (integer) and an
 * instance of a Title object.
 * Book doesn't have public constructors since it ca
 * only be instantiated by the `Library`.
 */
public class Book {

    private final int id;
    private final Title title;
    private Lending lending;

    Book(int id, Title title) {
        this.id = id;
        this.title = title;
        this.lending = null;
    }

    public Title getTitleObj() {
        return title;
    }

    public int getId() {
        return id;
    }

    public boolean isAvailable() {
        return lending == null;
    }

    public String getAuthor() {
        return title.getAuthor();
    }

    public String getTitle() {
        return title.getTitle();
    }

    public int getYear() {
        return title.getYear();
    }

    void setLending(Lending lending) {
        this.lending = lending;
    }

    void returnBook() {
        if (lending == null) {
            throw new IllegalStateException("cannot return book with has been lent");
        }
        this.lending = null;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", " + title +
                ", lending=" + (lending == null ? "no" : lending.getUser()) +
                '}';
    }
}
