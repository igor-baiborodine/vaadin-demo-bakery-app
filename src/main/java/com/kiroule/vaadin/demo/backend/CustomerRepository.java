package com.kiroule.vaadin.demo.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kiroule.vaadin.demo.backend.data.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
