package net.scholnick.isbndb;

import java.util.List;

import net.scholnick.isbndb.domain.Book;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * BooksResult holds all of the data returned by the REST call
 * 
 * @author Steve Scholnick <steve@scholnick.net>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
final class BooksResult {
	private List<Book> data;

	public List<Book> getData() {
		return data;
	}

	public void setData(List<Book> data) {
		this.data = data;
	}
	
    /** {@inheritDoc} */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
