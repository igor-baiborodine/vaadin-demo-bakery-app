package com.kiroule.vaadin.bakeryapp.ui.components;

import com.kiroule.vaadin.bakeryapp.ui.entities.PageInfo;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.IronIcon;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.templatemodel.TemplateModel;
import java.util.ArrayList;
import java.util.List;

@Tag("app-navigation")
@HtmlImport("src/components/app-navigation.html")
public class AppNavigation extends PolymerTemplate<TemplateModel> implements AfterNavigationObserver {

	@Id("tabs")
	private Tabs tabs;

	private List<String> hrefs = new ArrayList<>();
	private String logoutHref;
	private String defaultHref;
	private String currentHref;

	public void init(List<PageInfo> pages, String defaultHref, String logoutHref) {
		this.logoutHref = logoutHref;
		this.defaultHref = defaultHref;

		for (PageInfo page : pages) {
			Tab tab = new Tab(new IronIcon("vaadin", page.getIcon()), new Span(page.getTitle()));
			tab.getElement().setAttribute("theme", "icon-on-top");
			hrefs.add(page.getLink());
			tabs.add(tab);
		}

		tabs.addSelectedChangeListener(e -> navigate());
	}

	private void navigate() {
		int idx = tabs.getSelectedIndex();
		if (idx >= 0 && idx < hrefs.size()) {
			String href = hrefs.get(idx);
			if (href.equals(logoutHref)) {
				// The logout button is a 'normal' URL, not Flow-managed but
				// handled by Spring Security.
				UI.getCurrent().getPage().executeJavaScript("location.assign('logout')");
			} else if (!href.equals(currentHref)) {
				UI.getCurrent().navigate(href);
			}
		}
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		String href = event.getLocation().getFirstSegment().isEmpty() ? defaultHref
				: event.getLocation().getFirstSegment();
		currentHref = href;
		tabs.setSelectedIndex(hrefs.indexOf(href));
	}
}
