package com.kiroule.vaadin.demo.ui.view.orderedit;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.kiroule.vaadin.demo.AbstractIT;
import com.kiroule.vaadin.demo.backend.data.OrderState;
import com.kiroule.vaadin.demo.backend.data.entity.Customer;
import com.kiroule.vaadin.demo.ui.components.ConfirmationDialogDesignElement;
import com.kiroule.vaadin.demo.ui.util.DollarPriceConverter;
import com.kiroule.vaadin.demo.ui.view.MenuElement;
import com.kiroule.vaadin.demo.ui.view.orderedit.OrderEditViewElement.OrderInfo;
import com.kiroule.vaadin.demo.ui.view.orderedit.ProductInfoElement.ProductOrderData;
import com.kiroule.vaadin.demo.ui.view.storefront.StorefrontViewElement;
import com.vaadin.testbench.ElementQuery;

public class AddOrderIT extends AbstractIT {

	@Test
	public void emptyAddOrderView() {
		StorefrontViewElement storefront = loginAsBarista();
		OrderEditViewElement orderEditView = storefront.clickNewOrder();
		assertNotFound("Order state should not be shown", () -> orderEditView.getStateLabel());
		assertNotFound("Order id should not be shown", () -> orderEditView.getOrderId());
		assertNotFound("Set state dropdown should not be shown", () -> orderEditView.getState());

		assertEnabledWithCaption("Cancel", orderEditView.getEditOrCancel());
		assertEnabledWithCaption("Review order", orderEditView.getOk());
		assertEnabledWithCaption("Add item", orderEditView.getAddItems());

	}

	public static class TestOrder extends OrderInfo {
		public TestOrder() {
			dueDate = LocalDate.of(2025, 12, 5);
			dueTime = LocalTime.of(8, 00);
			state = OrderState.NEW;
			customer = new Customer();
			pickupLocation = "Store";
			products = new ArrayList<>();
			customer.setFullName("First Last");
			customer.setPhoneNumber("Phone");
			customer.setDetails("Details");

			ProductOrderData productOrderData = new ProductOrderData("Strawberry Cheese Cake", 2, "Lactose free");
			// Price used only to verify that the UI is updated correctly
			productOrderData.setPrice(78.16);
			products.add(productOrderData);
			productOrderData = new ProductOrderData("Vanilla Cracker", 1, "");
			// Price used only to verify that the UI is updated correctly
			productOrderData.setPrice(98.77);
			products.add(productOrderData);
			total = "$255.09"; // 78.16*2+98.77
		}
	}

	@Test
	public void addOrder() {
		StorefrontViewElement storefront = loginAsBarista();
		OrderEditViewElement orderEditView = storefront.clickNewOrder();

		OrderInfo testOrder = new TestOrder();
		orderEditView.setCustomerInfo(testOrder.customer);
		orderEditView.getDueDate().setDate(testOrder.dueDate);
		orderEditView.getDueTime().selectByText(testOrder.dueTime.toString());
		orderEditView.getPickupLocation().selectByText(testOrder.pickupLocation);

		for (int i = 0; i < testOrder.products.size(); i++) {
			if (i > 0) {
				orderEditView.getAddItems().click();
			}
			ProductOrderData product = testOrder.products.get(i);
			ProductInfoElement productInfo = orderEditView.getProductInfo(i);
			productInfo.setProduct(product);
			// Check that (unit) price was updated correctly
			String itemPriceText = productInfo.getPrice().getText();
			Result<Integer> itemPrice = new DollarPriceConverter().convertToModel(itemPriceText,
					new ValueContext(Locale.US));

			Assert.assertEquals((Integer) product.getPrice(), itemPrice.getOrThrow(RuntimeException::new));
		}

		// Check total sum
		Assert.assertEquals(testOrder.total, orderEditView.getTotal().getText());

		// Add empty row
		orderEditView.getAddItems().click();

		// Add + delete row
		orderEditView.getAddItems().click();
		ProductOrderData product = testOrder.products.get(0);
		ProductInfoElement productInfo = orderEditView.getProductInfo(orderEditView.getNumberOfProducts() - 1);
		productInfo.setProduct(product);
		Assert.assertEquals(testOrder.products.size() + 2, orderEditView.getNumberOfProducts());
		orderEditView.getProductInfo(orderEditView.getNumberOfProducts() - 1).getDelete().click();

		// One extra row at the bottom, but it should not affect the result
		Assert.assertEquals(testOrder.products.size() + 1, orderEditView.getNumberOfProducts());

		// Done -> go to confirmation screen
		orderEditView.getOk().click();
		// Ensure that that we are on the confirmation screen
		assertEnabledWithCaption("Place order", orderEditView.getOk());

		// Order info intact
		orderEditView.assertOrder(testOrder);

		// Empty rows should have been removed
		Assert.assertEquals(testOrder.products.size(), orderEditView.getNumberOfProducts());

		// Place order -> go to order report screen
		// This causes a reload so we need first wait until the refresh is done
		// and then fetch a new orderEditView reference
		orderEditView.getOk().click();
		// Re-fetch the orderEditView reference as the whole view was updated
		orderEditView = $(OrderEditViewElement.class).first();

		// ID is of type #1234
		String orderIdText = orderEditView.getOrderId().getText();
		Assert.assertTrue(orderIdText.matches("#\\d+"));

		// Order info intact
		orderEditView.assertOrder(testOrder);

		// Check URL is update correctly so we can refresh to show the same
		// order
		int orderId = Integer.parseInt(orderIdText.substring(1));
		String url = getDriver().getCurrentUrl();
		Assert.assertTrue("Url " + url + " should end with #!order/" + orderId, url.endsWith("#!order/" + orderId));

		assertEnabledWithCaption("Edit", orderEditView.getEditOrCancel());
		assertEnabledWithCaption("Mark as Confirmed", orderEditView.getOk());

		// Reload and verify the order was stored in DB and shown correctly
		getDriver().navigate().refresh();
		testBench().waitForVaadin();

		// Re-fetch the orderEditView reference as the whole view was updated
		orderEditView = new ElementQuery<>(OrderEditViewElement.class).context(getDriver()).first();
		orderEditView.assertOrder(testOrder);

	}

	@Test
	public void changeStateForNewOrder() {
		StorefrontViewElement storefront = loginAsBarista();
		OrderEditViewElement orderEditView = storefront.clickNewOrder();

		orderEditView.getFullName().setValue("fullname");
		orderEditView.getPhone().setValue("phone");
		orderEditView.getDetails().setValue("detailss");
		orderEditView
				.setProducts(Collections.singletonList(new ProductOrderData("Blueberry Cheese Cake", 12, "A comment")));

		orderEditView.getOk().click();
		orderEditView.getOk().click();

		// Re-fetch the orderEditView reference as the whole view was updated
		orderEditView = $(OrderEditViewElement.class).first();

		Assert.assertEquals(OrderState.NEW, orderEditView.getCurrentState());
		orderEditView.getEditOrCancel().click();
		orderEditView.getState().scrollIntoView();
		orderEditView.getState().selectByText(OrderState.CONFIRMED.getDisplayName());
		// Chrome scrolls back up when clicking due to some (un)focus issue,
		// avoid by explicitly focusing before clicking:
		orderEditView.getOk().focus();
		orderEditView.getOk().click();
		Assert.assertEquals(OrderState.CONFIRMED, orderEditView.getCurrentState());
	}

	@Test
	public void confirmDialogWhenAbandoningNewOrder() {
		StorefrontViewElement storefront = loginAsBarista();
		OrderEditViewElement orderEditView = storefront.clickNewOrder();

		orderEditView.getFullName().setValue("Something");

		// Navigate away and check that we did not move away and cancel the
		// confirmation dialog

		// Navigate away to another view
		$(MenuElement.class).first().getMenuLink("Storefront").click();
		Assert.assertTrue(orderEditView.isDisplayed());
		$(ConfirmationDialogDesignElement.class).first().getCancel().click();

		// Logout
		$(MenuElement.class).first().logout();
		Assert.assertTrue(orderEditView.isDisplayed());
		$(ConfirmationDialogDesignElement.class).first().getCancel().click();

	}

}
