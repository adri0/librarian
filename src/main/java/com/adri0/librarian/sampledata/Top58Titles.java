package com.adri0.librarian.sampledata;

import java.util.stream.IntStream;

/**
 * 58 titles from the list of the 100 most download
 * titles at the Project Gutenberg for the month of March 2020.
 * See https://www.gutenberg.org/
 */
public class Top58Titles {
    public static final String[][] top58 = {
            // Title, Author, Year
            {"Pride and Prejudice", "Jane Austen", "1983"},
            {"The Adventures of Tom Sawyer", "Mark Twain", "1989"},
            {"Emma", "Jane Austen", "1994"},
            {"The Metamorphosis", "Franz Kafka", "1915"},
            {"Siddhartha", "Hermann Hesse", "1981"},
            {"The Prophet", "Kahlil Gibran", "1973"},
            {"Sense and Sensibility", "Jane Austen", "1995"},
            {"The Scarlet Letter", "NATHANIEL HAWTHORNE", "1965"},
            {"Wuthering Heights", "EMILY BRONTE", "1983"},
            {"Adventures of Huckleberry Finn", "Mark Twain", "1992"},
            {"The Time Machine", "H. G. Wells", "1995"},
            {"Dracula", "Bram Stoker", "1994"},
            {"The Hound of the Baskervilles", "Sir Arthur Conan Doyle", "1993"},
            {"The Secret Garden", "Frances Hodgson Burnett", "1989"},
            {"The Jungle Book", "G. C. Barrett", "1994"},
            {"Little Women", "Louisa M. Atcott", "1995"},
            {"The Picture of Dorian Gray", "Oscar Wilde", "2003"},
            {"War and Peace", "Leo Tolstoy", "1869"},
            {"Hard Times", "Charles Dickens", "1998"},
            {"Anthem", "Ayn Rand", "1996"},
            {"Dubliners", "James Joyce", "1990"},
            {"Great Expectations", "Charles Dickens", "2000"},
            {"Don Quixote", "Miguel De Cervantes Saavedra", "1997"},
            {"The Mysterious Affair at Styles", "Agatha Christie", "2004"},
            {"Anna Karenina", "Leo Tolstoy", "1984"},
            {"The Yellow Wallpaper", "Charlotte Perkins Gilman", "1996"},
            {"Anne of Green Gables", "L.M. Montgomery", "1993"},
            {"The Complete Works of William Shakespeare", "William Shakespeare", "1992"},
            {"The Jungle", "Sanjay Sonawani", "2001"},
            {"A Tale of Two Cities", "Charles Dickens", "1997"},
            {"Treasure Island", "Robert Louis Stevenson", "1994"},
            {"Uncle Tom's Cabin", "Harriet Beecher Stowe", "1997"},
            {"Alice's Adventures in Wonderland", "Lewis Carroll", "1999"},
            {"The Wonderful Wizard of Oz", "Grace Frank Baum", "1991"},
            {"The Tale of Peter Rabbit", "Beatrix Potter", "2000"},
            {"The Devil's Dictionary", "Ambrose Bierce", "1998"},
            {"Peter Pan", "Mouse Works", "1997"},
            {"Candide", "Voltaire", "2002"},
            {"The Odyssey", "Homer", "1998"},
            {"The War of the Worlds", "H. G. Wells", "1993"},
            {"The Call of the Wild", "Jack London", "1986"},
            {"The Turn of the Screw", "Henry James", "1993"},
            {"The Adventures of Sherlock Holmes", "Arthur Conan, Sir Doyle", "1994"},
            {"The Iliad", "Homer", "1990"},
            {"The Brothers Karamazov", "FYODOR DOSTOEVSKY", "1984"},
            {"Oliver Twist", "Charles Dickens", "1993"},
            {"Heart of Darkness", "Joseph Conrad", "2002"},
            {"Ulysses", "James Joyce", "1997"},
            {"The Souls of Black Folk", "William Edward Burghardt Dubois", "1987"},
            {"Leviathan", "John Gordon Davis", "1976"},
            {"David Copperfield", "Charles Dickens", "1979"},
            {"Tractatus Logico-Philosophicus", "Ludwig Wittgenstein", "1981"},
            {"The Sign of the Four", "John Milne", "1995"},
            {"Pygmalion", "George Bernard Shaw", "2000"},
            {"Narrative of the Life of Frederick Douglass, an American Slave", "FREDERICK DOUGLASS", "1963"},
            {"Second Treatise of Government", "John Locke", "1980"},
            {"The Strange Case of Dr. Jekyll and Mr. Hyde", "Robert Louis Stevenson", "1993"},
            {"Grimms' Fairy Tales", "Jacob W. Grimm", "1971"}
    };

    public interface BookTitleParameters {
        void provide(String title, String author, int year);
    }

    public static void generateSamples(int nSamples, BookTitleParameters bookTitleParameters) {
        IntStream.range(0, nSamples).forEach(i -> {
            String title = top58[i][0];
            String author = top58[i][1];
            int year = Integer.parseInt(top58[i][2]);
            bookTitleParameters.provide(title, author, year);
        });
    }
}
