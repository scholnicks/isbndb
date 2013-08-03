isbndb
======

Library for interacting with the isbndb REST endpoints. Each call to the REST APU will be spaced by 1 second.

Download from : http://www.scholnick.net/isbndb-1.0.0.jar

isbndb library is dependent on the following libraries:

* [Jackson](http://jackson.codehaus.org/)
* [Commons Lang](http://commons.apache.org/proper/commons-lang/)
* [log4j](http://logging.apache.org/log4j/1.2/)

```java

import net.scholnick.isbndb.BooksProxy;
import net.scholnick.isbndb.domain.Author;
import net.scholnick.isbndb.domain.Book;

// all books with virus in the title
List(Book) books = BooksProxy.getInstance().getBooks("virus");

// exact book by ISBN
Book book = BooksProxy.getInstance().getBookByISBN("9780684853505");

// Title Search
List(Book) books = BooksProxy.getInstance().getBooks("bag of bones");

```

net.scholnick.isbndb.BooksProxy **must** know your isbn developer API key. Two methods:

```java
BooksProxy.getInstance().setDeveloperKey("YOUR_KEY"); // before any getBooks() calls
```

Create isbndb.properties and store in your classpath

```java
developer.key=YOUR_KEY
```

