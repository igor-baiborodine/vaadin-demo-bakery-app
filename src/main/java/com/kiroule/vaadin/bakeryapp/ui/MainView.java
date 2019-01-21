package com.kiroule.vaadin.bakeryapp.ui;

import static com.kiroule.vaadin.bakeryapp.ui.utils.BakeryConst.PAGE_DASHBOARD;
import static com.kiroule.vaadin.bakeryapp.ui.utils.BakeryConst.PAGE_PRODUCTS;
import static com.kiroule.vaadin.bakeryapp.ui.utils.BakeryConst.PAGE_STOREFRONT;
import static com.kiroule.vaadin.bakeryapp.ui.utils.BakeryConst.PAGE_USERS;
import static com.kiroule.vaadin.bakeryapp.ui.utils.BakeryConst.TITLE_DASHBOARD;
import static com.kiroule.vaadin.bakeryapp.ui.utils.BakeryConst.TITLE_LOGOUT;
import static com.kiroule.vaadin.bakeryapp.ui.utils.BakeryConst.TITLE_PRODUCTS;
import static com.kiroule.vaadin.bakeryapp.ui.utils.BakeryConst.TITLE_STOREFRONT;
import static com.kiroule.vaadin.bakeryapp.ui.utils.BakeryConst.TITLE_USERS;
import static com.kiroule.vaadin.bakeryapp.ui.utils.BakeryConst.VIEWPORT;

import com.kiroule.vaadin.bakeryapp.app.security.SecurityUtils;
import com.kiroule.vaadin.bakeryapp.ui.components.BakeryCookieConsent;
import com.kiroule.vaadin.bakeryapp.ui.components.OfflineBanner;
import com.kiroule.vaadin.bakeryapp.ui.exceptions.AccessDeniedException;
import com.kiroule.vaadin.bakeryapp.ui.views.HasConfirmation;
import com.kiroule.vaadin.bakeryapp.ui.views.admin.products.ProductsView;
import com.kiroule.vaadin.bakeryapp.ui.views.admin.users.UsersView;
import com.kiroule.vaadin.bakeryapp.ui.views.login.LoginView;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AbstractAppRouterLayout;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.server.PWA;


@Viewport(VIEWPORT)
@PWA(name = "Bakery App Starter", shortName = "vaadin-demo-bakery-app",
		startPath = "login",
		backgroundColor = "#227aef", themeColor = "#227aef",
		offlinePath = "offline-page.html",
		offlineResources = {"images/offline-login-banner.jpg"})
public class MainView extends AbstractAppRouterLayout implements BeforeEnterObserver {

	private final ConfirmDialog confirmDialog;

	public MainView() {
		this.confirmDialog = new ConfirmDialog();
		confirmDialog.setCancelable(true);
		confirmDialog.setConfirmButtonTheme("raised tertiary error");
		confirmDialog.setCancelButtonTheme("raised tertiary");

		getElement().appendChild(confirmDialog.getElement());
		getElement().appendChild(new BakeryCookieConsent().getElement());
		getElement().addAttachListener(e -> UI.getCurrent().add(new OfflineBanner()));
	}

	@Override
	protected void configure(AppLayout appLayout, AppLayoutMenu menu) {
		appLayout.setBranding(new Span("vaadin-demo-bakery-app"));

		if (SecurityUtils.isUserLoggedIn()) {
			setMenuItem(menu, new AppLayoutMenuItem(VaadinIcon.EDIT.create(), TITLE_STOREFRONT, PAGE_STOREFRONT));
			setMenuItem(menu, new AppLayoutMenuItem(VaadinIcon.CLOCK.create(), TITLE_DASHBOARD, PAGE_DASHBOARD));

			if (SecurityUtils.isAccessGranted(UsersView.class)) {
				setMenuItem(menu, new AppLayoutMenuItem(VaadinIcon.USER.create(), TITLE_USERS, PAGE_USERS));
			}
			if (SecurityUtils.isAccessGranted(ProductsView.class)) {
				setMenuItem(menu, new AppLayoutMenuItem(VaadinIcon.CALENDAR.create(), TITLE_PRODUCTS, PAGE_PRODUCTS));
			}

			setMenuItem(menu, new AppLayoutMenuItem(VaadinIcon.ARROW_RIGHT.create(), TITLE_LOGOUT, e ->
					UI.getCurrent().getPage().executeJavaScript("location.assign('logout')")));
		}
		getElement().addEventListener("search-focus", e -> {
			appLayout.getElement().getClassList().add("hide-navbar");
		});

		getElement().addEventListener("search-blur", e -> {
			appLayout.getElement().getClassList().remove("hide-navbar");
		});
	}

	private void setMenuItem(AppLayoutMenu menu, AppLayoutMenuItem menuItem) {
		menuItem.getElement().setAttribute("theme", "icon-on-top");
		menu.addMenuItem(menuItem);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		final boolean accessGranted =
			SecurityUtils.isAccessGranted(event.getNavigationTarget());
		if (!accessGranted) {
			if (SecurityUtils.isUserLoggedIn()) {
				event.rerouteToError(AccessDeniedException.class);
			}
			else {
				event.rerouteTo(LoginView.class);
			}
		}
	}

	@Override
	public void showRouterLayoutContent(HasElement content) {
		super.showRouterLayoutContent(content);

		this.confirmDialog.setOpened(false);
		if (content instanceof HasConfirmation) {
			((HasConfirmation) content).setConfirmDialog(this.confirmDialog);
		}
	}
}
