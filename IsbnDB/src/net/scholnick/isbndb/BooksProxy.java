package net.scholnick.isbndb;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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
 * BooksProxy handles all interactions with the isbndb.com REST API. Queries to isbndb are spaced out by waitInterval, 
 * which defaults to 1 second. 
 * 
 * @author Steve Scholnick <scholnicks@gmail.com>
 */
public final class BooksProxy {
    private static final Logger log = Logger.getLogger(BooksProxy.class);
    
	private final ObjectMapper mapper;
	
	private String developerKey;
	private int    waitInterval = MINIMUM_WAIT_INTERVAL;
	
	private static final BooksProxy INSTANCE = new BooksProxy();

	private long lastAccessedTime;
	
    private static final int MINIMUM_WAIT_INTERVAL = 1000;
	private static final int TIMEOUT = 5 * 1000;
	
	/** Single Access Point */
	public static BooksProxy getInstance() {
		return INSTANCE;
	}
	
	/** Returns the developer key */
	public String getDeveloperKey() {
		return developerKey;
	}

	/** Sets the developer key */
	public void setDeveloperKey(String developerKey) {
		this.developerKey = developerKey;
	}

	/** Returns the wait interval between calls to isbndb */
	public int getWaitInterval() {
		return waitInterval;
	}

	/** Sets the wait interval between calls to isbndb */
	public void setWaitInterval(int waitInterval) {
		this.waitInterval = waitInterval;
		validateWaitInterval();
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
		catch (URISyntaxException | IOException e) {
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
			title = title.replaceAll(" ","_");
			URI uri = new URI("http","isbndb.com","/api/v2/json/" + developerKey + "/books", "q=" + title,null);
			log.debug("Getting JSON from " + uri);
			return parse( uri.toURL() );
		} 
		catch (URISyntaxException | IOException e) {
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
		catch (URISyntaxException | IOException e) {
			throw new RESTException("Unable to parse JSON",e);
		}
	}
	
	/** Private Constructor */
	private BooksProxy() {
		mapper = new ObjectMapper();
		lastAccessedTime = -1L;
		loadProperties();
	}
	
	/** Ensures the <code>waitInterval</code> cannot be less that the minimum */
	private void validateWaitInterval() {
		if (waitInterval < MINIMUM_WAIT_INTERVAL) {
			waitInterval = MINIMUM_WAIT_INTERVAL;
			log.warn("Wait interval cannot be less than the minimum wait interval of " + MINIMUM_WAIT_INTERVAL);
		}
	}
	
	/** Loads the properies if <code>isbndb.properties</code> exists in the classpath */
	private void loadProperties() {
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("isbndb.properties");
			
			if (is != null) {
				Properties props = new Properties();
				props.load(is);
				developerKey = props.getProperty("developer.key").trim();
				
				String waitInternalProperty = props.getProperty("wait.interval");
				if (waitInternalProperty != null) {
					waitInterval = Integer.valueOf(waitInternalProperty.trim());
					validateWaitInterval();
				}
			}
		}
		catch (IOException e) {
			throw new RuntimeException("Unable to read isbndb.properties",e);
		}
	}
	
	/** Parses the JSON returned from the URL */
	public List<Book> parse(URL url) throws JsonParseException, JsonMappingException, IOException {
		HttpURLConnection huc = (HttpURLConnection) url.openConnection();
		HttpURLConnection.setFollowRedirects(false);
		huc.setConnectTimeout(TIMEOUT);
		huc.setReadTimeout(TIMEOUT);
		huc.setRequestMethod("GET");
		huc.connect();

		try (InputStream is = huc.getInputStream()){
			return mapper.readValue(is,BooksResult.class).getData();
		}
	}

	/** Checks for the last accessed time. And if it is less than the wait interval, the wait interval is slept */
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
