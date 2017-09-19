package com.kiroule.vaadin.demo.ui.components;

import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.VerticalLayoutElement;
import com.vaadin.testbench.elementsbase.ServerClass;

@ServerClass("org.vaadin.dialogs.ConfirmDialog")
public class ConfirmationDialogDesignElement extends VerticalLayoutElement {

	public ButtonElement getCancel() {
		return $(ButtonElement.class).id("confirmdialog-cancel-button");
	}

	public ButtonElement getDiscardChanges() {
		return $(ButtonElement.class).id("confirmdialog-ok-button");
	}

}
