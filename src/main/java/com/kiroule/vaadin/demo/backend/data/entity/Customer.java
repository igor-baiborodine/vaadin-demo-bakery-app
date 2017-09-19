package com.kiroule.vaadin.demo.backend.data.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Customer extends AbstractEntity {

	@NotNull
	@Size(min = 1, max = 255)
	private String fullName;

	@NotNull
	@Size(min = 1, max = 255)
	private String phoneNumber;

	@Size(max = 255)
	private String details;

	public Customer() {
		// Empty constructor is needed by Spring Data / JPA
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
