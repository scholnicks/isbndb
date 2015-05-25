package net.scholnick.isbndb.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Author of a book
 * 
 * @author Steve Scholnick <scholnicks@gmail.com>
 */
public final class Author {
	private String id;
	private String name;
	
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
