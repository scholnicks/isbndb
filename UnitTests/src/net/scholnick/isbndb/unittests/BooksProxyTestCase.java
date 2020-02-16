package net.scholnick.isbndb.unittests;

import static junit.framework.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import net.scholnick.isbndb.BooksProxy;
import net.scholnick.isbndb.domain.Author;
import net.scholnick.isbndb.domain.Book;

import org.junit.Test;

/**
 * UnitTests
 * 
 * @author Steve Scholnick <scholnicks@gmail.com>
 */
public final class BooksProxyTestCase {
	@Test
	public void singleBookTitle() throws IOException {
		List<Book> books = BooksProxy.getInstance().parse( getURL("single_book.json") );
		assertEquals("Bag of bones", books.get(0).getTitle());
	}
	
	@Test
	public void singleBookAuthor() throws IOException {
		List<Book> books = BooksProxy.getInstance().parse( getURL("single_book.json") );
		assertEquals("King, Stephen Harris Morley", books.get(0).getAuthors().get(0).getName());
		assertEquals("king_stephen_harris_morley", books.get(0).getAuthors().get(0).getId());
	}
	
	@Test
	public void multipleBooksTitle() throws IOException {
		List<Book> books = BooksProxy.getInstance().parse( getURL("virus.json") );
		assertEquals(10, books.size());
		assertEquals("Virus", books.get(1).getTitle());
	}
	
	@Test
	public void downloadBooks() throws Exception {
		List<Book> books = BooksProxy.getInstance().getBooks("virus");
		assertFalse( books.isEmpty() );
	}

	@Test
	public void downloadBookByAuthorId() throws Exception {
		Author a = new Author();
		a.setId("steve_berry");
		List<Book> books = BooksProxy.getInstance().getBooks(a);
		assertFalse( books.isEmpty() );
	}

	@Test
	public void downloadBookByAuthorName() throws Exception {
		Author a = new Author();
		a.setName("steve berry");
		List<Book> books = BooksProxy.getInstance().getBooks(a);
		assertFalse( books.isEmpty() );
	}
	
	@Test
	public void downloadBookByISBN() throws Exception {
		Book book = BooksProxy.getInstance().getBookByISBN("9780684853505");
		assertEquals("Bag of bones", book.getTitle());
	}
	
	@Test
	public void downloadBookWithSpacesInTitle() throws Exception {
		List<Book> books = BooksProxy.getInstance().getBooks("bag of bones");
		assertEquals(10, books.size());
	}

	@Test
	public void minimumWaitIntervalUnder() throws Exception {
		BooksProxy.getInstance().setWaitInterval(0);
		assertEquals(1000, BooksProxy.getInstance().getWaitInterval());
		BooksProxy.getInstance().setWaitInterval(1000);
	}

	@Test
	public void minimumWaitIntervalOver() throws Exception {
		BooksProxy.getInstance().setWaitInterval(2500);
		assertEquals(2500, BooksProxy.getInstance().getWaitInterval());
		BooksProxy.getInstance().setWaitInterval(1000);
	}

	private URL getURL(String fileName) throws IOException {
		return this.getClass().getClassLoader().getResource("net/scholnick/isbndb/unittests/data/" + fileName);
	}

}
	