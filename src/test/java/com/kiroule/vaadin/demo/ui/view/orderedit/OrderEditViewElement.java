package com.kiroule.vaadin.demo.ui.view.orderedit;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.beans.SamePropertyValuesAs;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import com.kiroule.vaadin.demo.AbstractIT;
import com.kiroule.vaadin.demo.backend.data.OrderState;
import com.kiroule.vaadin.demo.backend.data.entity.Customer;
import com.kiroule.vaadin.demo.ui.view.orderedit.ProductInfoElement.ProductOrderData;
import com.vaadin.testbench.HasDriver;
import com.vaadin.testbench.elements.CssLayoutElement;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elementsbase.ServerClass;

@ServerClass("com.kiroule.vaadin.demo.ui.view.orderedit.OrderEditView")
public class OrderEditViewElement extends OrderEditViewDesignElement {

	public static class OrderInfo {
		LocalDate dueDate;
		LocalTime dueTime;
		Customer customer;
		String pickupLocation;
		List<ProductOrderData> products;
		String total;
		OrderState state;
	}

	public OrderState getCurrentState() {
		try {
			LabelElement stateLabel = getStateLabel();
			String displayName = stateLabel.getText();
			return OrderState.forDisplayName(displayName);
		} catch (NoSuchElementException e) {
			// State label is not shown for the "confirmation" view
			return OrderState.NEW;
		}
	}

	public int getNumberOfProducts() {
		return getProductInfoContainer().findElements(By.xpath("./div")).size();
	}

	public ProductInfoElement getProductInfo(int i) {
		return getProductInfoContainer().$(CssLayoutElement.class).get(i).wrap(ProductInfoElement.class);
	}

	public void setCustomerInfo(Customer customer) {
		getFullName().setValue(customer.getFullName());
		getPhone().setValue(customer.getPhoneNumber());
		getDetails().setValue(customer.getDetails());
	}

	public Customer getCustomerInfo() {
		Customer customer = new Customer();
		customer.setFullName(getFullName().getValue());
		customer.setPhoneNumber(getPhone().getValue());
		customer.setDetails(getDetails().getValue());
		return customer;
	}

	public OrderInfo getOrderInfo() {
		OrderInfo order = new OrderInfo();
		order.customer = getCustomerInfo();
		order.dueDate = getDueDate().getDate();
		order.pickupLocation = getPickupLocation().getValue();
		order.products = new ArrayList<>();
		int nrProducts = getNumberOfProducts();
		for (int i = 0; i < nrProducts; i++) {
			order.products.add(getProductInfo(i).getProductOrderData());
		}
		order.total = getTotal().getText();
		order.state = getCurrentState();
		return order;
	}

	public void assertOrder(OrderInfo order) {
		OrderInfo currentInfo = getOrderInfo();
		Assert.assertEquals(order.dueDate, currentInfo.dueDate);
		Assert.assertEquals(order.pickupLocation, currentInfo.pickupLocation);
		Assert.assertThat(order.customer, SamePropertyValuesAs.samePropertyValuesAs(currentInfo.customer));
		Assert.assertEquals(order.state, currentInfo.state);

		for (int i = 0; i < order.products.size(); i++) {
			Assert.assertThat(order.products.get(i),
					SamePropertyValuesAs.samePropertyValuesAs(currentInfo.products.get(i)));
		}

		// Check total sum
		Assert.assertEquals(order.total, currentInfo.total);

	}

	public void setProducts(List<ProductOrderData> products) {
		for (int i = 0; i < products.size(); i++) {
			ProductOrderData product = products.get(i);

			getProductInfo(i).setProduct(product);
		}
	}

	public static OrderEditViewElement get(HasDriver hasDriver) {
		return AbstractIT.findFirstElement(hasDriver, OrderEditViewElement.class);
	}

}