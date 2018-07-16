package com.kiroule.vaadin.bakeryapp.ui.views.dashboard;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

public class BoardBox extends CssLayout {

	private CssLayout mainDiv = new CssLayout();

	public BoardBox(Component component) {
		// An extra wrapper is here because of the IE11 flex box issue
		// https://github.com/philipwalton/flexbugs#7-flex-basis-doesnt-account-for-box-sizingborder-box
		addStyleName("board-box-wrapper");
		setSizeFull();
		addComponent(mainDiv);
		mainDiv.addStyleName("board-box");
		mainDiv.setSizeFull();
		CssLayout inner = new CssLayout();
		inner.setSizeFull();
		inner.addStyleName("board-box-inner");
		inner.addComponent(component);
		mainDiv.addComponent(inner);
	}

	public BoardBox(Component component, String styleName) {
		this(component);
		mainDiv.addStyleName(styleName);
	}

	public void setNeedsAttention(boolean needsAttention) {
		mainDiv.setStyleName("board-box-needs-attention", needsAttention);
	}
}
