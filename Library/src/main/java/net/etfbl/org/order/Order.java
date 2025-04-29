package net.etfbl.org.order;

import java.io.Serializable;
import java.util.ArrayList;


public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<OrderItem> orderItemsList = new ArrayList<OrderItem>();
	
	public Order(ArrayList<OrderItem> orderItem) {
		orderItemsList = orderItem;
	}
	
	public ArrayList<OrderItem> getOrderItems(){
		return orderItemsList;
	}
	
}
