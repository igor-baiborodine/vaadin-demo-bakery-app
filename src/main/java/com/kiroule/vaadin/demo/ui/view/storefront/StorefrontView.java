package com.kiroule.vaadin.demo.ui.view.storefront;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.annotation.ViewScope;
import com.kiroule.vaadin.demo.backend.data.entity.Order;
import com.kiroule.vaadin.demo.ui.navigation.NavigationManager;
import com.kiroule.vaadin.demo.ui.view.orderedit.OrderEditView;
import com.vaadin.ui.Button.ClickShortcut;

/**
 * The storefront view showing upcoming orders.
 * <p>
 * Created as a single View class because the logic is so simple that using a
 * pattern like MVP would add much overhead for little gain. If more complexity
 * is added to the class, you should consider splitting out a presenter.
 */
@SpringView
public class StorefrontView extends StorefrontViewDesign implements View {

	private static final String PARAMETER_SEARCH = "search";

	private static final String PARAMETER_INCLUDE_PAST = "includePast";

	private final NavigationManager navigationManager;

	@Autowired
	public StorefrontView(NavigationManager navigationManager) {
		this.navigationManager = navigationManager;
	}

	/**
	 * This method is invoked once each time an instance of the view is created.
	 * <p>
	 * This typically happens whenever a user opens the URL for the view, or
	 * refreshes the browser as long as the view is set to {@link ViewScope}. If
	 * we set the view to {@link UIScope}, the instance will be kept in memory
	 * (in the session) as long as the UI exists in memory and the same view
	 * instance will be reused whenever the user enters the view.
	 * <p>
	 * Here we set up listeners and attach data providers and otherwise
	 * configure the components for the parts which only need to be done once.
	 */
	@PostConstruct
	public void init() {
		list.addSelectionListener(e -> selectedOrder(e.getFirstSelectedItem().get()));
		newOrder.addClickListener(e -> newOrder());
		searchButton.addClickListener(e -> search(searchField.getValue(), includePast.getValue()));

		// We don't want a global shortcut for enter, scope it to the panel
		searchPanel.addAction(new ClickShortcut(searchButton, KeyCode.ENTER, null));
	}

	public void selectedOrder(Order order) {
		navigationManager.navigateTo(OrderEditView.class, order.getId());
	}

	public void newOrder() {
		navigationManager.navigateTo(OrderEditView.class);
	}

	public void search(String searchTerm, boolean includePast) {
		filterGrid(searchTerm, includePast);
		String parameters = PARAMETER_SEARCH + "=" + searchTerm;
		if (includePast) {
			parameters += "&" + PARAMETER_INCLUDE_PAST;
		}
		navigationManager.updateViewParameter(parameters);
	}

	/**
	 * This is called whenever the user enters the view, regardless of if the
	 * view instance was created right before this or if an old instance was
	 * reused.
	 * <p>
	 * Here we update the data shown in the view so the user sees the latest
	 * changes.
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		Map<String, String> params = event.getParameterMap();
		String searchTerm = params.getOrDefault(PARAMETER_SEARCH, "");
		boolean includePast = params.containsKey(PARAMETER_INCLUDE_PAST);
		filterGrid(searchTerm, includePast);
	}

	public void filterGrid(String searchTerm, boolean includePast) {
		list.filterGrid(searchTerm, includePast);
		searchField.setValue(searchTerm);
		this.includePast.setValue(includePast);
	}
}
