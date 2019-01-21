package com.kiroule.vaadin.bakeryapp.testbench.elements.ui;

import com.kiroule.vaadin.bakeryapp.testbench.elements.components.AppNavigationElement;
import com.vaadin.flow.component.applayout.testbench.AppLayoutElement;

public class MainViewElement extends AppLayoutElement {

	public AppNavigationElement getMenu() {
		return $(AppNavigationElement.class).first();
	}

}
