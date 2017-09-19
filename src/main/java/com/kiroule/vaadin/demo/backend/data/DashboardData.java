package com.kiroule.vaadin.demo.backend.data;

import java.util.LinkedHashMap;
import java.util.List;

import com.kiroule.vaadin.demo.backend.data.entity.Product;

public class DashboardData {

	private DeliveryStats deliveryStats;
	private List<Number> deliveriesThisMonth;
	private List<Number> deliveriesThisYear;
	private Object salesLastYears;
	private Number[][] salesPerMonth;
	private LinkedHashMap<Product, Integer> productDeliveries;

	public DeliveryStats getDeliveryStats() {
		return deliveryStats;
	}

	public void setDeliveryStats(DeliveryStats deliveryStats) {
		this.deliveryStats = deliveryStats;
	}

	public List<Number> getDeliveriesThisMonth() {
		return deliveriesThisMonth;
	}

	public void setDeliveriesThisMonth(List<Number> deliveriesThisMonth) {
		this.deliveriesThisMonth = deliveriesThisMonth;
	}

	public List<Number> getDeliveriesThisYear() {
		return deliveriesThisYear;
	}

	public void setDeliveriesThisYear(List<Number> deliveriesThisYear) {
		this.deliveriesThisYear = deliveriesThisYear;
	}

	public Object getSalesLastYears() {
		return salesLastYears;
	}

	public void setSalesLastYears(Object salesLastYears) {
		this.salesLastYears = salesLastYears;
	}

	public Number[][] getSalesPerMonth() {
		return salesPerMonth;
	}

	public void setSalesPerMonth(Number[][] salesPerMonth) {
		this.salesPerMonth = salesPerMonth;
	}

	public Number[] getSalesPerMonth(int i) {
		return salesPerMonth[i];
	}

	public LinkedHashMap<Product, Integer> getProductDeliveries() {
		return productDeliveries;
	}

	public void setProductDeliveries(LinkedHashMap<Product, Integer> productDeliveries) {
		this.productDeliveries = productDeliveries;
	}

}
