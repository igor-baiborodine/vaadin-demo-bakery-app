package com.kiroule.vaadin.demo.ui.view.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;

import com.kiroule.vaadin.demo.AbstractIT;
import com.kiroule.vaadin.demo.ui.components.ConfirmationDialogDesignElement;
import com.kiroule.vaadin.demo.ui.view.MenuElement;
import com.kiroule.vaadin.demo.ui.view.admin.product.CrudViewElement;
import com.kiroule.vaadin.demo.ui.view.storefront.StorefrontViewElement;
import com.vaadin.testbench.elements.GridElement;
import com.vaadin.testbench.elements.GridElement.GridRowElement;
import com.vaadin.testbench.elements.TextFieldElement;

public abstract class AbstractCrudIT<T extends CrudViewElement> extends AbstractIT {

	protected abstract void assertFormFieldsEmpty(T view);

	protected abstract void populateNewEntity(T view);

	protected abstract TextFieldElement getFirstFormTextField(T view);

	protected abstract String getViewName();

	protected abstract T getViewElement();

	protected T loginAndNavigateToView() {
		loginAsAdmin();
		MenuElement menu = $(MenuElement.class).first();
		menu.getMenuLink(getViewName()).click();
		return getViewElement();
	}

	protected static void assertData(List<String[]> expected, List<String[]> data) {
		Assert.assertEquals(expected.size(), data.size());
		for (int i = 0; i < expected.size(); i++) {
			String[] expectedRow = expected.get(i);
			String[] actualRow = data.get(i);

			Assert.assertArrayEquals(expectedRow, actualRow);
		}
	}

	protected static void sort(List<String[]> currentData, int i, boolean reverse) {
		Collections.sort(currentData, (o1, o2) -> {
			if (!reverse) {
				return o1[i].compareTo(o2[i]);
			} else {
				return o2[i].compareTo(o1[i]);
			}
		});

	}

	protected static List<String[]> filter(List<String[]> haystack, String needle) {
		List<String[]> matches = new ArrayList<>();
		for (String[] data : haystack) {
			if (anyContains(data, needle)) {
				matches.add(data);
			}
		}
		return matches;
	}

	protected static boolean anyContains(String[] data, String needle) {
		for (int i = 0; i < data.length; i++) {
			if (data[i].toLowerCase().contains(needle.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	protected void assertInitialState(T view) {

		// Grid enabled, contains data but has nothing selected
		GridElement grid = view.getList();
		assertEnabled(true, grid);
		Assert.assertTrue(grid.getRowCount() > 0);
		for (GridRowElement row : grid.getRows()) {
			Assert.assertFalse(row.isSelected());
		}

		// Form and buttons are disabled
		assertEnabled(false, view.getForm());
		assertEnabled(false, view.getUpdate());
		assertEnabled(false, view.getCancel());
		assertEnabled(false, view.getDelete());

		assertFormFieldsEmpty(view);
		Assert.assertEquals(AbstractCrudView.CAPTION_UPDATE, view.getUpdate().getText());
		Assert.assertEquals(AbstractCrudView.CAPTION_DISCARD, view.getCancel().getText());

		assertViewParameter("");
	}

	private void assertViewParameter(String expected) {
		Assert.assertEquals(expected, getViewParameter());
	}

	protected void assertEditState(T view, boolean hasChanges) {
		// Grid enabled, contains data and has one row selected
		GridElement grid = view.getList();
		assertEnabled(true, grid);
		Assert.assertTrue(grid.getRowCount() > 0);
		int selectedCount = 0;
		for (GridRowElement row : grid.getRows()) {
			if (row.isSelected()) {
				selectedCount++;
			}
		}
		Assert.assertEquals(1, selectedCount);

		// Form and delete are always enabled
		assertEnabled(true, view.getForm());
		assertEnabled(true, view.getDelete());

		assertEnabled(hasChanges, view.getUpdate());
		assertEnabled(hasChanges, view.getCancel());

		Assert.assertEquals(AbstractCrudView.CAPTION_UPDATE, view.getUpdate().getText());
		Assert.assertEquals(AbstractCrudView.CAPTION_DISCARD, view.getCancel().getText());

		String viewParam = getViewParameter();
		Assert.assertTrue(Integer.parseInt(viewParam) > 0);
	}

	private void assertAddState(T view, boolean hasChanges) {
		// Grid enabled, contains data but has nothing selected
		GridElement grid = view.getList();
		assertEnabled(true, grid);
		Assert.assertTrue(grid.getRowCount() > 0);
		for (GridRowElement row : grid.getRows()) {
			Assert.assertFalse(row.isSelected());
		}

		// Form is always enabled, delete is always disabled
		assertEnabled(true, view.getForm());
		assertEnabled(false, view.getDelete());

		assertEnabled(hasChanges, view.getUpdate());
		assertEnabled(hasChanges, view.getCancel());

		Assert.assertEquals("Add", view.getUpdate().getText());
		Assert.assertEquals("Cancel", view.getCancel().getText());

		Assert.assertEquals("new", getViewParameter());
	}

	@Test
	public void sortGrid() {
		T view = loginAndNavigateToView();
		GridElement grid = view.getList();
		List<String[]> currentData = getData(grid);

		List<Integer> testableColumns = getUniquelySortableColumnIndexes(view);

		for (Integer i : testableColumns) {
			sort(currentData, i, false);
			grid.getHeaderCell(0, i).click();
			assertData(currentData, getData(grid));

			sort(currentData, i, true);
			grid.getHeaderCell(0, i).click();
			assertData(currentData, getData(grid));
		}
	}

	protected List<Integer> getUniquelySortableColumnIndexes(T view) {
		int columnCount = getColumnCount(view.getList());
		return IntStream.range(0, columnCount).mapToObj(i -> i).collect(Collectors.toList());
	}

	@Test
	public void filterGrid() {
		T view = loginAndNavigateToView();
		GridElement grid = view.getList();
		List<String[]> currentData = getData(grid);

		view.getSearch().setValue("bak");
		List<String[]> shouldMatch = filter(currentData, "bak");
		Assert.assertEquals(shouldMatch.size(), grid.getRowCount());

		view.getSearch().setValue("ba");
		shouldMatch = filter(currentData, "ba");
		Assert.assertEquals(shouldMatch.size(), grid.getRowCount());

		view.getSearch().setValue("a");
		shouldMatch = filter(currentData, "a");
		Assert.assertEquals(shouldMatch.size(), grid.getRowCount());
	}

	@Test
	public void initialState() {
		T view = loginAndNavigateToView();
		assertInitialState(view);
	}

	@Test
	public void createEntityButCancel() {
		T view = loginAndNavigateToView();
		view.getAdd().click();
		assertAddState(view, false);
		populateNewEntity(view);
		view.getCancel().click();
		assertInitialState(view);
	}

	@Test
	public void createAndDeleteEntity() {
		T view = loginAndNavigateToView();
		view.getAdd().click();
		assertAddState(view, false);
		populateNewEntity(view);
		assertAddState(view, true);
		view.getUpdate().click();
		assertEditState(view, false);

		TextFieldElement field = getFirstFormTextField(view);
		String newValue = field.getValue() + "-updated";
		field.setValue(newValue);
		assertEditState(view, true);
		view.getUpdate().click();
		assertEditState(view, false);
		Assert.assertEquals(newValue, field.getValue());

		view.getDelete().click();
		assertInitialState(view);
	}

	@Test
	public void updateEntity() {
		T view = loginAndNavigateToView();
		/* Choose second row. In user view first is locked and
		   can't be modified. */

		view.getList().getCell(1, 0).click();
		assertEditState(view, false);

		TextFieldElement field = getFirstFormTextField(view);
		String oldValue = field.getValue();
		String newValue = oldValue + "-updated";
		field.setValue(newValue);
		assertEditState(view, true);
		view.getUpdate().click();
		assertEditState(view, false);
		Assert.assertEquals(newValue, field.getValue());
		field.setValue(oldValue);
		assertEditState(view, true);
		view.getUpdate().click();
		assertEditState(view, false);
	}


	@Test
	public void confirmationDialogShownWhenAboutToLoseData() {
		T view = loginAndNavigateToView();

		// Open some entity and change something
		view.getList().getCell(0, 0).click();
		TextFieldElement field = getFirstFormTextField(view);
		String oldValue = field.getValue();
		String newValue = oldValue + "-updated";
		field.setValue(newValue);

		// Navigate away and check that we did not move away and cancel the
		// confirmation dialog

		// Navigate away to another view
		$(MenuElement.class).first().getMenuLink("Storefront").click();
		Assert.assertTrue(view.isDisplayed());
		$(ConfirmationDialogDesignElement.class).first().getCancel().click();

		// Logout
		$(MenuElement.class).first().logout();
		Assert.assertTrue(view.isDisplayed());
		$(ConfirmationDialogDesignElement.class).first().getCancel().click();

		// Create a new entity
		view.getAdd().click();
		assertEditState(view, true);
		$(ConfirmationDialogDesignElement.class).first().getCancel().click();

		// Select another entity
		view.getList().getCell(1, 0).click();
		assertEditState(view, true);
		$(ConfirmationDialogDesignElement.class).first().getCancel().click();
		Assert.assertFalse(view.getList().getRow(1).isSelected());
		// Assert.assertTrue(view.getList().getRow(0).isSelected());
	}

	@Test
	public void confirmationDialogCanBeDismissed() {
		T view = loginAndNavigateToView();

		// Open some entity and change something
		view.getList().getCell(0, 0).click();
		TextFieldElement field = getFirstFormTextField(view);
		String oldValue = field.getValue();
		String newValue = oldValue + "-updated";
		field.setValue(newValue);

		// Navigate away and check that we can actually move away
		$(MenuElement.class).first().getMenuLink("Storefront").click();
		$(ConfirmationDialogDesignElement.class).first().getDiscardChanges().click();
		Assert.assertNotNull($(StorefrontViewElement.class).first());
	}

	@Test
	public void enterEditUsingParameter() {
		T view = loginAndNavigateToView();
		String url = getDriver().getCurrentUrl();
		view.getList().getCell(0, 0).click();
		String firstIdUrl = getDriver().getCurrentUrl();
		String firstId = firstIdUrl.substring(firstIdUrl.lastIndexOf("/") + 1);

		// Select the second row (to avoid having the first already selected),
		// open the url and first row should be selected
		view.getList().getCell(1, 0).click();
		getDriver().get(url + "/" + firstId);

		T viewElement = getViewElement();
		TextFieldElement firstField = getFirstFormTextField(viewElement);
		Assert.assertNotEquals("", firstField.getValue());
		firstField.setValue(firstField.getValue() + "_upd");
		viewElement.getUpdate().click();
		assertEditState(viewElement, false);
	}

}
