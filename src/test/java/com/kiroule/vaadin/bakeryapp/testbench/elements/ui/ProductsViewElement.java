package com.kiroule.vaadin.bakeryapp.testbench.elements.ui;

import com.kiroule.vaadin.bakeryapp.testbench.elements.components.SearchBarElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("products-view")
public class ProductsViewElement extends TestBenchElement implements HasApp, HasCrudView {

	@Element("product-form")
	public static class ProductFormElement extends TestBenchElement {
	}

	@Override
	public GridElement getGrid() {
		return $(GridElement.class).waitForFirst();
	}

	public TextFieldElement getPrice() {
		return getForm().$(TextFieldElement.class).id("price");
	}

	public TextFieldElement getProductName() {
		return getForm().$(TextFieldElement.class).id("name");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<ProductFormElement> getFormClass() {
		return ProductFormElement.class;
	}

	public SearchBarElement getSearchBar() {
		return $(SearchBarElement.class).first();
	}
}
