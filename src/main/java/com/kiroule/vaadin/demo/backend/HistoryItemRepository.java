package com.kiroule.vaadin.demo.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kiroule.vaadin.demo.backend.data.entity.HistoryItem;

public interface HistoryItemRepository extends JpaRepository<HistoryItem, Long> {
}
