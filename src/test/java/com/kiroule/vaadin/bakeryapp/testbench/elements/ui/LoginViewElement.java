package com.kiroule.vaadin.bakeryapp.testbench.elements.ui;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.textfield.testbench.PasswordFieldElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("login-view")
public class LoginViewElement extends TestBenchElement {

	public StorefrontViewElement login(String username, String password) {
		return login(username, password, StorefrontViewElement.class);
	}

	public <E extends TestBenchElement> E login(String username, String password, Class<E> target) {
		setUsername(username);
		setPassword(password);
		signIn();

		return $(target).onPage().waitForFirst();
	}

	public void signIn() {
		$(ButtonElement.class).waitForFirst().click();
	}

	public void setPassword(String password) {
		$(PasswordFieldElement.class).waitForFirst().setValue(password);
	}

	public void setUsername(String username) {
		$(TextFieldElement.class).waitForFirst().setValue(username);
	}

	public String getUsernameLabel() {
		return $(TextFieldElement.class).waitForFirst().getLabel();
	}
}