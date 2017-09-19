package com.kiroule.vaadin.demo.ui.view.dashboard;

import java.time.Year;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.kiroule.vaadin.demo.AbstractIT;
import com.kiroule.vaadin.demo.ui.view.dashboard.DashboardViewElement.BoardBoxElement;
import com.kiroule.vaadin.demo.ui.view.dashboard.DashboardViewElement.ChartElement;
import com.vaadin.testbench.By;

public class DashboardIT extends AbstractIT {

	// Test does not depend heavily on data, instead assert that all values make
	// sense and not everything is zeroes

	@Test
	public void containsCharts() {
		DashboardViewElement dashboardView = loginAsAdmin();
		ChartElement deliveriesThisMonth = dashboardView.getDeliveriesThisMonth();
		String thisMonth = YearMonth.now().getMonth().getDisplayName(TextStyle.FULL, Locale.US);
		Assert.assertEquals(("Deliveries in " + thisMonth).toUpperCase(), deliveriesThisMonth.getTitle().toUpperCase());
		Assert.assertEquals(1, deliveriesThisMonth.getSeries().size());
		Assert.assertTrue(deliveriesThisMonth.hasData());

		ChartElement deliveriesThisYear = dashboardView.getDeliveriesThisYear();
		Assert.assertEquals(("Deliveries in " + Year.now().getValue()).toUpperCase(),
				deliveriesThisYear.getTitle().toUpperCase());
		Assert.assertEquals(1, deliveriesThisYear.getSeries().size());
		Assert.assertTrue(deliveriesThisYear.hasData());

		ChartElement salesLastYears = dashboardView.getYearlySales();
		Assert.assertEquals(3, salesLastYears.getSeries().size());
		Assert.assertTrue(deliveriesThisYear.hasData());

		ChartElement productSplit = dashboardView.getMonthlyProductSplit();
		List<WebElement> series = productSplit.getSeries();
		Assert.assertEquals(1, series.size());
		Assert.assertTrue("With the generated data, more than 3 products should have been sold last month",
				series.get(0).findElements(By.tagName("path")).size() > 3);
	}

	@Test
	public void boxesContainData() {
		DashboardViewElement dashboardView = loginAsAdmin();
		List<BoardBoxElement> boxes = dashboardView.getBoxes();
		Assert.assertEquals(9, boxes.size()); // "widgets" in dashboard

		BoardBoxElement todayBox = dashboardView.getTodayBox();
		String[] todayValues = todayBox.getContent().split("/", 2);
		int deliveredToday = Integer.parseInt(todayValues[0]);
		int totalToday = Integer.parseInt(todayValues[1]);

		Assert.assertTrue("With the generated data, there should be at least one delivery", totalToday > 0);
		Assert.assertTrue("Delivered cannot be larger than total", deliveredToday <= totalToday);

		BoardBoxElement naBox = dashboardView.getNotAvailableBox();
		int naToday = Integer.parseInt(naBox.getContent());
		Assert.assertTrue("N/A data cannot be negative", naToday >= 0);

		BoardBoxElement newBox = dashboardView.getNewBox();
		int nrNew = Integer.parseInt(newBox.getContent());
		Assert.assertTrue("With the generated data, there should be more than one new order", nrNew > 1);

		BoardBoxElement tomorrowBox = dashboardView.getTomorrowBox();
		int nrTomorrow = Integer.parseInt(tomorrowBox.getContent());
		Assert.assertTrue("With the generated data, there should be at least one order tomorrow", nrTomorrow > 0);
	}

	@Test
	public void gridContainsData() {
		DashboardViewElement dashboardView = loginAsAdmin();
		Assert.assertTrue("With the generated data, there should be at least ten rows in the grid",
				dashboardView.getGrid().getRowCount() > 10);
	}
}
