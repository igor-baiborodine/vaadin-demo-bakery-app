package com.kiroule.vaadin.demo.ui.view.dashboard;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.Labels;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.Marker;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.PlotOptionsLine;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.board.Row;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.kiroule.vaadin.demo.backend.data.DashboardData;
import com.kiroule.vaadin.demo.backend.data.DeliveryStats;
import com.kiroule.vaadin.demo.backend.data.entity.Order;
import com.kiroule.vaadin.demo.backend.data.entity.Product;
import com.kiroule.vaadin.demo.backend.service.OrderService;
import com.kiroule.vaadin.demo.ui.components.OrdersGrid;
import com.kiroule.vaadin.demo.ui.navigation.NavigationManager;
import com.kiroule.vaadin.demo.ui.view.orderedit.OrderEditView;

/**
 * The dashboard view showing statistics about sales and deliveries.
 * <p>
 * Created as a single View class because the logic is so simple that using a
 * pattern like MVP would add much overhead for little gain. If more complexity
 * is added to the class, you should consider splitting out a presenter.
 */
@SpringView
public class DashboardView extends DashboardViewDesign implements View {

	private static final String DELIVERIES = "Deliveries";

	private static final String BOARD_ROW_PANELS = "board-row-panels";

	private final NavigationManager navigationManager;
	private final OrderService orderService;

	private final BoardLabel todayLabel = new BoardLabel("Today", "3/7", "today");
	private final BoardLabel notAvailableLabel = new BoardLabel("N/A", "1", "na");
	private final BoardBox notAvailableBox = new BoardBox(notAvailableLabel);
	private final BoardLabel newLabel = new BoardLabel("New", "2", "new");
	private final BoardLabel tomorrowLabel = new BoardLabel("Tomorrow", "4", "tomorrow");

	private final Chart deliveriesThisMonthGraph = new Chart(ChartType.COLUMN);
	private final Chart deliveriesThisYearGraph = new Chart(ChartType.COLUMN);
	private final Chart yearlySalesGraph = new Chart(ChartType.AREA);
	private final Chart monthlyProductSplit = new Chart(ChartType.PIE);
	private final OrdersGrid dueGrid;

	private ListSeries deliveriesThisMonthSeries;
	private ListSeries deliveriesThisYearSeries;
	private ListSeries[] salesPerYear;

	private DataSeries deliveriesPerProductSeries;

	@Autowired
	public DashboardView(NavigationManager navigationManager, OrderService orderService, OrdersGrid dueGrid) {
		this.navigationManager = navigationManager;
		this.orderService = orderService;
		this.dueGrid = dueGrid;
	}

	@PostConstruct
	public void init() {
		setResponsive(true);

		Row row = board.addRow(new BoardBox(todayLabel), notAvailableBox, new BoardBox(newLabel),
				new BoardBox(tomorrowLabel));
		row.addStyleName("board-row-group");

		row = board.addRow(new BoardBox(deliveriesThisMonthGraph), new BoardBox(deliveriesThisYearGraph));
		row.addStyleName(BOARD_ROW_PANELS);

		row = board.addRow(new BoardBox(yearlySalesGraph));
		row.addStyleName(BOARD_ROW_PANELS);

		row = board.addRow(new BoardBox(monthlyProductSplit), new BoardBox(dueGrid, "due-grid"));
		row.addStyleName(BOARD_ROW_PANELS);

		initDeliveriesGraphs();
		initProductSplitMonthlyGraph();
		initYearlySalesGraph();

		dueGrid.setId("dueGrid");
		dueGrid.setSizeFull();

		dueGrid.addSelectionListener(e -> selectedOrder(e.getFirstSelectedItem().get()));
	}

	private void initYearlySalesGraph() {
		yearlySalesGraph.setId("yearlySales");
		yearlySalesGraph.setSizeFull();
		int year = Year.now().getValue();

		Configuration conf = yearlySalesGraph.getConfiguration();
		conf.setTitle("Sales last years");
		conf.getxAxis().setCategories(getMonthNames());
		conf.getChart().setMarginBottom(6);

		PlotOptionsLine options = new PlotOptionsLine();
		options.setMarker(new Marker(false));
		options.setShadow(true);
		conf.setPlotOptions(options);

		salesPerYear = new ListSeries[3];
		for (int i = 0; i < salesPerYear.length; i++) {
			salesPerYear[i] = new ListSeries(Integer.toString(year - i));
			salesPerYear[i].setPlotOptions(new PlotOptionsLineWithZIndex(year - i));
			conf.addSeries(salesPerYear[i]);
		}
		conf.getyAxis().setTitle("");

	}

	private void initProductSplitMonthlyGraph() {
		monthlyProductSplit.setId("monthlyProductSplit");
		monthlyProductSplit.setSizeFull();

		LocalDate today = LocalDate.now();

		Configuration conf = monthlyProductSplit.getConfiguration();
		String thisMonth = today.getMonth().getDisplayName(TextStyle.FULL, Locale.US);
		conf.setTitle("Products delivered in " + thisMonth);
		deliveriesPerProductSeries = new DataSeries(DELIVERIES);
		conf.addSeries(deliveriesPerProductSeries);

		conf.getyAxis().setTitle("");

	}

	private void initDeliveriesGraphs() {
		LocalDate today = LocalDate.now();

		deliveriesThisMonthGraph.setId("deliveriesThisMonth");
		deliveriesThisMonthGraph.setSizeFull();

		deliveriesThisYearGraph.setId("deliveriesThisYear");
		deliveriesThisYearGraph.setSizeFull();

		Configuration yearConf = deliveriesThisYearGraph.getConfiguration();

		yearConf.setTitle("Deliveries in " + today.getYear());
		yearConf.getChart().setMarginBottom(6);
		yearConf.getxAxis().setCategories(getMonthNames());
		yearConf.getxAxis().setLabels(new Labels(null));
		yearConf.getLegend().setEnabled(false);
		deliveriesThisYearSeries = new ListSeries(DELIVERIES);
		yearConf.addSeries(deliveriesThisYearSeries);
		configureColumnSeries(deliveriesThisYearSeries);

		Configuration monthConf = deliveriesThisMonthGraph.getConfiguration();
		String thisMonth = today.getMonth().getDisplayName(TextStyle.FULL, Locale.US);
		monthConf.setTitle("Deliveries in " + thisMonth);
		monthConf.getChart().setMarginBottom(6);
		monthConf.getLegend().setEnabled(false);
		deliveriesThisMonthSeries = new ListSeries(DELIVERIES);
		monthConf.addSeries(deliveriesThisMonthSeries);
		configureColumnSeries(deliveriesThisMonthSeries);

		int daysInMonth = YearMonth.of(today.getYear(), today.getMonthValue()).lengthOfMonth();
		String[] categories = IntStream.rangeClosed(1, daysInMonth).mapToObj(Integer::toString)
				.toArray(size -> new String[size]);
		monthConf.getxAxis().setCategories(categories);
		monthConf.getxAxis().setLabels(new Labels(false));
	}

	protected void configureColumnSeries(ListSeries series) {
		PlotOptionsColumn options = new PlotOptionsColumn();
		options.setBorderWidth(1);
		options.setGroupPadding(0);
		series.setPlotOptions(options);

		YAxis yaxis = series.getConfiguration().getyAxis();
		yaxis.setGridLineWidth(0);
		yaxis.setLabels(new Labels(false));
		yaxis.setTitle("");
	}

	private String[] getMonthNames() {
		return Stream.of(Month.values()).map(month -> month.getDisplayName(TextStyle.SHORT, Locale.US))
				.toArray(size -> new String[size]);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		DashboardData data = fetchData();
		updateLabels(data.getDeliveryStats());
		updateGraphs(data);
	}

	private DashboardData fetchData() {
		return orderService.getDashboardData(MonthDay.now().getMonthValue(), Year.now().getValue());
	}

	private void updateGraphs(DashboardData data) {
		deliveriesThisMonthSeries.setData(data.getDeliveriesThisMonth());
		deliveriesThisYearSeries.setData(data.getDeliveriesThisYear());

		for (int i = 0; i < 3; i++) {
			salesPerYear[i].setData(data.getSalesPerMonth(i));
		}

		for (Entry<Product, Integer> entry : data.getProductDeliveries().entrySet()) {
			deliveriesPerProductSeries.add(new DataSeriesItem(entry.getKey().getName(), entry.getValue()));
		}
	}

	private void updateLabels(DeliveryStats deliveryStats) {
		todayLabel.setContent(deliveryStats.getDeliveredToday() + "/" + deliveryStats.getDueToday());
		notAvailableLabel.setContent(Integer.toString(deliveryStats.getNotAvailableToday()));
		notAvailableBox.setNeedsAttention(deliveryStats.getNotAvailableToday() > 0);
		newLabel.setContent(Integer.toString(deliveryStats.getNewOrders()));
		tomorrowLabel.setContent(Integer.toString(deliveryStats.getDueTomorrow()));
	}

	/**
	 * Extends {@link PlotOptionsLine} to support zIndex. Omits getter/setter,
	 * since they are not needed in our case.
	 *
	 */
	private static class PlotOptionsLineWithZIndex extends PlotOptionsLine {
		@SuppressWarnings("unused")
		private Number zIndex;

		public PlotOptionsLineWithZIndex(Number zIndex) {
			this.zIndex = zIndex;
		};
	}

	public void selectedOrder(Order order) {
		navigationManager.navigateTo(OrderEditView.class, order.getId());
	}

}
