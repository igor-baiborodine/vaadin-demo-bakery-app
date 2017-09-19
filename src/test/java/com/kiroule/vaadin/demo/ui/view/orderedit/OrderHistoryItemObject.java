package com.kiroule.vaadin.demo.ui.view.orderedit;

public class OrderHistoryItemObject {

	private String date;
	private String message;
	private String author;

	public OrderHistoryItemObject(String date, String message, String author) {
		this.date = date;
		this.message = message;
		this.author = author;
	}

	public String getDate() {
		return date;
	}

	public String getMessage() {
		return message;
	}

	public String getAuthor() {
		return author;
	}

}
