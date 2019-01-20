package com.kiroule.vaadin.bakeryapp.backend.repositories;

import com.kiroule.vaadin.bakeryapp.backend.data.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
