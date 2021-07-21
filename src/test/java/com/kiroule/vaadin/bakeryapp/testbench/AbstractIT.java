package com.kiroule.vaadin.bakeryapp.testbench;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.kiroule.vaadin.bakeryapp.testbench.elements.ui.LoginViewElement;
import com.kiroule.vaadin.bakeryapp.ui.utils.BakeryConst;
import com.vaadin.testbench.IPAddress;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBenchDriverProxy;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.parallel.ParallelTest;

import io.github.bonigarcia.wdm.WebDriverManager;

public abstract class AbstractIT<E extends TestBenchElement> extends ParallelTest {
	public String APP_URL = "http://localhost:8080/";

	static {
		// Let notifications persist longer during tests
		BakeryConst.NOTIFICATION_DURATION = 10000;
	}

	@Rule
	public ScreenshotOnFailureRule screenshotOnFailure = new ScreenshotOnFailureRule(this, true);

	@Override
	public void setup() throws Exception {
		super.setup();
		if (getRunLocallyBrowser() == null) {
			APP_URL = "http://" + IPAddress.findSiteLocalAddress() + ":8080/";
		}
	}

	@BeforeClass
	public static void setupClass() {
		WebDriverManager.chromedriver().setup();
	}

	@Override
	public TestBenchDriverProxy getDriver() {
		return (TestBenchDriverProxy) super.getDriver();
	}

	@Override
	public void setDesiredCapabilities(DesiredCapabilities desiredCapabilities) {
		// Disable interactivity check in Firefox https://github.com/mozilla/geckodriver/#mozwebdriverclick
		if (desiredCapabilities.getBrowserName().equals(BrowserType.FIREFOX)) {
			desiredCapabilities.setCapability("moz:webdriverClick", false);
		}

		super.setDesiredCapabilities(desiredCapabilities);
	}

	protected LoginViewElement openLoginView() {
		return openLoginView(getDriver(), APP_URL);
	}

	protected LoginViewElement openLoginView(WebDriver driver, String url) {
		driver.get(url);
		return $(LoginViewElement.class).waitForFirst();
	}

	protected abstract E openView();

}
