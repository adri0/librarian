package com.adri0.librarian;

import com.adri0.librarian.core.*;
import com.adri0.librarian.sampledata.Top58Titles;

import java.util.Collection;
import java.util.List;

public class SampleScenario {
    public static void main(String[] args) {
        print("Welcome to Librarian!");
        print("---------------------");
        print("This scenario shows an example on how this package can be used to manage a library " +
                "lending system.");
        print("We are going to use examples found at com.adri0.librarian.sampledata.Top58Titles to " +
                "populate the library.\n");

        print("Let's first create a Library instance and add the first two books by using " +
                "the addBook method.\n");

        print("> Library library = new Library();");
        print("> Top58Titles.generateSamples(2, library::addBook);");

        Library library = new Library();
        Top58Titles.generateSamples(2, library::addBook);

        print("\nNow we can use the listAll method to show information regarding all titles included.\n");

        print("> library.listAllTitles();");
        printAll(library.listAllTitles());


        print("\nNow we're going to add the first 5 books of the list. Note that the first 2 are going" +
                "to be added again. The library will show now more copies of those repeated titles.\n");

        print("> Top58Titles.generateSamples(5, library::addBook);");
        print("> library.listAllTitles();");

        Top58Titles.generateSamples(5, library::addBook);
        printAll(library.listAllTitles());


        print("\nAlicja will lend the book with id 1 from the library. To accomplish that we use the method " +
                "lendBook. It returns a Lending object, which shows some information regarding the lending.\n");

        print("> User alicja = new User(\"Alicja\");");
        print("> Lending lending = library.lendBook(1, alicja);");

        User alicja = new User("Alicja");
        Lending lending = library.lendBook(1, alicja);
        print(lending);


        print("\nMoreover, if we print the Book object, we can also be informed that it has been lent " +
                "to Alicja.\n");

        print("> lending.getBook();");
        print(lending.getBook());

        print("\nSuppose that Bogdan wants to check the same title above and, if available, " +
                "he would like to rent it out. We can use the method getTitleOverview to show more information " +
                "about it.\n");

        print("> Title title = library.getBook(1).getTitle();");
        print("> TitleOverview titleOverview = library.getTitleOverview(title);");

        Title title = library.getBook(1).getTitleObj();
        TitleOverview titleOverview = library.getTitleOverview(title);
        print(titleOverview);

        print("\nFrom that he learns that 2 copies of the book exist in the library, one of which " +
                "is with Alicja, but the other (boojId=3) is still available. He decides to lend it. " +
                "Then later, if we check out the title overview again, we see that no copies from " +
                "that title are available.\n");

        print("> User bogdan = new User(\"Bogdan\");");
        print("> library.lendBook(3, bogdan);");
        print("> library.getTitleOverview(title));");

        User bogdan = new User("Bogdan");
        library.lendBook(3, bogdan);
        print(library.getTitleOverview(title));

        print("\nOnce Alicja finishes her reading she returns the book to the library. In order to " +
                "account for the return we can use the returnBook method. And that makes it " +
                "available again for future lendings.\n");

        print("> library.returnBook(1);");
        print("> library.getTitleOverview(title);");

        library.returnBook(1);
        print(library.getTitleOverview(title));

        print("\nAlicja might want to lend another book from the same author, but doesn't know " +
                "which. So we might let her search through the library for other titles from " +
                "that author by using the search method. The search method returns an instance " +
                "of the object Query, which can append filter clauses byAuthor, byTitle or byYear.\n");

        print("> library.search().byAuthor(\"austen\").execute();");

        List<Book> result = library.search()
                .byAuthor("austen")
                .execute();
        printAll(result);

        print("\nOr maybe we would like to know the books from that author that were published after " +
                "a certain year.\n");

        print("> library.search()");
        print(">        .byAuthor(\"austen\")");
        print(">        .by(book -> book.getYear() > 1990);");
        print(">        .execute();");

        result = library.search()
                .byAuthor("austen")
                .by(book -> book.getYear() > 1990)
                .execute();

        printAll(result);

        print("\nThis is an overview of what can be done with the librarian package. For more use cases, " +
                "constraints and business rules check the test cases in the LibraryTest class.\n");
    }

    private static void print(Object message) {
        System.out.println(message);
    }

    private static <T> void printAll(Collection<T> collection) {
        collection.forEach(SampleScenario::print);
    }
}
