isbndb
======

Java Library for interacting with the isbndb REST endpoints. There is a default 1 second interval betweeen calls, which is enforced as a minimum.

* Download : [http://www.scholnick.net/isbndb/isbndb.jar](http://www.scholnick.net/isbndb/isbndb-1.0.1.jar)
* JavaDoc  : <http://www.scholnick.net/isbndb/api-doc>
* License  : <http://www.scholnick.net/isbndb/license.txt>

isbndb library is dependent on the following libraries:

* [Jackson Java JSON-processor](http://jackson.codehaus.org/)
* [Commons Lang](http://commons.apache.org/proper/commons-lang/)
* [log4j](http://logging.apache.org/log4j/1.2/)

```java

import java.util.List;
import net.scholnick.isbndb.BooksProxy;
import net.scholnick.isbndb.domain.Author;
import net.scholnick.isbndb.domain.Book;


// all books with virus in the title
List<Book> books = BooksProxy.getInstance().getBooks("virus");

// exact book by ISBN
Book book = BooksProxy.getInstance().getBookByISBN("9780684853505");

// Title with spaces (BooksProxy automatically handles the spaces)
List<Book> books = BooksProxy.getInstance().getBooks("bag of bones");

```

net.scholnick.isbndb.BooksProxy **must** know your isbn developer API key. Two methods:

```java
BooksProxy.getInstance().setDeveloperKey("YOUR_KEY"); // before any getBooks() calls
```

Create isbndb.properties and store it in your classpath

```java
developer.key=YOUR_KEY
```

