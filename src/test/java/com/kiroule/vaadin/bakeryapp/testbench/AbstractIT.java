package com.kiroule.vaadin.bakeryapp.testbench;

import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.cookieconsent.CookieConsent;
import com.vaadin.flow.component.cookieconsent.testbench.CookieConsentElement;
import com.kiroule.vaadin.bakeryapp.testbench.elements.ui.LoginViewElement;
import com.kiroule.vaadin.bakeryapp.testbench.elements.ui.MainViewElement;
import com.kiroule.vaadin.bakeryapp.ui.utils.BakeryConst;
import com.vaadin.testbench.IPAddress;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBenchDriverProxy;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.parallel.ParallelTest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public abstract class AbstractIT<E extends TestBenchElement> extends ParallelTest {
	public String APP_URL = "http://localhost:8080/";

	static {
		// Prevent debug logging from Apache HTTP client
		Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.setLevel(Level.INFO);
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

	@Test
	public void shouldShowCookieConsent() {
		openView();
		final MainViewElement mainView = $(MainViewElement.class).first();
		final List<CookieConsentElement> cookieConsentElements =
			mainView.$(CookieConsentElement.class).all();
		Assert.assertEquals(1, cookieConsentElements.size());
		final CookieConsentElement cookieConsentElement =
			cookieConsentElements.get(0);
		Assert.assertEquals(
			CookieConsentElement.DefaultValues.MESSAGE,
			cookieConsentElement.getMessage());
		Assert.assertEquals(CookieConsent.Position.BOTTOM,
			cookieConsentElement.getPosition());
	}

}
