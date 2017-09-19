package com.kiroule.vaadin.demo.ui.view.orderedit;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;

import com.vaadin.testbench.By;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.CssLayoutElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elementsbase.ServerClass;

@ServerClass("com.kiroule.vaadin.demo.ui.view.orderedit.OrderHistory")
public class OrderHistoryElement extends OrderHistoryDesignElement {

	private TextFieldElement getCommentInput() {
		return $(TextFieldElement.class).id("newCommentInput");
	}

	private ButtonElement getSaveComment() {
		return $(ButtonElement.class).id("commitNewComment");
	}

	public List<OrderHistoryItemObject> getHistoryItems() {
		List<OrderHistoryItemObject> history = new ArrayList<>();
		CssLayoutElement layout = getItems();
		layout.scrollIntoView();

		List<WebElement> labels = layout.findElements(By.className("v-label"));
		List<WebElement> captions = layout.findElements(By.className("v-captiontext"));

		for (int i = 0; i < labels.size(); i++) {
			String c = captions.get(i).getText();
			int by = c.indexOf(" by ");
			String date = c.substring(0, by);
			String author = c.substring(by + 4);
			String message = labels.get(i).getText();
			history.add(new OrderHistoryItemObject(date, message, author));
		}
		return history;
	}

	public void addComment(String message) {
		TextFieldElement input = getCommentInput();
		input.setValue(message);

		getSaveComment().click();
	}

}