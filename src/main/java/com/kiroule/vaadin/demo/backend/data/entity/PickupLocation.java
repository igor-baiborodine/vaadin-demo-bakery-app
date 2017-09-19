package com.kiroule.vaadin.demo.backend.data.entity;

import javax.persistence.Entity;
import javax.validation.constraints.Size;

@Entity
public class PickupLocation extends AbstractEntity {

	@Size(max = 255)
	private String name;

	public PickupLocation() {
		// Empty constructor is needed by Spring Data / JPA
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
