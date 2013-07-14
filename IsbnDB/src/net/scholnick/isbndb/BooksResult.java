package net.scholnick.isbndb;

import java.util.List;

import net.scholnick.isbndb.domain.Book;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class BooksResult {
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
