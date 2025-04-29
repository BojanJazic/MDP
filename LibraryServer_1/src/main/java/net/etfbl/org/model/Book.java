package net.etfbl.org.model;


import java.io.Serializable;
import java.util.Objects;

public class Book implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String bookTitle;
	private String bookAuthor;
	private String publicationYear;
	private String language;
	private String coverPage;
	private String bookContent;
	
	
	public Book() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Book(int id, String bookTitle, String bookAuthor, String publicationYear, 
				String language, String coverPage, String bookContent) {
		super();
		this.id = id;
		this.bookTitle = bookTitle;
		this.bookAuthor = bookAuthor;
		this.publicationYear = publicationYear;
		this.language = language;
		this.coverPage = coverPage;
		this.bookContent = bookContent;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getBookTitle() {
		return bookTitle;
	}


	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}


	public String getBookAuthor() {
		return bookAuthor;
	}


	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}


	public String getPublicationYear() {
		return publicationYear;
	}


	public void setPublicationYear(String publicationYear) {
		this.publicationYear = publicationYear;
	}


	public String getLanguage() {
		return language;
	}


	public void setLanguage(String language) {
		this.language = language;
	}


	public String getCoverPage() {
		return coverPage;
	}


	public void setCoverPage(String coverPage) {
		this.coverPage = coverPage;
	}


	public String getBookContent() {
		return bookContent;
	}


	public void setBookContent(String bookContent) {
		this.bookContent = bookContent;
	}


	@Override
	public String toString() {
		return "Book [id=" + id + ", bookTitle=" + bookTitle + ", bookAuthor=" + bookAuthor + ", publicationYear="
				+ publicationYear + ", language=" + language + ", coverPage=" + coverPage + ", bookContent="
				+ bookContent + "]";
	}


	@Override
	public int hashCode() {
		return Objects.hash(id);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book book = (Book) obj;
		return Objects.equals(this.id, book.id);
	}
	
}
