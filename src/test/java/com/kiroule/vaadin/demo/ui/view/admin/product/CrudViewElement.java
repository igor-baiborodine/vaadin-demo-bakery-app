package com.kiroule.vaadin.demo.ui.view.admin.product;

import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elements.GridElement;
import com.vaadin.testbench.elements.TextFieldElement;

public interface CrudViewElement {

	GridElement getList();

	TextFieldElement getSearch();

	TestBenchElement getForm();

	TestBenchElement getUpdate();

	TestBenchElement getCancel();

	TestBenchElement getDelete();

	TestBenchElement getAdd();

	boolean isDisplayed();
}
