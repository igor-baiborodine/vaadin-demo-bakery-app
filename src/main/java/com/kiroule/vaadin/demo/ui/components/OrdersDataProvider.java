package com.kiroule.vaadin.demo.ui.components;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;
import org.vaadin.spring.annotation.PrototypeScope;

import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringComponent;
import com.kiroule.vaadin.demo.backend.data.entity.Order;
import com.kiroule.vaadin.demo.backend.service.OrderService;

@SpringComponent
@PrototypeScope
public class OrdersDataProvider extends FilterablePageableDataProvider<Order, Object> {

	private final OrderService orderService;
	private LocalDate filterDate = LocalDate.now().minusDays(1);

	@Autowired
	public OrdersDataProvider(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	protected Page<Order> fetchFromBackEnd(Query<Order, Object> query, Pageable pageable) {
		return orderService.findAnyMatchingAfterDueDate(getOptionalFilter(), getOptionalFilterDate(), pageable);
	}

	private Optional<LocalDate> getOptionalFilterDate() {
		if (filterDate == null) {
			return Optional.empty();
		} else {
			return Optional.of(filterDate);
		}
	}

	public void setIncludePast(boolean includePast) {
		if (includePast) {
			filterDate = null;
		} else {
			filterDate = LocalDate.now().minusDays(1);
		}
	}

	@Override
	protected int sizeInBackEnd(Query<Order, Object> query) {
		return (int) orderService.countAnyMatchingAfterDueDate(getOptionalFilter(), getOptionalFilterDate());
	}

	@Override
	protected List<QuerySortOrder> getDefaultSortOrders() {
		List<QuerySortOrder> sortOrders = new ArrayList<>();
		sortOrders.add(new QuerySortOrder("dueDate", SortDirection.ASCENDING));
		sortOrders.add(new QuerySortOrder("dueTime", SortDirection.ASCENDING));
		// id included only to always get a stable sort order
		sortOrders.add(new QuerySortOrder("id", SortDirection.DESCENDING));
		return sortOrders;
	}
}
