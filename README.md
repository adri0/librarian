# librarian

This is a Java 8 module that can be used to manage a library with a lending system. It provides a set of
 classes and methods that can be used by a client application. 
 
## Requirements

- Java 8+
- Maven

## Building

To build the module and run all its tests just clone this repository on your local file system, change to
 the librarian directory and run `package` task from Maven.

```
> mvn package
```

Run the sample usage scenario by running the generated jar:

```
> java -jar target/librarian-0.1.jar
```

To run only the tests use the `test` task from Maven:

```
> mvn test
```

## Quick start

Import all classes from the core package class into your application and create an instance of `Library`.

```java
import com.adri0.librarian.core.*;
```

```java
Library library = new Library();
```

Start adding books by using the `addBook` method, providing a title, 
author and year of publication. Each book added returns a Book object that contains an id that represents uniquely that
book in the library.

```java
Book book = library.addBook("Anna Karenina", "Leo Tolstoy", 1984);
int bookId = book.getId();
```

You can use the `bookId` to lend books to a user. A user can be represented by the `User` class, that 
simply accepts a user name as parameter. Use the `Library.lendBook(int bookId, User user)` method to lend 
a book to that user.

```java
User user = new User("Cecil");
Lending lending = library.lendBook(bookId, user);
```

This method `lendBook` returns a `Lending` object with details from the lending of that book.

```
Lending{
  bookId=1,
  Title{'Anna Karenina', by Leo Tolstoy, 1984},
  user=Cecil,
  lentAt=2019-12-09T16:57:04.641,
  returnedAt=null
}
```

To return a book to the library use `Library.returnBook(int bookId)` method:

```java
library.returnBook(bookId);
```

### Main public classes and methods

#### Library

The `Library` class is the main interface of with the package. It provides methods for 
lending and returning, listing all the books in the library and keeping a track
of the titles in the catalog.

The main methods are the following:

- `addBook`: Adds a book to the library. Generates a unique ID and return `Book` object 
  with information about the book. It also adds unique book titles to the catalog.
- `removeBook`: Remove a book from the library. It is not possible to remove books that
 are currently lent to an user. Doing so will raise an `IllegalStateException`.
- `getBook`: Returns a `Book` object of a previously added book.
- `getCurrentLendings`: Returns a list of all active lendings in the library. It returns
 a list of `Lending` objects.
- `lendBook`: Lends a book to an user and returns a `Lending` instance. It is not possible 
 to lend a book that is being currently lent. Doing so will raise an `IllegalStateException`.
- `listAllTitles`: Returns a list of overviews of all unique book titles in the library. 
 The return type is a `List<TitleOverview>`. 
- `search`: Starts a search query over all books in the library. The method returns 
 a `Query` object that builds a search query. It is possible to chain clauses 
 with the methods such as `byAuthor(String author)`, `byTitle(String title)`, etc. 
 
#### Book

`Book` represents a unique book copy in the library. A `Book` is composed by an
id (integer) and an instance of a `Title` object. `Book` doesn't have public constructors
since it can only be instantiated by the `Library`. The `Title` represents a book title 
that can be shared by many books.

Besides of getters for a book's id, title, author and year, we have the following public 
method:

- `isAvaliable`: Tells whether a book is available for lending.

#### Title

Represents unique book titles in the library. A book title is composed by a book's
title, author and year of publication. 

#### Lending

Represents all information of a lending process. Think of if as a lending receipt or lending
record. It shows which copy of a book is being lend. Besides getter for book and user we have:  

- `getLentAt`: Returns a timestamp of when the book has been lend.
- `getReturnedAt`: Returns a timestamp of when the book has been returned. It will be 
 `null` if it hasn't been yet returned.
 
#### TitleOverview

A `TitleOverview` are generated when `Library.listAllTitles` is called. It shows
for a given `Title` how many copies of the title exist in the library, from those
which are being lend to whom, and which are available for lending. It's worth noting
that `TitleOverview` objects are snapshots of the overview of a title at the time
the instance has been generated.

- `totalCopies`: Number of total copies of a given title.
- `availableCopies`: Number of copies of the given title that are available for lending.
- `getAvailableBooks`: List of the available `Book` copies of the given title.
- `getCurrentLendings`: List of the active `Lending`s for the title. 

#### Query

A query object is instantiated when`Library.search()` method is used. The actual
search performs only when the method `execute` is called. It has the following
chainable clauses methods:

- `byAuthor`: Adds a clause that will filter authors with a given parameter. It is 
 sufficient that the parameter matches with a substring of an author. So a user can 
 query by only its first name or last name.
- `byTitle`: Adds a title filter clause. Analogous to author, it is case sensitive 
 and searches for substrings.
- `byYear`: Only match books of a given year.
- `byAvailability`: Only matches book that are available, or not available, depending
 on the boolean parameter (`true` for available).
- `by`: Accepts a `Predicate` function as parameter that will match whatever book 
 that the predicate returns `true`.
 
 
### Limitations and further improvements

- Currently, the module only supports in-memory storage. All the information is lost
 when the application process finishes.
- Queries performed by `search` loop over all titles in the library. It is ok if the library
 size is small, but it can get slow if we add hundreds of thousands of books. In the
 latter case, a way to improve search speed is to implement text-based indexes to 
 limit search space of titles and authors, and number based index for year.
