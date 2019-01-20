package com.kiroule.vaadin.bakeryapp.app.security;

import com.kiroule.vaadin.bakeryapp.backend.data.entity.User;

@FunctionalInterface
public interface CurrentUser {

	User getUser();
}
