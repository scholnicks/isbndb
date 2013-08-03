isbndb
======

Java Library for interacting with the isbndb REST endpoints. There is a default 1 second interval betweeen calls, which is enforced as a minimum.

* Download from : [hhttp://www.scholnick.net/isbndb/isbndb.jar](http://www.scholnick.net/isbndb/isbndb-1.0.0.jar)
* JavaDoc : <http://www.scholnick.net/isbndb/api-doc>
* License : <http://www.scholnick.net/isbndb/license.txt>

isbndb library is dependent on the following libraries:

* [Jackson](http://jackson.codehaus.org/)
* [Commons Lang](http://commons.apache.org/proper/commons-lang/)
* [log4j](http://logging.apache.org/log4j/1.2/)

```java
// all books with virus in the title
List<Book> books = BooksProxy.getInstance().getBooks("virus");

// exact book by ISBN
Book book = BooksProxy.getInstance().getBookByISBN("9780684853505");

// Title Search
List<Book> books = BooksProxy.getInstance().getBooks("bag of bones");

```

net.scholnick.isbndb.BooksProxy **must** know your isbn developer API key. Two methods:

```java
BooksProxy.getInstance().setDeveloperKey("YOUR_KEY"); // before any getBooks() calls
```

Create isbndb.properties and store in your classpath

```java
developer.key=YOUR_KEY
```

