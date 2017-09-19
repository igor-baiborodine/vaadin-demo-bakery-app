package com.kiroule.vaadin.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.LoggerFactory;

import com.kiroule.vaadin.demo.ui.view.LoginViewElement;
import com.kiroule.vaadin.demo.ui.view.dashboard.DashboardViewElement;
import com.kiroule.vaadin.demo.ui.view.storefront.StorefrontViewElement;
import com.vaadin.testbench.By;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.HasDriver;
import com.vaadin.testbench.HasTestBenchCommandExecutor;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchDriverProxy;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.commands.TestBenchCommandExecutor;
import com.vaadin.testbench.elements.AbstractComponentElement;
import com.vaadin.testbench.elements.GridElement;
import com.vaadin.testbench.elements.GridElement.GridCellElement;
import com.vaadin.testbench.elements.GridElement.GridRowElement;
import com.vaadin.testbench.elementsbase.AbstractElement;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class AbstractIT extends TestBenchTestCase {

	public static final String APP_URL = "http://localhost:8080/";

	static {
		// Prevent debug logging from Apache HTTP client
		Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.setLevel(Level.INFO);
	}
	@Rule
	public ScreenshotOnFailureRule screenshotOnFailure = new ScreenshotOnFailureRule(this, true);

	@Before
	public void setup() {
		setDriver(createDriver());
		getDriver().resizeViewPortTo(800, 600);
	}

	protected WebDriver createDriver() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		options.addArguments("--disable-gpu");
		return TestBench.createDriver(new ChromeDriver(options));
	}

	@Override
	public TestBenchDriverProxy getDriver() {
		return (TestBenchDriverProxy) super.getDriver();
	}

	protected static boolean hasAttribute(TestBenchElement element, String name) {
		return internalGetAttribute(element, name) != null;
	}

	protected static Object internalGetAttribute(TestBenchElement element, String name) {
		return element.getCommandExecutor().executeScript("return arguments[0].getAttribute(arguments[1]);", element,
				name);
	}

	protected static void assertEnabledWithCaption(String text, AbstractComponentElement element) {
		assertEnabled(true, element);
		Assert.assertEquals(text, element.getCaption());
	}

	/**
	 * Assert that the element looked up by the given function is not in the
	 * DOM. If it is not, an {@link AssertionError} is thrown with the given
	 * message.
	 *
	 * @param message
	 *            the message for the {@link AssertionError}
	 * @param elementSupplier
	 *            the function which returns the element
	 */
	protected void assertNotFound(String message, Supplier<WebElement> elementSupplier) {
		try {
			elementSupplier.get();
			Assert.fail("Element");
		} catch (NoSuchElementException e) {
			// Everything ok
		}

	}

	public static <T extends AbstractElement> T findFirstElement(HasDriver hasDriver, Class<T> elementType) {
		return new ElementQuery<>(elementType).context(hasDriver.getDriver()).first();
	}

	protected static void assertEnabled(boolean expectedEnabled, TestBenchElement element) {
		if (expectedEnabled) {
			if (hasAttribute(element, "disabled")) {
				throw new AssertionError("Expected element to be enabled but it has a 'disabled' attribute");
			}
			if (hasClassName(element, "v-disabled")) {
				throw new AssertionError("Expected element to be enabled but it has a 'v-disabled' class");
			}
		} else {
			if (!hasAttribute(element, "disabled") && !hasClassName(element, "v-disabled")) {
				throw new AssertionError(
						"Expected element to be disabled but it does not have a 'disabled' attribute nor a 'v-disabled' class");
			}

		}
	}

	/**
	 * Checks if the given element has the given class name.
	 *
	 * @param element
	 *            the element to check
	 * @param className
	 *            the class name to check for
	 * @return <code>true</code> if the element has the given class name,
	 *         <code>false</code> otherwise
	 */
	protected static boolean hasClassName(TestBenchElement element, String className) {
		return element.getClassNames().contains(className);
	}

	/**
	 * Gets all visible cell contents from the given grid.
	 *
	 * @param grid
	 *            the grid to check
	 * @return text contents of all cells in the grid
	 */
	public static List<String[]> getData(GridElement grid) {
		int cols = getColumnCount(grid);
		ArrayList<String[]> ret = new ArrayList<>();
		for (GridRowElement row : grid.getRows()) {
			String[] rowData = new String[cols];
			for (int i = 0; i < cols; i++) {
				rowData[i] = row.getCell(i).getText();
			}
			ret.add(rowData);
		}
		return ret;
	}

	/**
	 * Gets the number of columns shown in the grid.
	 * <p>
	 * Assumes that the grid contains at least one row.
	 *
	 * @param grid
	 *            the grid to query
	 * @return the number of columns in the grid
	 */
	public static int getColumnCount(GridElement grid) {
		return grid.getRow(0).findElements(By.xpath("./td")).size();
	}

	/**
	 * Finds the cell with the given content and returns it.
	 *
	 * @param grid
	 *            the grid to search through
	 * @param contents
	 *            the contents to look for
	 * @return the first cell with a matching content
	 * @throws NoSuchElementException
	 *             if no cell was found
	 */
	public static TestBenchElement getCell(GridElement grid, String contents) throws NoSuchElementException {
		int columns = getColumnCount(grid);
		for (GridRowElement row : grid.getRows()) {
			for (int i = 0; i < columns; i++) {
				GridCellElement cell = row.getCell(i);
				if (contents.equals(cell.getText())) {
					return cell;
				}
			}
		}

		throw new NoSuchElementException("No cell with text '" + contents + "' found");
	}

	protected String getViewParameter() {
		String url = getDriver().getCurrentUrl();
		if (url.contains("#")) {
			String fragment = url.substring(url.indexOf("#") + 1);
			if (fragment.contains("/")) {
				String params = fragment.substring(fragment.indexOf("/") + 1);
				return params;
			}
		}
		return "";
	}

	protected StorefrontViewElement loginAsBarista() {
		openLoginView(APP_URL).login("barista@vaadin.com", "barista");
		return $(StorefrontViewElement.class).first();
	}

	protected DashboardViewElement loginAsAdmin() {
		openLoginView(APP_URL).login("admin@vaadin.com", "admin");
		return $(DashboardViewElement.class).first();
	}

	protected LoginViewElement openLoginView(String url) {
		return openLoginView(getDriver(), url);
	}

	protected LoginViewElement openLoginView(WebDriver driver, String url) {
		driver.get(url);
		TestBenchElement body = (TestBenchElement) driver.findElement(By.tagName("body"));
		TestBenchCommandExecutor executor = ((HasTestBenchCommandExecutor) driver).getCommandExecutor();
		return TestBench.createElement(LoginViewElement.class, body.getWrappedElement(), executor);
	}

}
