package com.kiroule.vaadin.demo.backend.data.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.kiroule.vaadin.demo.backend.data.OrderState;

@Entity
public class HistoryItem extends AbstractEntity {

	private OrderState newState;

	@NotNull
	@Size(min = 1, max = 255)
	private String message;

	@NotNull
	private LocalDateTime timestamp;
	@NotNull
	@OneToOne
	private User createdBy;

	protected HistoryItem() {
		// Empty constructor is needed by Spring Data / JPA
	}

	public HistoryItem(User createdBy, String message) {
		this.createdBy = createdBy;
		this.message = message;
		timestamp = LocalDateTime.now();
	}

	public OrderState getNewState() {
		return newState;
	}

	public void setNewState(OrderState newState) {
		this.newState = newState;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

}
