package com.kiroule.vaadin.bakeryapp.ui.view.orderedit;

import com.kiroule.vaadin.bakeryapp.backend.data.entity.OrderItem;

public class OrderItemDeletedEvent {

	private OrderItem orderItem;

	public OrderItemDeletedEvent(OrderItem orderItem) {
		this.orderItem = orderItem;
	}

	public OrderItem getOrderItem() {
		return orderItem;
	}
}
