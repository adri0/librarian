package com.adri0.librarian.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    private Book book;
    private Lending lending;

    @BeforeEach
    public void instantiateQuery() {
        book = new Book(1, new Title("The Odyssey", "Homer", 1998));
        lending = new Lending(book, new User("Cecil"));
    }

    @Test
    public void book_return_title_information() {
        assertEquals("The Odyssey", book.getTitle());
        assertEquals("Homer", book.getAuthor());
        assertEquals(1998, book.getYear());
    }

    @Test
    public void book_is_available_when_lending_is_null() {
        assertTrue(book.isAvailable());
        book.setLending(lending);
        assertFalse(book.isAvailable());
    }

    @Test
    public void cannot_return_book_without_lending() {
        Assertions.assertThrows(IllegalStateException.class, () ->
                book.returnBook());
    }

    @Test
    public void returned_book_becomes_available() {
        book.setLending(lending);
        assertFalse(book.isAvailable());
        book.returnBook();
        assertTrue(book.isAvailable());
    }
}
