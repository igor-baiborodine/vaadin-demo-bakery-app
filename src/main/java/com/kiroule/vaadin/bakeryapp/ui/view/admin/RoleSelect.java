package com.kiroule.vaadin.bakeryapp.ui.view.admin;

import com.kiroule.vaadin.bakeryapp.backend.data.Role;
import com.vaadin.ui.ComboBox;

public class RoleSelect extends ComboBox<String> {

	public RoleSelect() {
		setCaption("Role");
		setEmptySelectionAllowed(false);
		setItems(Role.getAllRoles());
		setTextInputAllowed(false);
	}
}
