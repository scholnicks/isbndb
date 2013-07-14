package net.scholnick.isbndb;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import net.scholnick.isbndb.domain.Book;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class BooksProxy {
    private static final Logger log = Logger.getLogger(BooksProxy.class);
    
	private final ObjectMapper mapper;
	private final String       developerKey;
	
	private static final BooksProxy INSTANCE = new BooksProxy();

	private long lastAccessedTime;
	
	public static BooksProxy getInstance() {
		return INSTANCE;
	}
	
	public List<Book> getBooks(String title) throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		checkElapsedTime();
		
		URI uri = new URI("http","isbndb.com","/api/v2/json/" + developerKey + "/books", "q=" + title,null);
		return parse( uri.toURL() );
	}
	
	private BooksProxy() {
		mapper = new ObjectMapper();
		lastAccessedTime = -1L;
		
		try {
			Properties props = new Properties();
			props.load( getClass().getClassLoader().getResourceAsStream("isbndb.properties") );
			developerKey = props.getProperty("developer.key");
		}
		catch (IOException e) {
			throw new RuntimeException("Unable to read isbndb.properties",e);
		}
	}
	
	public List<Book> parse(URL url) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(url.openStream(),BooksResult.class).getData();
	}

	private void checkElapsedTime() {
		long sleepTime = 1000L; // 1 second

		if (lastAccessedTime != -1L) {
			long elapsedTime = System.currentTimeMillis() - lastAccessedTime;

			if (elapsedTime > 1000) // 1 sec
				return;

			sleepTime = 1000L - elapsedTime;
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
