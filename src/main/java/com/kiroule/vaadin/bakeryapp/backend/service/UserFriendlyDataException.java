package com.kiroule.vaadin.bakeryapp.backend.service;

import org.springframework.dao.DataIntegrityViolationException;

/**
 * A data integraty violation exception containing a message intended to be
 * shown to the end user.
 */
public class UserFriendlyDataException extends DataIntegrityViolationException {

	public UserFriendlyDataException(String message) {
		super(message);
	}

}
