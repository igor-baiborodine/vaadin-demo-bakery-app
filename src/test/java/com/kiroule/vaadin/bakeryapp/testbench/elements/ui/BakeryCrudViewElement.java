package com.kiroule.vaadin.bakeryapp.testbench.elements.ui;

import com.kiroule.vaadin.bakeryapp.testbench.elements.components.SearchBarElement;
import com.vaadin.flow.component.confirmdialog.testbench.ConfirmDialogElement;
import com.vaadin.flow.component.crud.testbench.CrudElement;
import com.vaadin.flow.component.formlayout.testbench.FormLayoutElement;

public class BakeryCrudViewElement extends CrudElement implements HasApp {

	public SearchBarElement getSearchBar() {
		return $(SearchBarElement.class).first();
	}

	public FormLayoutElement getForm() {
		return getEditor().$(FormLayoutElement.class).first();
	}

	public ConfirmDialogElement getDiscardConfirmDialog() {
		return $(ConfirmDialogElement.class).first();
	}

	public ConfirmDialogElement getDeleteConfirmDialog() {
		return $(ConfirmDialogElement.class).last();
	}
}
