package com.kiroule.vaadin.bakeryapp.testbench.elements.ui;

import com.kiroule.vaadin.bakeryapp.testbench.elements.components.SearchBarElement;
import com.vaadin.flow.component.combobox.testbench.ComboBoxElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.textfield.testbench.PasswordFieldElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("users-view")
public class UsersViewElement extends TestBenchElement implements HasCrudView, HasApp {

	@Element("user-form")
	public static class UserFormElement extends TestBenchElement {
	}

	@Override
	public GridElement getGrid() {
		return $(GridElement.class).waitForFirst();
	}

	public TextFieldElement getEmailField() {
		return getForm().$(TextFieldElement.class).id("email");
	}

	public TextFieldElement getFirstName() {
		return getForm().$(TextFieldElement.class).id("first");
	}

	public TextFieldElement getLastName() {
		return getForm().$(TextFieldElement.class).id("last");
	}

	public PasswordFieldElement getPasswordField() {
		return getForm().$(PasswordFieldElement.class).id("password");
	}

	public ComboBoxElement getRole() {
		return getForm().$(ComboBoxElement.class).id("role");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<UserFormElement> getFormClass() {
		return UserFormElement.class;
	}

	public SearchBarElement getSearchBar() {
		return $(SearchBarElement.class).first();
	}
}
