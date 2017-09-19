package com.kiroule.vaadin.demo.ui.view.dashboard;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;

public class BoardLabel extends Label {

	private String header;
	private String content;

	public BoardLabel(String header, String content) {
		super("", ContentMode.HTML);
		addStyleName("board-box-label");
		setSizeFull();
		setHeader(header);
		setContent(content);
	}

	public BoardLabel(String header, String content, String styleName) {
		this(header, content);
		addStyleName(styleName);
	}

	private void setHeader(String header) {
		this.header = header;
		updateValue();
	}

	public void setContent(String content) {
		this.content = content;
		updateValue();
	}

	private void updateValue() {
		setValue("<h1>" + content + "</h1>" //
				+ "<h4>" + header + "</h4>");
	}

}
