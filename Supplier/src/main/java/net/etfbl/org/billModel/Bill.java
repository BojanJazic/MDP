package net.etfbl.org.billModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.stream.Collectors;

import net.etfbl.org.model.Book;

public class Bill implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Random random = new Random();
	
	
	private ArrayList<String> orderedBooksList = new ArrayList<String>();
	private Date date;
	private double price = 0.0;
	
	
	public Bill(ArrayList<Book> listOfBooks) {
		this.orderedBooksList = listOfBooks.stream()
										   .map(book -> book.getBookTitle())
										   .collect(Collectors.toCollection(ArrayList::new));
		this.date = new Date();
		
		for(int i = 0; i < orderedBooksList.size(); i++) {
			price += random.nextInt(30) + 20;
		}
	}


	public ArrayList<String> getOrderedBooksList() {
		return orderedBooksList;
	}


	public void setOrderedBooksList(ArrayList<String> orderedBooksList) {
		this.orderedBooksList = orderedBooksList;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}
	
	

}
