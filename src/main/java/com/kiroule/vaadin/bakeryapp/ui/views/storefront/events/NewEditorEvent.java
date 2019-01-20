package com.kiroule.vaadin.bakeryapp.ui.views.storefront.events;

import com.kiroule.vaadin.bakeryapp.ui.views.orderedit.OrderItemsEditor;
import com.vaadin.flow.component.ComponentEvent;

public class NewEditorEvent extends ComponentEvent<OrderItemsEditor> {

	public NewEditorEvent(OrderItemsEditor component) {
		super(component, false);
	}
}