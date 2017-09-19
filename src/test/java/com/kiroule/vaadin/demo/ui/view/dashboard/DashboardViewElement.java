package com.kiroule.vaadin.demo.ui.view.dashboard;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.vaadin.testbench.elements.AbstractComponentElement;
import com.vaadin.testbench.elements.CssLayoutElement;
import com.vaadin.testbench.elements.GridElement;
import com.vaadin.testbench.elementsbase.ServerClass;

public class DashboardViewElement extends DashboardViewDesignElement {

	@ServerClass("com.kiroule.vaadin.demo.ui.view.dashboard.BoardBox")
	public static class BoardBoxElement extends CssLayoutElement {

		public String getHeader() {
			return findElement(By.tagName("h4")).getText();
		}

		public String getContent() {
			return findElement(By.tagName("h1")).getText();
		}
	}

	@ServerClass("com.vaadin.addon.charts.Chart")
	public static class ChartElement extends AbstractComponentElement {

		public String getTitle() {
			return findElement(By.className("highcharts-title")).getText();
		}

		public boolean hasData() {
			return findElements(By.className("highcharts-no-data")).isEmpty();
		}

		public List<WebElement> getSeries() {
			return findElements(By.className("highcharts-series"));
		}
	}

	public List<BoardBoxElement> getBoxes() {
		return $(BoardBoxElement.class).all();
	}

	public BoardBoxElement getTodayBox() {
		return $(BoardBoxElement.class).get(0);
	}

	public BoardBoxElement getNotAvailableBox() {
		return $(BoardBoxElement.class).get(1);
	}

	public BoardBoxElement getNewBox() {
		return $(BoardBoxElement.class).get(2);
	}

	public BoardBoxElement getTomorrowBox() {
		return $(BoardBoxElement.class).get(3);
	}

	public ChartElement getDeliveriesThisMonth() {
		return $(ChartElement.class).id("deliveriesThisMonth");
	}

	public ChartElement getDeliveriesThisYear() {
		return $(ChartElement.class).id("deliveriesThisYear");
	}

	public ChartElement getYearlySales() {
		return $(ChartElement.class).id("yearlySales");
	}

	public ChartElement getMonthlyProductSplit() {
		return $(ChartElement.class).id("monthlyProductSplit");
	}

	public GridElement getGrid() {
		return $(GridElement.class).id("dueGrid");
	}

}