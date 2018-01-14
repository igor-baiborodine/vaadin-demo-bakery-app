package com.kiroule.vaadin.bakeryapp.backend.data.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.validation.constraints.NotNull;

import com.kiroule.vaadin.bakeryapp.backend.data.OrderState;

// "Order" is a reserved word
@Entity(name = "OrderInfo")
@NamedEntityGraphs({ @NamedEntityGraph(name = "Order.gridData", attributeNodes = { @NamedAttributeNode("customer") }),
		@NamedEntityGraph(name = "Order.allData", attributeNodes = { @NamedAttributeNode("customer"),
				@NamedAttributeNode("items"), @NamedAttributeNode("history") }) })
public class Order extends AbstractEntity {

	@NotNull
	private LocalDate dueDate;
	@NotNull
	private LocalTime dueTime;
	@NotNull
	@ManyToOne
	private PickupLocation pickupLocation;
	@NotNull
	@OneToOne(cascade = CascadeType.ALL)
	private Customer customer;
	@NotNull
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderColumn(name = "id")
	private List<OrderItem> items;
	@NotNull
	private OrderState state;

	private boolean paid;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderColumn(name = "id")
	private List<HistoryItem> history;

	public Order() {
		// Empty constructor is needed by Spring Data / JPA
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public LocalTime getDueTime() {
		return dueTime;
	}

	public void setDueTime(LocalTime dueTime) {
		this.dueTime = dueTime;
	}

	public PickupLocation getPickupLocation() {
		return pickupLocation;
	}

	public void setPickupLocation(PickupLocation pickupLocation) {
		this.pickupLocation = pickupLocation;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public Stream<OrderItem> getItemsStream() {
		// Make a copy of the items list to work around
		// EclipseLink bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=467470
		return new ArrayList<>(getItems()).stream();
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public List<HistoryItem> getHistory() {
		return history;
	}

	public void setHistory(List<HistoryItem> history) {
		this.history = history;
	}

	public OrderState getState() {
		return state;
	}

	public void setState(OrderState state) {
		this.state = state;
	}

}
