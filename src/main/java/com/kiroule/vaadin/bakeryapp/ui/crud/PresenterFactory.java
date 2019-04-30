/**
 *
 */
package com.kiroule.vaadin.bakeryapp.ui.crud;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.kiroule.vaadin.bakeryapp.app.security.CurrentUser;
import com.kiroule.vaadin.bakeryapp.backend.data.entity.Order;
import com.kiroule.vaadin.bakeryapp.backend.service.OrderService;
import com.kiroule.vaadin.bakeryapp.ui.views.storefront.StorefrontView;

@Configuration
public class PresenterFactory {

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public EntityPresenter<Order, StorefrontView> orderEntityPresenter(OrderService crudService, CurrentUser currentUser) {
		return new EntityPresenter<>(crudService, currentUser);
	}

}
