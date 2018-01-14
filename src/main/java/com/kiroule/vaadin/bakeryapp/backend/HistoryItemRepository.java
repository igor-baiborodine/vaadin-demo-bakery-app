package com.kiroule.vaadin.bakeryapp.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kiroule.vaadin.bakeryapp.backend.data.entity.HistoryItem;

public interface HistoryItemRepository extends JpaRepository<HistoryItem, Long> {
}
