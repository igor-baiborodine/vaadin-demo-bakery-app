package com.kiroule.vaadin.demo.ui.view.dashboard;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

public class BoardBox extends CssLayout {

	public BoardBox(Component component) {
		addStyleName("board-box");
		setSizeFull();
		CssLayout inner = new CssLayout();
		inner.setSizeFull();
		inner.addStyleName("board-box-inner");
		inner.addComponent(component);
		addComponent(inner);
	}

	public BoardBox(Component component, String styleName) {
		this(component);
		addStyleName(styleName);
	}

	public void setNeedsAttention(boolean needsAttention) {
		setStyleName("board-box-needs-attention", needsAttention);
	}
}
