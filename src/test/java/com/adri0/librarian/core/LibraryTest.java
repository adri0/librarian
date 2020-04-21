package com.adri0.librarian.core;

import com.adri0.librarian.sampledata.Top58Titles;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;


public class LibraryTest {

    private Library libraryWith10Books;
    private User alicja;
    private User bogdan;

    @BeforeEach
    public void setUp() {
        libraryWith10Books = new Library();
        Top58Titles.generateSamples(10, libraryWith10Books::addBook);
        alicja = new User("Alicja");
        bogdan = new User("Bogdan");
    }

    @Test
    public void ids_are_generated_for_added_books_start_from_1() {
        Library library = new Library();
        List<Book> books = new ArrayList<>();
        Top58Titles.generateSamples(2, (title, author, year) -> {
            Book book = library.addBook(title, author, year);
            books.add(book);
        });
        assertEquals(1, books.get(0).getId());
        assertEquals(2, books.get(1).getId());
    }

    @Test
    public void lending_a_book_makes_it_unavailable() {
        int bookId = 1;
        Book book = libraryWith10Books.getBook(bookId);
        assertTrue(book.isAvailable());
        libraryWith10Books.lendBook(bookId, alicja);
        assertFalse(book.isAvailable());
    }

    @Test
    public void lending_a_book_generates_a_lending_object() throws InterruptedException {
        int bookId = 1;
        Lending lending = libraryWith10Books.lendBook(bookId, alicja);
        assertEquals(libraryWith10Books.getBook(bookId), lending.getBook());
        assertEquals(alicja, lending.getUser());
        MILLISECONDS.sleep(1);
        assertTrue(lending.getLentAt().isBefore(LocalDateTime.now()));
        assertEquals(1, libraryWith10Books.getCurrentLendings().size());
        assertEquals(lending, libraryWith10Books.getCurrentLendings().get(0));
    }

    @Test
    public void cannot_lend_unavailable_book() {
        int bookId = 1;
        libraryWith10Books.lendBook(bookId, alicja);
        Assertions.assertThrows(IllegalStateException.class, () ->
            libraryWith10Books.lendBook(bookId, alicja));
    }

    @Test
    public void cannot_remove_unavailable_book() {
        int bookId = 1;
        libraryWith10Books.lendBook(bookId, alicja);
        Assertions.assertThrows(IllegalStateException.class, () ->
            libraryWith10Books.removeBook(bookId));
    }

    @Test
    public void can_remove_available_book() {
        int bookId = 1;
        assertTrue(libraryWith10Books.getBook(bookId).isAvailable());
        assertEquals(10, libraryWith10Books.totalBooks());
        libraryWith10Books.removeBook(bookId);
        assertNull(libraryWith10Books.getBook(bookId));
        assertEquals(9, libraryWith10Books.totalBooks());
    }

    @Test
    public void cannot_lend_nonexistent_book() {
        int nonExistentBookId = 11;
        Assertions.assertThrows(NoSuchElementException.class, () ->
            libraryWith10Books.lendBook(nonExistentBookId, alicja));
    }

    @Test
    public void returned_book_makes_it_available_for_lending() {
        int bookId = 1;
        Lending lending = libraryWith10Books.lendBook(bookId, alicja);
        Book book = lending.getBook();
        assertFalse(book.isAvailable());
        libraryWith10Books.returnBook(bookId);
        assertTrue(book.isAvailable());
        libraryWith10Books.lendBook(bookId, bogdan);
        assertFalse(book.isAvailable());
    }

    @Test
    public void books_with_same_title_can_be_added_more_than_once() {
        int bookId = 1;
        Book book1 = libraryWith10Books.getBook(bookId);
        Book book2 = libraryWith10Books.addBook(
                book1.getTitle(), book1.getAuthor(), book1.getYear());
        assertEquals(book1.getTitleObj(), book2.getTitleObj());
        assertNotEquals(book1.getId(), book2.getId());
    }

    @Test
    public void full_search_returns_all_books() {
        assertEquals(
                libraryWith10Books.totalBooks(),
                libraryWith10Books.search().execute().size()
        );
    }

    @Test
    public void listAll_gets_correct_overview() {
        Library library = new Library();

        // Add first 4 titles
        Top58Titles.generateSamples(4, library::addBook);

        // Add 2 copies of each of the first 2 titles
        Top58Titles.generateSamples(2, library::addBook);
        Top58Titles.generateSamples(2, library::addBook);

        // Alicja lends a copy of 1st and 2nd titles
        library.lendBook(1, alicja);
        library.lendBook(2, alicja);

        // Bogdan lends another copy of 2nd title (id=6) and one of the 3rd title.
        library.lendBook(6, bogdan);
        library.lendBook(3, bogdan);

        List<TitleOverview> allOverviews = library.listAllTitles();

        assertEquals(4, allOverviews.size());

        // assert expected total copies, expected available copies, number of lendings
        assertTitleOverviewMatches(
                getOverviewByTitle(allOverviews, library.getBook(1).getTitleObj()),
                3, 2, 1);
        assertTitleOverviewMatches(
                getOverviewByTitle(allOverviews, library.getBook(2).getTitleObj()),
                3, 1, 2);
        assertTitleOverviewMatches(
                getOverviewByTitle(allOverviews, library.getBook(3).getTitleObj()),
                1, 0, 1);
        assertTitleOverviewMatches(
                getOverviewByTitle(allOverviews, library.getBook(4).getTitleObj()),
                1, 1, 0);
    }

    private static TitleOverview getOverviewByTitle(
            List<TitleOverview> allOverviews, Title title) {
        for (TitleOverview overview : allOverviews) {
            if (overview.getTitle().equals(title)) return overview;
        }
        return null;
    }

    private static void assertTitleOverviewMatches(TitleOverview titleOverview,
            int expectedTotalCopies, int expectedAvailableCopies, int expectedLendings) {

        assertEquals(expectedTotalCopies, titleOverview.totalCopies(),
                "total copies doesn't match expected. overview: " + titleOverview);
        assertEquals(expectedAvailableCopies, titleOverview.availableCopies(),
                "available copies doesn't match expected. overview: " + titleOverview);
        assertEquals(expectedLendings, titleOverview.getCurrentLendings().size(),
                "number of lendings doesn't match expected. overview: " + titleOverview);
    }
}
