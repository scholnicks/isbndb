package net.scholnick.isbndb.domain;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Book is a single instance of book data
 * 
 * @author Steve Scholnick <steve@scholnick.net>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Book {
	@JsonProperty("title_latin")               private String title;
	@JsonProperty("isbn13")                    private String isbn;
	@JsonProperty("physical_description_text") private String physicalDescription;
	@JsonProperty("publisher_name")            private String publisher;
	@JsonProperty("language")                  private String language;
	@JsonProperty("notes")                     private String notes;
	@JsonProperty("summary")                   private String summary;
	@JsonProperty("edition_info")              private String edition;
	@JsonProperty("dewey_normal")              private String deweyNumber;
	@JsonProperty("lcc_number")                private String lccNumber;
	
	@JsonProperty("author_data")               private List<Author> authors;
	@JsonProperty("subjectIds")                private List<String> subjectIds;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	
    public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public String getPhysicalDescription() {
		return physicalDescription;
	}

	public void setPhysicalDescription(String physicalDescription) {
		this.physicalDescription = physicalDescription;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getDeweyNumber() {
		return deweyNumber;
	}

	public void setDeweyNumber(String deweyNumber) {
		this.deweyNumber = deweyNumber;
	}

	public String getLccNumber() {
		return lccNumber;
	}

	public void setLccNumber(String lccNumber) {
		this.lccNumber = lccNumber;
	}

	public List<String> getSubjectIds() {
		return subjectIds;
	}

	public void setSubjectIds(List<String> subjectIds) {
		this.subjectIds = subjectIds;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
