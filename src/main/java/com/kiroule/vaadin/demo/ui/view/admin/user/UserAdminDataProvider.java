package com.kiroule.vaadin.demo.ui.view.admin.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;
import org.vaadin.spring.annotation.PrototypeScope;

import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringComponent;
import com.kiroule.vaadin.demo.backend.data.entity.User;
import com.kiroule.vaadin.demo.backend.service.UserService;

@SpringComponent
@PrototypeScope
public class UserAdminDataProvider extends FilterablePageableDataProvider<User, Object> {

	private final UserService userService;

	@Autowired
	public UserAdminDataProvider(UserService userService) {
		this.userService = userService;
	}

	@Override
	protected Page<User> fetchFromBackEnd(Query<User, Object> query, Pageable pageable) {
		return userService.findAnyMatching(getOptionalFilter(), pageable);
	}

	@Override
	protected int sizeInBackEnd(Query<User, Object> query) {
		return (int) userService.countAnyMatching(getOptionalFilter());
	}

	@Override
	protected List<QuerySortOrder> getDefaultSortOrders() {
		List<QuerySortOrder> sortOrders = new ArrayList<>();
		sortOrders.add(new QuerySortOrder("email", SortDirection.ASCENDING));
		return sortOrders;
	}

}