package com.kiroule.vaadin.bakeryapp.testbench.elements.ui;

import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("order-item-editor")
public class OrderItemEditorElement extends TestBenchElement {

	public void clickAmountFieldPlus() {
		clickAmountFieldPlusOrMinus(1);
	}
	
	public void clickAmountFieldMinus() {
		clickAmountFieldPlusOrMinus(-1);
	}
	
	public TextFieldElement getCommentField() {
		return $(TextFieldElement.class).id("comment");
	}
	
	private void clickAmountFieldPlusOrMinus(int value) {
		if (value == 0) {
			throw new IllegalArgumentException("Value should be -1 or 1");
		}
		final String part = value < 0 ? "decrease-button" : "increase-button";
		$("vaadin-integer-field").first().$("div").attribute("part", part).first().click();
	}
}
