package com.kiroule.vaadin.bakeryapp.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kiroule.vaadin.bakeryapp.backend.data.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
