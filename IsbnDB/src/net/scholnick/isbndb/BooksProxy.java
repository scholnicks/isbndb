package net.scholnick.isbndb;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import net.scholnick.isbndb.domain.Author;
import net.scholnick.isbndb.domain.Book;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * BooksProxy handles all interactions with the isbndb.com REST API.
 * 
 * @author Steve Scholnick <steve@scholnick.net>
 */
public final class BooksProxy {
    private static final Logger log = Logger.getLogger(BooksProxy.class);
    
	private final ObjectMapper mapper;
	private String       developerKey;
	private Long         waitInterval = 1000L;
	
	private static final BooksProxy INSTANCE = new BooksProxy();

	private long lastAccessedTime;
	
	/** Single Access Point */
	public static BooksProxy getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Returns all of the {@link Book}s for an {@link Author}
	 * 
	 * @param author					{@link Author} query
	 * @return							{@link Book}s
	 * @throws RESTException		    If unable to parse the returned JSON
	 */
	public List<Book> getBooks(Author author) {
		checkElapsedTime();
		
		String key         = null;
		String searchField = null;
		
		if (author.getId() == null) {
			key = author.getName();
			searchField = "author_name";
		}
		else {
			key = author.getId();
			searchField = "author_id";
		}
	
		URI uri;
		try {
			uri = new URI("http","isbndb.com","/api/v2/json/" + developerKey + "/books", "q=" + key + "&i=" + searchField,null);
			return parse( uri.toURL() );
		} 
		catch (URISyntaxException e) {
			throw new RESTException("Unable to parse JSON",e);
		} 
		catch (JsonParseException e) {
			throw new RESTException("Unable to parse JSON",e);
		} 
		catch (JsonMappingException e) {
			throw new RESTException("Unable to parse JSON",e);
		} 
		catch (MalformedURLException e) {
			throw new RESTException("Unable to parse JSON",e);
		} 
		catch (IOException e) {
			throw new RESTException("Unable to parse JSON",e);
		}
	}
	
	/**
	 * Returns all of the {@link Book}s matching a title
	 * 
	 * @param title						Book title
	 * @return							{@link Book}s
	 * @throws RESTException		    If unable to parse the returned JSON
	 */
	public List<Book> getBooks(String title) {
		checkElapsedTime();
		
		try {
			URI uri = new URI("http","isbndb.com","/api/v2/json/" + developerKey + "/books", "q=" + title,null);
			return parse( uri.toURL() );
		} 
		catch (URISyntaxException e) {
			throw new RESTException("Unable to parse JSON",e);
		} 
		catch (JsonParseException e) {
			throw new RESTException("Unable to parse JSON",e);
		} 
		catch (JsonMappingException e) {
			throw new RESTException("Unable to parse JSON",e);
		} 
		catch (MalformedURLException e) {
			throw new RESTException("Unable to parse JSON",e);
		} 
		catch (IOException e) {
			throw new RESTException("Unable to parse JSON",e);
		}
	}
	
	/**
	 * Returns the {@link Book} that matches the ISBN
	 * 
	 * @param isbn						ISBN
	 * @return							Matched {@link Book} or <code>null</code>
	 * @throws RESTException		    If unable to parse the returned JSON
	 */
	public Book getBookByISBN(String isbn) {
		checkElapsedTime();

		try {
			URI uri = new URI("http","isbndb.com","/api/v2/json/" + developerKey + "/book/" +isbn,null,null);
			List<Book> results = parse( uri.toURL() );
			return results.isEmpty() ? null : results.get(0);
		} 
		catch (URISyntaxException e) {
			throw new RESTException("Unable to parse JSON",e);
		} 
		catch (JsonParseException e) {
			throw new RESTException("Unable to parse JSON",e);
		} 
		catch (JsonMappingException e) {
			throw new RESTException("Unable to parse JSON",e);
		} 
		catch (MalformedURLException e) {
			throw new RESTException("Unable to parse JSON",e);
		} 
		catch (IOException e) {
			throw new RESTException("Unable to parse JSON",e);
		}
	}
	
	private BooksProxy() {
		mapper = new ObjectMapper();
		lastAccessedTime = -1L;
		loadProperties();
	}
	
	private void loadProperties() {
		try {
			Properties props = new Properties();
			props.load( getClass().getClassLoader().getResourceAsStream("isbndb.properties") );
			developerKey = props.getProperty("developer.key").trim();
			
			String waitInternalProperty = props.getProperty("wait.interval");
			if (waitInternalProperty != null) {
				waitInterval = Long.valueOf(waitInternalProperty.trim());
			}
		}
		catch (IOException e) {
			throw new RuntimeException("Unable to read isbndb.properties",e);
		}
	}
	
	public List<Book> parse(URL url) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(url.openStream(),BooksResult.class).getData();
	}

	private void checkElapsedTime() {
		long sleepTime = waitInterval; // 1 second

		if (lastAccessedTime != -1L) {
			long elapsedTime = System.currentTimeMillis() - lastAccessedTime;

			if (elapsedTime > waitInterval) // 1 sec
				return;

			sleepTime = waitInterval - elapsedTime;
			log.debug("Pausing.  Elapsed time = " + elapsedTime + " ms, waiting for " + sleepTime + " ms");
		}

		try {
			Thread.sleep(sleepTime);
		}
		catch (InterruptedException ie) {
			// just return
		}
	}
}
