package com.kiroule.vaadin.bakeryapp.backend.repositories;

import com.kiroule.vaadin.bakeryapp.backend.data.entity.HistoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryItemRepository extends JpaRepository<HistoryItem, Long> {
}
