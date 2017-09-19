package com.kiroule.vaadin.demo.ui.view.admin.user;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.kiroule.vaadin.demo.ui.view.admin.AbstractCrudIT;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.NotificationElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class UserAdminIT extends AbstractCrudIT<UserAdminViewElement> {

	private static final String MODIFY_LOCKED_USER_NOT_PERMITTED = "User has been locked and cannot be modified or deleted";

	@Override
	protected String getViewName() {
		return "Users";
	}

	@Override
	protected UserAdminViewElement getViewElement() {
		return $(UserAdminViewElement.class).first();
	}

	@Override
	protected void populateNewEntity(UserAdminViewElement view) {
		view.getEmail().setValue("john@doe.com");
		view.getName().setValue("John doe");
		view.getPassword().setValue("john");
		view.getRole().selectByText("admin");
	}

	@Override
	protected TextFieldElement getFirstFormTextField(UserAdminViewElement view) {
		return view.getName();
	}

	@Override
	protected List<Integer> getUniquelySortableColumnIndexes(UserAdminViewElement view) {
		List<Integer> cols = super.getUniquelySortableColumnIndexes(view);
		cols.remove(2); // Role sorting is not stable
		return cols;
	}

	@Test
	public void updatePassword() {
		UserAdminViewElement userAdmin = loginAndNavigateToView();
		// Change the password for baker to foo and back to baker
		TestBenchElement bakerCell = getCell(userAdmin.getList(), "baker@vaadin.com");
		bakerCell.click();

		TextFieldElement passwordField = userAdmin.getPassword();
		Assert.assertEquals("", passwordField.getValue());

		// Too short password
		passwordField.setValue("foo");
		ButtonElement update = userAdmin.getUpdate();
		update.click();
		Assert.assertTrue(passwordField.hasClassName("v-textfield-error"));
		passwordField.setValue("foobar");
		update.click();
		assertEditState(userAdmin, false);

		bakerCell.click();
		Assert.assertEquals("", passwordField.getValue());
		passwordField.setValue("baker");
		update.click();

		assertEditState(userAdmin, false);
	}

	@Override
	protected void assertFormFieldsEmpty(UserAdminViewElement view) {
		Assert.assertEquals("", view.getEmail().getText());
		Assert.assertEquals("", view.getName().getText());
		Assert.assertEquals("", view.getPassword().getText());
		Assert.assertEquals("", view.getRole().getValue());
	}

	@Test
	public void passwordRequiredForNewUser() {
		UserAdminViewElement userAdmin = loginAndNavigateToView();
		userAdmin.getAdd().click();
		userAdmin.getEmail().setValue("foo");
		userAdmin.getName().setValue("bar");
		ButtonElement update = userAdmin.getUpdate();
		update.click();
		Assert.assertTrue(userAdmin.getPassword().hasClassName("v-textfield-error"));
	}

	@Test
	public void tryToUpdateLockedEntity() {
		UserAdminViewElement view = loginAndNavigateToView();
		view.getList().getCell(0, 0).click();
		assertEditState(view, false);

		TextFieldElement field = getFirstFormTextField(view);
		String oldValue = field.getValue();
		String newValue = oldValue + "-updated";
		field.setValue(newValue);
		assertEditState(view, true);
		view.getUpdate().click();

		Assert.assertEquals(MODIFY_LOCKED_USER_NOT_PERMITTED, $(NotificationElement.class).first().getCaption());
	}

	@Test
	public void tryToDeleteLockedEntity() {
		UserAdminViewElement view = loginAndNavigateToView();
		view.getSearch().setValue("admin");
		view.getList().getCell(0, 0).click();
		assertEditState(view, false);
		view.getDelete().click();
		Assert.assertEquals(MODIFY_LOCKED_USER_NOT_PERMITTED, $(NotificationElement.class).first().getCaption());
	}
}
