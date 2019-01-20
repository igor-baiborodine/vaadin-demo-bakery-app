package com.kiroule.vaadin.bakeryapp.testbench.elements.ui;

import com.kiroule.vaadin.bakeryapp.testbench.elements.components.AppNavigationElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("main-view")
public class MainViewElement extends TestBenchElement {

	public AppNavigationElement getMenu() {
		return $(AppNavigationElement.class).first();
	}

}
