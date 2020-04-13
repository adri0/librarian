package com.adri0.librarian.core;

public class Title {

    private final String title;
    private final String author;
    private final int year;

    public Title(String title, String author, int year) {
        if (title == null || author == null) {
            throw new IllegalArgumentException("title and author must not be null");
        }
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Title)) return false;
        Title other = (Title) o;
        return title.equals(other.title)
                && author.equals(other.author)
                && year == other.year;
    }

    @Override
    public int hashCode() {
        int prime = 43;
        return prime * author.hashCode()
                + prime * title.hashCode()
                + prime * year;
    }

    @Override
    public String toString() {
        return String.format("Title{'%s', by %s, %s}", title, author, year);
    }
}
