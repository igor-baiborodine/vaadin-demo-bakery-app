package com.kiroule.vaadin.bakeryapp.ui.views.storefront.events;

import com.kiroule.vaadin.bakeryapp.ui.views.orderedit.OrderItemsEditor;
import com.vaadin.flow.component.ComponentEvent;

public class ValueChangeEvent extends ComponentEvent<OrderItemsEditor> {

	public ValueChangeEvent(OrderItemsEditor component) {
		super(component, false);
	}
}