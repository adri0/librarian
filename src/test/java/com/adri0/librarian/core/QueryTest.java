package com.adri0.librarian.core;

import com.adri0.librarian.sampledata.Top58Titles;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class QueryTest {

    private static Set<Book> bookSet;
    private Query query;

    @BeforeAll
    public static void setUp() {
        bookSet = new HashSet<>();
        final Integer[] idHolder = {0};
        Top58Titles.generateSamples(58, ((title, author, year) -> {
            bookSet.add(new Book(idHolder[0]++, new Title(title, author, year)));
        }));
    }

    @BeforeEach
    public void instantiateQuery() {
        query = new Query(bookSet);
    }

    @Test
    public void search_author_by_substring() {
        List<Book> result = query.byAuthor("Dickens").execute();
        assertEquals(5, result.size());
        result.forEach(book -> assertTrue(book.getAuthor().contains("Dickens")));
    }

    @Test
    public void author_search_is_case_insensitive() {
        Arrays.asList("dickens", "DICKENS").forEach((authorFragment) -> {
            List<Book> result = new Query(bookSet)
                    .byAuthor(authorFragment)
                    .execute();
            assertEquals(5, result.size());
            result.forEach(book -> assertTrue(book.getAuthor().contains("Dickens")));
        });
    }

    @Test
    public void search_title_by_substring() {
        List<Book> result = query.byTitle("war").execute();
        assertEquals(2, result.size());
        result.forEach(book -> assertTrue(book.getTitle().contains("War")));
    }

    @Test
    public void title_search_is_case_insensitive() {
        Arrays.asList("WAR", "war").forEach((titleFragment) -> {
            List<Book> result = new Query(bookSet)
                    .byTitle(titleFragment)
                    .execute();
            assertEquals(2, result.size());
            result.forEach(book -> assertTrue(book.getTitle().contains("War")));
        });
    }

    @Test
    public void not_found_results_empty_collection() {
        assertEquals(0, new Query(Collections.emptyList()).byAuthor("Dickens").execute().size());
        assertEquals(0, query.byAuthor("Machado de Assis").execute().size());
    }

    @Test
    public void can_compound_author_title_year_clauses() {
        List<Book> result = query
                .byAuthor("dickens")
                .byTitle("tale")
                .execute();
        assertEquals(1, result.size());
        assertEquals("A Tale of Two Cities", result.get(0).getTitle());
        assertEquals("Charles Dickens", result.get(0).getAuthor());

        result = new Query(bookSet)
                .byTitle("tale")
                .byYear(1971)
                .execute();
        assertEquals(1, result.size());
        assertEquals("Grimms' Fairy Tales", result.get(0).getTitle());
        assertEquals(1971, result.get(0).getYear());
    }

    @Test
    public void can_query_by_an_arbitrary_predicate() {
        List<Book> result = query
                .by(book -> book.getYear() < 1950)
                .execute();
        assertEquals(2, result.size());
    }
}
