package com.kiroule.vaadin.demo.backend.data.entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class OrderItem extends AbstractEntity {

	@OneToOne
	@NotNull
	private Product product;
	@Min(1)
	@Max(1000)
	private int quantity = 1;
	@Size(max = 255)
	private String comment;

	public OrderItem() {
		// Empty constructor is needed by Spring Data / JPA
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
