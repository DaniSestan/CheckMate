package com.danisestan.checkmate.TestAutomation;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.danisestan.checkmate.Utils.Resources;
import com.danisestan.checkmate.uiActions.RegistrationPage;

public class Keywords extends Resources{
	
	public static String Navigate() {
		System.out.println("Navigate is called");
		driver.get(webElement);		
		tsDetails = "";
		return "Pass";
	}

	public static String selectRadioButton() {
		System.out.println("InputText is called");
		try {
			getWebElement(webElement).click();
		}catch (Throwable t) {
			tsDetails = "Failed - Element not found \"+webElement";
			return "Failed";
		}
		tsDetails = ""; 		return "Pass";
	}
	

	
	public static String InputText() {
		System.out.println("InputText is called");
		try {
			getWebElement(webElement).sendKeys(TestData);
		}catch (Throwable t) {
			tsDetails = "Failed - Element not found "+webElement;
			return "Failed";
		}
		tsDetails = ""; 		return "Pass";
	}
	
	
	
	public static String ClickOnLink() {
		System.out.println("ClickOnLink is called");
		try {
			getWebElement(webElement).click();
		}catch (Throwable t) {
			t.printStackTrace();
			tsDetails = "Failed - Element not found "+webElement;
			return "Failed";
		}
		tsDetails = ""; 		return "Pass";
	}

	public static String VerifyText() {
		System.out.println("VerifyText is called");
		try {
			String ActualText= getWebElement(webElement).getText();
			System.out.println(ActualText);
			if(!ActualText.equals(TestData)) {
				tsDetails = "Failed - Actual text "+ActualText+" is not equal to to expected text "+TestData;
				return "Failed";
			}
		}catch (Throwable t) {
			tsDetails = "Failed - Element not found "+webElement;
			return "Failed";
		}
		tsDetails = ""; 		return "Pass";
	}

	public static String VerifyAppText() {
		System.out.println("VerifyText is called");
		try {
			String ActualText= getWebElement(webElement).getText();
			if(!ActualText.equals(AppText.getProperty(webElement))) {
				tsDetails = "Failed - Actual text "+ActualText+" is not equal to to expected text "+AppText.getProperty
						(webElement);
				return "Failed";			}
		}catch (Throwable t) {
			tsDetails = "Failed - Element not found "+webElement;
			return "Failed";
		}
		tsDetails = ""; 		return "Pass";
	}
	
	
   /**
    * This Method will return web element.
    * @param locator
    * @return
    * @throws Exception
    */
	public static WebElement getLocator(String locator) throws Exception {
        String[] split = locator.split(":");
		String locatorType = split[0];
		String locatorValue = split[1];

		if (locatorType.toLowerCase().equals("id"))
			return driver.findElement(By.id(locatorValue));
		else if (locatorType.toLowerCase().equals("name"))
			return driver.findElement(By.name(locatorValue));
		else if ((locatorType.toLowerCase().equals("classname"))
				|| (locatorType.toLowerCase().equals("class")))
			return driver.findElement(By.className(locatorValue));
		else if ((locatorType.toLowerCase().equals("tagname"))
				|| (locatorType.toLowerCase().equals("tag")))
			return driver.findElement(By.className(locatorValue));
		else if ((locatorType.toLowerCase().equals("linktext"))
				|| (locatorType.toLowerCase().equals("link")))
			return driver.findElement(By.linkText(locatorValue));
		else if (locatorType.toLowerCase().equals("partiallinktext"))
			return driver.findElement(By.partialLinkText(locatorValue));
		else if ((locatorType.toLowerCase().equals("cssselector"))
				|| (locatorType.toLowerCase().equals("css")))
			return driver.findElement(By.cssSelector(locatorValue));
		else if (locatorType.toLowerCase().equals("xpath"))
			return driver.findElement(By.xpath(locatorValue));
		else
			throw new Exception("Unknown locator type '" + locatorType + "'");
	}
	
	public static List<WebElement> getLocators(String locator) throws Exception {
        String[] split = locator.split(":");
		String locatorType = split[0];
		String locatorValue = split[1];

		if (locatorType.toLowerCase().equals("id"))
			return driver.findElements(By.id(locatorValue));
		else if (locatorType.toLowerCase().equals("name"))
			return driver.findElements(By.name(locatorValue));
		else if ((locatorType.toLowerCase().equals("classname"))
				|| (locatorType.toLowerCase().equals("class")))
			return driver.findElements(By.className(locatorValue));
		else if ((locatorType.toLowerCase().equals("tagname"))
				|| (locatorType.toLowerCase().equals("tag")))
			return driver.findElements(By.className(locatorValue));
		else if ((locatorType.toLowerCase().equals("linktext"))
				|| (locatorType.toLowerCase().equals("link")))
			return driver.findElements(By.linkText(locatorValue));
		else if (locatorType.toLowerCase().equals("partiallinktext"))
			return driver.findElements(By.partialLinkText(locatorValue));
		else if ((locatorType.toLowerCase().equals("cssselector"))
				|| (locatorType.toLowerCase().equals("css")))
			return driver.findElements(By.cssSelector(locatorValue));
		else if (locatorType.toLowerCase().equals("xpath"))
			return driver.findElements(By.xpath(locatorValue));
		else
			throw new Exception("Unknown locator type '" + locatorType + "'");
	}
	
	public static WebElement getWebElement(String locator) throws Exception{
		System.out.println("locator data:-"+locator+"is---"+Repository.getProperty(locator));
		return getLocator(Repository.getProperty(locator));
	}
	
	public static List<WebElement> getWebElements(String locator) throws Exception{
		return getLocators(Repository.getProperty(locator));
	}
	
	public static String explicitWait() throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.visibilityOf(getWebElement(webElement)));
		tsDetails = ""; 		return "Pass";
	}
	

	public static String clickWhenReady(By locator, int timeout) {
		WebElement element = null;
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		element = wait.until(ExpectedConditions.elementToBeClickable(locator));
		element.click();
		tsDetails = ""; 		return "Pass";

	}


	
	public static String waitFor() throws InterruptedException {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			tsDetails = "Failed - unable to load the page";
			return "Failed";
		}
		tsDetails = "";
		return "Pass";
	}
	
	public static String selectDaysInDropDown() throws Exception{
		RegistrationPage reg = new RegistrationPage();
		String status = reg.selectDaysInDropDown();
		return status;
	}
	
	public static String selectMonthInDropDown() throws Exception{
		RegistrationPage reg = new RegistrationPage();
		return reg.selectMonthInDropDown();
	}
	
	public static String selectYearInDropDown() throws Exception{
		RegistrationPage reg = new RegistrationPage();
		return reg.selectYearInDropDown();
	}
	
	public static String selectYourAddressCountry() throws Exception{
		RegistrationPage reg = new RegistrationPage();
		return reg.selectYourAddressCountry();
	}
	
	public static void closeBrowser(){
		driver.quit();
	}
	
}
