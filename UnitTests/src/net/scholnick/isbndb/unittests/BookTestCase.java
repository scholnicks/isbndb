package net.scholnick.isbndb.unittests;

import static junit.framework.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import net.scholnick.isbndb.BooksProxy;
import net.scholnick.isbndb.domain.Book;

import org.junit.Test;

public final class BookTestCase {
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
		assertEquals(10, books.size());
	}
	
	@Test
	public void downloadBookWithSpacesInTitle() throws Exception {
		List<Book> books = BooksProxy.getInstance().getBooks("bag of bones");
		assertEquals(10, books.size());
	}

	private URL getURL(String fileName) throws IOException {
		return this.getClass().getClassLoader().getResource("net/scholnick/isbndb/unittests/data/" + fileName);
	}

}
	