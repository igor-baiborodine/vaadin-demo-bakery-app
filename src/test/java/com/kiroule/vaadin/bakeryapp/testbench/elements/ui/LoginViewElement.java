package com.kiroule.vaadin.bakeryapp.testbench.elements.ui;

import com.vaadin.flow.component.login.testbench.LoginOverlayElement;
import com.vaadin.flow.component.orderedlayout.testbench.VerticalLayoutElement;
import com.vaadin.testbench.TestBenchElement;

public class LoginViewElement extends VerticalLayoutElement {

	public StorefrontViewElement login(String username, String password) {
		return login(username, password, StorefrontViewElement.class);
	}

	public <E extends TestBenchElement> E login(
		String username, String password, Class<E> target) {

		final LoginOverlayElement loginElement = getLoginElement();
		loginElement.getUsernameField().setValue(username);
		loginElement.getPasswordField().setValue(password);
		loginElement.getSubmitButton().click();

		return $(target).onPage().waitForFirst();
	}

	public String getUsernameLabel() {
		return getLoginElement().getUsernameField().getLabel();
	}

	private LoginOverlayElement getLoginElement() {
		return $(LoginOverlayElement.class).waitForFirst();
	}

}
