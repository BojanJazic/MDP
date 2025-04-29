package net.etfbl.org.order;

import java.io.Serializable;
import java.util.Random;

import net.etfbl.org.model.Book;

public class OrderItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Random random = new Random();
	
	private Book book;
	private int quantity;
	
	public OrderItem(Book book) {
		this.book = book;
		this.quantity = random.nextInt(11) + 10;
	}
	
	public Book getBook() {
		return book;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	@Override
	public String toString() {
		return book.getBookTitle() + "   " + quantity;
	}
	
}
