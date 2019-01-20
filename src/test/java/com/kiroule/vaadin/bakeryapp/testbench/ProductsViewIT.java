package com.kiroule.vaadin.bakeryapp.testbench;

import com.kiroule.vaadin.bakeryapp.testbench.elements.ui.ProductsViewElement;
import com.kiroule.vaadin.bakeryapp.testbench.elements.ui.StorefrontViewElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.Keys;

public class ProductsViewIT extends AbstractIT<ProductsViewElement> {

	private static Random r = new Random();

	@Override
	protected ProductsViewElement openView() {
		StorefrontViewElement storefront = openLoginView().login("admin@vaadin.com", "admin");
		return storefront.getMenu().navigateToProducts();
	}

	@Test
	public void editProduct() {
		ProductsViewElement productsPage = openView();

		Assert.assertFalse(productsPage.getDialog().isPresent());

		String url = getDriver().getCurrentUrl();
		GridElement grid = productsPage.getGrid();

		String uniqueName = "Unique cake name " + r.nextInt();
		String initialPrice = "98.76";
		createProduct(productsPage, uniqueName, initialPrice);

		grid.getCell(uniqueName).click();
		Assert.assertTrue(getDriver().getCurrentUrl().length() > url.length());

		Assert.assertTrue(productsPage.getDialog().get().isOpen());

		TextFieldElement price = productsPage.getPrice();
		Assert.assertEquals(initialPrice, price.getValue());

		price.focus();
		price.setValue("123.45");
		price.sendKeys(Keys.TAB);
		productsPage.getButtonsBar().getSaveButton().click();

		Assert.assertFalse(productsPage.getDialog().isPresent());

		Assert.assertTrue(getDriver().getCurrentUrl().endsWith("products"));

		grid.getCell(uniqueName).click();

		price = productsPage.getPrice(); // Requery the price element.
		Assert.assertEquals("123.45", price.getValue());

		// Return initial value
		price.focus();
		price.setValue(initialPrice);
		price.sendKeys(Keys.TAB);
		productsPage.getButtonsBar().getSaveButton().click();
	}

	@Test
	public void testCancelConfirmationMessage() {
		ProductsViewElement productsPage = openView();

		productsPage.getSearchBar().getCreateNewButton().click();
		productsPage.getDialog().get();
		productsPage.getProductName().setValue("Some name");
		productsPage.getButtonsBar().getCancelButton().click();
		Assert.assertEquals("There are unsaved modifications to the Product. Discard changes?",
				productsPage.getConfirmDialog().get().getMessageText());
	}

	private void createProduct(ProductsViewElement productsPage, String name, String price) {
		productsPage.getSearchBar().getCreateNewButton().click();

		Assert.assertTrue(productsPage.getDialog().get().isOpen());

		TextFieldElement nameField = productsPage.getProductName();
		TextFieldElement priceField = productsPage.getPrice();

		nameField.focus();
		nameField.setValue(name);

		priceField.focus();
		priceField.setValue(price);

		productsPage.getButtonsBar().getSaveButton().click();
	}

}
