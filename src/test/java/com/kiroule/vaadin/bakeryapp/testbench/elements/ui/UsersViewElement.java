package com.kiroule.vaadin.bakeryapp.testbench.elements.ui;

import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.textfield.testbench.PasswordFieldElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;

public class UsersViewElement extends BakeryCrudViewElement {

	public TextFieldElement getEmailField() {
		return getForm().$(TextFieldElement.class).first();
	}

	public TextFieldElement getFirstName() {
		return getForm().$(TextFieldElement.class).all().get(1);
	}

	public TextFieldElement getLastName() {
		return getForm().$(TextFieldElement.class).all().get(2);
	}

	public PasswordFieldElement getPasswordField() {
		return getForm().$(PasswordFieldElement.class).first();
	}

	public ComboBoxElement getRole() {
		return getForm().$(ComboBoxElement.class).first();
	}

}
