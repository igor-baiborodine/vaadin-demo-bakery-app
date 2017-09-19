package com.kiroule.vaadin.demo.ui.view;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.vaadin.testbench.TestBenchElement;

public class LoginViewElement extends TestBenchElement {

	public void login(String username, String password) {
		WebElement loginElement = getLogin();
		WebElement passwordElement = getPassword();
		loginElement.clear();
		loginElement.sendKeys(username);
		passwordElement.clear();
		passwordElement.sendKeys(password);

		getSubmit().click();

		waitUntilElementPresent(By.className("navigation-bar"));
	}

	protected void waitUntilElementPresent(By by) {
		new WebDriverWait(getDriver(), 30).until(ExpectedConditions.presenceOfElementLocated(by));
	}

	private WebElement getSubmit() {
		return findElement(By.id("button-submit"));
	}

	private WebElement getPassword() {
		return findElement(By.id("password"));
	}

	private WebElement getLogin() {
		return findElement(By.id("login"));
	}

}
