package com.kiroule.vaadin.demo.ui.components;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;
import org.vaadin.spring.annotation.PrototypeScope;

import com.vaadin.spring.annotation.SpringComponent;
import com.kiroule.vaadin.demo.backend.data.entity.Customer;
import com.kiroule.vaadin.demo.backend.data.entity.Order;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.HtmlRenderer;

@SpringComponent
@PrototypeScope
public class OrdersGrid extends Grid<Order> {

	@Autowired
	private OrdersDataProvider dataProvider;

	public OrdersGrid() {
		addStyleName("orders-grid");
		setSizeFull();
		removeHeaderRow(0);

		// Add stylenames to rows
		setStyleGenerator(OrdersGrid::getRowStyle);

		// Due column
		Column<Order, String> dueColumn = addColumn(
				order -> twoRowCell(getTimeHeader(order.getDueDate()), String.valueOf(order.getDueTime())),
				new HtmlRenderer());
		dueColumn.setSortProperty("dueDate", "dueTime");
		dueColumn.setStyleGenerator(order -> "due");

		// Summary column
		Column<Order, String> summaryColumn = addColumn(order -> {
			Customer customer = order.getCustomer();
			return twoRowCell(customer.getFullName(), getOrderSummary(order));
		}, new HtmlRenderer()).setExpandRatio(1).setSortProperty("customer.fullName").setMinimumWidthFromContent(false);
		summaryColumn.setStyleGenerator(order -> "summary");
	}

	public void filterGrid(String searchTerm, boolean includePast) {
		dataProvider.setFilter(searchTerm);
		dataProvider.setIncludePast(includePast);
	}

	@PostConstruct
	protected void init() {
		setDataProvider(dataProvider);
	}

	/**
	 * Makes date into a more readable form; "Today", "Mon 7", "12 Jun"
	 * 
	 * @param dueDate
	 *            The date to make into a string
	 * @return A formatted string depending on how far in the future the date
	 *         is.
	 */
	private static String getTimeHeader(LocalDate dueDate) {
		LocalDate today = LocalDate.now();
		if (dueDate.isEqual(today)) {
			return "Today";
		} else {
			// Show weekday for upcoming days
			LocalDate todayNextWeek = today.plusDays(7);
			if (dueDate.isAfter(today) && dueDate.isBefore(todayNextWeek)) {
				// "Mon 7"
				return dueDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.US) + " "
						+ dueDate.getDayOfMonth();
			} else {
				// In the past or more than a week in the future
				return dueDate.getDayOfMonth() + " " + dueDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.US);
			}
		}
	}

	private static String getRowStyle(Order order) {
		String style = order.getState().name().toLowerCase();

		long days = LocalDate.now().until(order.getDueDate(), ChronoUnit.DAYS);
		if (days == 0) {
			style += " today";
		} else if (days == 1) {
			style += " tomorrow";
		}

		return style;
	}

	private static String getOrderSummary(Order order) {
		Stream<String> quantityAndName = order.getItems().stream()
				.map(item -> item.getQuantity() + "x " + item.getProduct().getName());
		return quantityAndName.collect(Collectors.joining(", "));
	}

	private static String twoRowCell(String header, String content) {
		return "<div class=\"header\">" + HtmlUtils.htmlEscape(header) + "</div><div class=\"content\">"
				+ HtmlUtils.htmlEscape(content) + "</div>";
	}

}
