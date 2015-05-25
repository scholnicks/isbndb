package net.scholnick.isbndb;

/**
 * RESTException can be thrown by any {@link BooksProxy} method that performs a REST call.
 * 
 * @author Steve Scholnick <scholnicks@gmail.com>
 */
public final class RESTException extends RuntimeException {
	private static final long serialVersionUID = 6746233809715504263L;

	/** Constructor */
	public RESTException() {
	}

	/** Constructor */
	public RESTException(String message) {
		super(message);
	}

	/** Constructor */
	public RESTException(Throwable cause) {
		super(cause);
	}

	/** Constructor */
	public RESTException(String message, Throwable cause) {
		super(message, cause);
	}
}
