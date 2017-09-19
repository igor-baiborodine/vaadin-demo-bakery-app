package com.kiroule.vaadin.demo.ui.view.orderedit;

import org.openqa.selenium.NoSuchElementException;

import com.vaadin.testbench.elementsbase.ServerClass;

@ServerClass("com.kiroule.vaadin.demo.ui.view.orderedit.ProductInfo")
public class ProductInfoElement extends ProductInfoDesignElement {

	public static class ProductOrderData {
		private String product;
		private String comment;
		private int quantity;
		private int price;

		public ProductOrderData() {

		}

		public ProductOrderData(String product, int quantity, String comment) {
			this.product = product;
			this.comment = comment;
			this.quantity = quantity;
		}

		public String getProduct() {
			return product;
		}

		public void setProduct(String product) {
			this.product = product;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}

		public int getPrice() {
			return price;
		}

		public void setPrice(int price) {
			this.price = price;
		}

		public void setPrice(double price) {
			setPrice((int) (100 * price));
		}

	}

	public void setProduct(ProductOrderData productOrderData) {
		getProduct().selectByText(productOrderData.getProduct());
		getQuantity().setValue(String.valueOf(productOrderData.getQuantity()));
		getComment().setValue(productOrderData.getComment());
	}

	public ProductOrderData getProductOrderData() {
		ProductOrderData productOrderData = new ProductOrderData();
		productOrderData.setProduct(getProduct().getValue());
		productOrderData.setQuantity(Integer.parseInt(getQuantity().getValue()));
		try {
			productOrderData.setComment(getComment().getValue());
		} catch (NoSuchElementException e) {
			try {
				productOrderData.setComment(getReportModeComment().getText());
			} catch (NoSuchElementException e2) {
				// Hidden in report mode if there is no comment
				productOrderData.setComment("");
			}
		}
		int intPrice = (Integer.parseInt(getPrice().getText().replace("$", "").replace(".", "")));
		productOrderData.setPrice(intPrice);

		return productOrderData;
	}
}