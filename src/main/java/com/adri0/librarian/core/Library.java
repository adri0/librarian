package com.adri0.librarian.core;

import java.util.*;
import java.util.stream.Collectors;


public class Library {

    /**
     * All added books are stored in this map.
     * It maps the bookId to a Book instance.
     */
    private final Map<Integer, Book> allBooks;

    /**
     * All books lent are inside this map.
     * It maps the bookId to its respective lending object.
     */
    private final Map<Integer, Lending> currentLendings;

    /**
     * Catalog of all unique titles in the library
     */
    private final Set<Title> catalog;

    /**
     * History of all lending performed. It maintains
     * both the active and past lendings.
     */
    private final List<Lending> lendingHistory;

    /**
     * Stores the last assigned bookId, so it is known
     * which should be the next bookId;
     */
    private int lastBookId;

    public Library() {
        this.allBooks = new HashMap<>();
        this.catalog = new HashSet<>();
        this.currentLendings = new HashMap<>();
        this.lendingHistory = new LinkedList<>();
        this.lastBookId = 0;
    }

    /**
     * Add a new book to the library.
     * Given a triplet {title, author, year}, generates a unique
     * bookId, creates a new Book object with the respective title
     * and add it to the allBooks map.
     * If it's a new book title, also adds it to the catalog.
     */
    public Book addBook(String title, String author, int year) {
        Title bookTitle = getTitle(title, author, year);
        int id = generateId();
        Book book = new Book(id, bookTitle);
        allBooks.put(id, book);
        return book;
    }

    /**
     * Remove a book given its bookId.
     */
    public void removeBook(int bookId) {
        assertBookExists(bookId);
        assertBookNotLent(bookId, "cannot remove a book that has been lent");
        allBooks.remove(bookId);
    }

    /**
     * Get a book instance from the library given its bookId.
     */
    public Book getBook(int bookId) {
        return allBooks.get(bookId);
    }

    /**
     * Check whether a book with the provided id exists in the library.
     */
    public boolean containsBook(int bookId) {
        return allBooks.get(bookId) == null;
    }

    /**
     * Returns the number of books in the library.
     */
    public int totalBooks() {
        return allBooks.size();
    }

    public List<Lending> getCurrentLendings() {
        return Arrays.asList(currentLendings.values().toArray(new Lending[] {}));
    }

    /**
     * Lends a book by providing its bookId and a user.
     * If the book doesn't exist it will throw an NoSuchElementException.
     * If the book has been lent, throws an IllegalStateException.
     * Returns a Lending instance providing details of the lending.
     * Adds the Lending instance to the currentLendings map.
     */
    public Lending lendBook(int bookId, User user) {
        assertBookExists(bookId);
        assertBookNotLent(bookId, "cannot lend a book that has been lent");
        Book book = allBooks.get(bookId);
        Lending lending = new Lending(book, user);
        book.setLending(lending);
        currentLendings.put(bookId, lending);
        lendingHistory.add(lending);
        return lending;
    }

    /**
     * Returns a book that has been lent.
     * Its respective Lending object is finished
     * and removed from currentLendings map.
     */
    public void returnBook(int bookId) {
        assertBookExists(bookId);
        assertBookLent(bookId, "cannot return a book that hasn't been lent");
        Lending lending = currentLendings.get(bookId);
        lending.finish();
        currentLendings.remove(bookId);
    }

    /**
     * Given a book title check it's record in the library.
     * The TitleOverview show how many copies of given title
     * exist in the library and how many of those are lent.
     */
    public TitleOverview getTitleOverview(Title title) {
        return new TitleOverview(
                title,
                allBooks.values().stream()
                        .filter(book -> book.getTitleObj().equals(title))
                        .collect(Collectors.toList()),
                currentLendings.values().stream()
                        .filter(lending -> lending.getBookTitle().equals(title))
                        .collect(Collectors.toList()));
    }

    /**
     * Returns a collection containing TitleOverview of
     * all titles available in the library.
     */
    public List<TitleOverview> listAllTitles() {
        Map<Title, List<Book>> booksByTitle = allBooks.values().stream()
                .collect(Collectors.groupingBy(Book::getTitleObj));
        Map<Title, List<Lending>> lendingsByTitle = currentLendings.values().stream()
                .collect(Collectors.groupingBy(Lending::getBookTitle));
        return catalog.stream()
                .filter(title -> booksByTitle.size() > 0)
                .map(title -> new TitleOverview(title, booksByTitle.get(title), lendingsByTitle.get(title)))
                .collect(Collectors.toList());
    }

    /**
     * Initiates a query in the library.
     * The method returns a Query object.
     * @See com.adri0.librarian.core.Query
     * On for how to apply filters.
     * The method Query::execute must be called.
     */
    public Query search() {
        return new Query(this.allBooks.values());
    }

    private void assertBookExists(int bookId) {
        if (!allBooks.containsKey(bookId)) {
            throw new NoSuchElementException("book with id " + bookId + " doesn't exist in the library");
        }
    }

    private void assertBookLent(int bookId, String message) {
        if (!currentLendings.containsKey(bookId)) {
            throw new IllegalStateException(message + ": " + bookId);
        }
    }

    private void assertBookNotLent(int bookId, String message) {
        if (currentLendings.containsKey(bookId)) {
            throw new IllegalStateException(message + ": " + bookId);
        }
    }

    private int generateId() {
        return ++lastBookId;
    }

    private Title getTitle(String title, String author, int year) {
        Title bookTitle = new Title(title, author, year);
        catalog.add(bookTitle);
        return bookTitle;
    }
}
