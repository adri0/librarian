package com.adri0.librarian.core;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

public class LendingTest {

    private Book book;
    private Lending lending;

    @BeforeEach
    public void instantiateQuery() {
        book = new Book(1, new Title("The Odyssey", "Homer", 1998));
        lending = new Lending(book, new User("Cecil"));
        book.setLending(lending);
    }

    @Test
    public void lentAt_is_populate_on_creation() throws InterruptedException {
        MILLISECONDS.sleep(1);
        assertTrue(lending.getLentAt().isBefore(LocalDateTime.now()));
    }

    @Test
    public void returnedAt_is_null_if_not_returned() {
        assertNull(lending.getReturnedAt());
    }

    @Test
    public void returnedAt_is_populated_when_lending_finished() throws InterruptedException {
        LocalDateTime testStart = LocalDateTime.now();
        MILLISECONDS.sleep(1);
        lending.finish();
        assertNotNull(lending.getReturnedAt());
        assertTrue(lending.getReturnedAt().isAfter(testStart));
    }

    @Test
    public void book_is_made_available_when_lending_finished() {
        assertFalse(book.isAvailable());
        lending.finish();
        assertTrue(book.isAvailable());
    }
}
