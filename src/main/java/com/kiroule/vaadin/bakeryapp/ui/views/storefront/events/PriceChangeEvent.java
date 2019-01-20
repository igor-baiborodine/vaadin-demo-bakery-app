package com.kiroule.vaadin.bakeryapp.ui.views.storefront.events;

import com.kiroule.vaadin.bakeryapp.ui.views.orderedit.OrderItemEditor;
import com.vaadin.flow.component.ComponentEvent;

public class PriceChangeEvent extends ComponentEvent<OrderItemEditor> {

	private final int oldValue;

	private final int newValue;

	public PriceChangeEvent(OrderItemEditor component, int oldValue, int newValue) {
		super(component, false);
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public int getOldValue() {
		return oldValue;
	}

	public int getNewValue() {
		return newValue;
	}

}