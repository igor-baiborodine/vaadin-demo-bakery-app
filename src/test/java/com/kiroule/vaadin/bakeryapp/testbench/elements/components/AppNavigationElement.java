package com.kiroule.vaadin.bakeryapp.testbench.elements.components;

import com.kiroule.vaadin.bakeryapp.testbench.elements.ui.DashboardViewElement;
import com.kiroule.vaadin.bakeryapp.testbench.elements.ui.LoginViewElement;
import com.kiroule.vaadin.bakeryapp.testbench.elements.ui.ProductsViewElement;
import com.kiroule.vaadin.bakeryapp.testbench.elements.ui.StorefrontViewElement;
import com.kiroule.vaadin.bakeryapp.testbench.elements.ui.UsersViewElement;
import com.vaadin.flow.component.tabs.testbench.TabElement;
import com.vaadin.flow.component.tabs.testbench.TabsElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("app-navigation")
public class AppNavigationElement extends TestBenchElement {

	public StorefrontViewElement navigateToStorefront() {
		return navigateTo(0, StorefrontViewElement.class);
	}

	public DashboardViewElement navigateToDashboard() {
		return navigateTo(1, DashboardViewElement.class);
	}

	public UsersViewElement navigateToUsers() {
		return navigateTo(2, UsersViewElement.class);
	}

	public ProductsViewElement navigateToProducts() {
		return navigateTo(3, ProductsViewElement.class);
	}

	public LoginViewElement logout() {
		$(TabElement.class).last().click();
		return $(LoginViewElement.class).onPage().waitForFirst();
	}

	private <T extends TestBenchElement> T navigateTo(int index, Class<T> landingPage) {
		$(TabsElement.class).first().$(TabElement.class).get(index).click();
		return $(landingPage).onPage().waitForFirst();
	}
}
