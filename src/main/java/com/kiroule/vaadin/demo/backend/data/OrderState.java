package com.kiroule.vaadin.demo.backend.data;

import com.vaadin.shared.util.SharedUtil;

public enum OrderState {
	NEW, CONFIRMED, READY, DELIVERED, PROBLEM, CANCELLED;

	/**
	 * Gets a version of the enum identifier in a human friendly format.
	 *
	 * @return a human friendly version of the identifier
	 */
	public String getDisplayName() {
		return SharedUtil.upperCaseUnderscoreToHumanFriendly(name());
	}

	/**
	 * Gets a enum value for which {@link #getDisplayName()} returns the given
	 * string. Match is case-insensitive.
	 *
	 * @return the enum value with a matching display name
	 */
	public static OrderState forDisplayName(String displayName) {
		for (OrderState state : values()) {
			if (displayName.toLowerCase().equals(state.getDisplayName().toLowerCase())) {
				return state;
			}
		}
		return null;
	}
}
