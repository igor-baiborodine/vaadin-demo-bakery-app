package com.kiroule.vaadin.bakeryapp.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kiroule.vaadin.bakeryapp.backend.data.entity.HistoryItem;

public interface HistoryItemRepository extends JpaRepository<HistoryItem, Long> {
}
