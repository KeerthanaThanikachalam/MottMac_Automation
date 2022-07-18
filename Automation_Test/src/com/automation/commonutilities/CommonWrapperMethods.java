package com.automation.commonutilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automation.reports.Reports;

public class CommonWrapperMethods extends Reports {
	public static WebDriver driver;
	public static String Browser, Application_Name, URL, Scenario_Name, Test_Sheet_Path, refTestDataName;
	protected static Properties config, prop;
	public static int Default_Wait_4_Page;

	// Constructor to load configuration properties
	public CommonWrapperMethods() {
		// Loading the configuration properties file
		config = new Properties();
		try {
			config.load(new FileInputStream(new File("./config.properties")));
		} catch (FileNotFoundException e) {
			System.err.println("'config.properties' file load Error. Please check the file exist/name of the file");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("'config.properties' file load Error. Please check the Input data of the file");
			e.printStackTrace();
		}
	}

// **************************************************************************************************
// @Method Name: launchApplication
// @Description: URL to navigate in the browser
// @Input Parameters: URL to be launched and Title to verify
// @Output Parameters: Boolean : True or False
// **************************************************************************************************
	public void launchApplication(String url, String verifyTitle) {
		try {
			// Launching the URL
			driver.get(url);
			// Verify the URL
			if (!verifyTitle.equalsIgnoreCase("")) {
				if (!driver.getTitle().equalsIgnoreCase(verifyTitle)) {
					System.out.println("Application Launch failed");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			reportStep("Application Launch Failed", "Fail", true);
		}
	}

// **************************************************************************************************
// @Method Name: loadObject
// @Description: Wrapper function - Load the properties file
// @Input Parameters: NIL
// @Output Parameters: NIL
// ***************************************************************************************************
	public void loadObject() {
		prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("./pageObjects.properties")));
		} catch (FileNotFoundException e) {
			System.err.println("'*.properties' multiple file load Error. Please check the file exist/name of the file");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("'*.properties' multiple file load Error. Please check the Input data of the file");
			e.printStackTrace();
		}
	}

// **************************************************************************************************
// @Method Name: suiteVariables
// @Description: Initialize the Global variables.
// @Input Parameters: NIL
// @Output Parameters: NIL
// ****************************************************************************************************
	public void suiteVariables() {
		// Assigning time out values
		Default_Wait_4_Page = Integer.parseInt(config.getProperty("Default_Wait_4_Page"));
		// Application Name
		Application_Name = config.getProperty("Application_Name");
		// Browser to be launched
		Browser = config.getProperty("Browser");
		// URL to be launched
		URL = config.getProperty("URL");
		// Test Case Sheet Path
		Test_Sheet_Path = config.getProperty("Test_Sheet_Path");
	}

// **************************************************************************************************
// @Method Name: launchBrowser
// @Description: Launch the Browser depending on the input
// @Input Parameters: Name of the Browser
// @Output Parameters: Boolean : True or False
// **************************************************************************************************
	public boolean launchBrowser(String browserName) {
		boolean bReturn = false;

		try {
			switch (browserName.toUpperCase()) {
			case "CHROME":
				System.out.println("Launching Chrome Browser");
				String chrome = "./" + config.getProperty("Browser_Drivers_Path") + "/chromedriver.exe";
				System.setProperty("webdriver.chrome.driver", chrome);

				// Setting up Chrome options
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("disable-infobars");
				chromeOptions.addArguments("start-maximized");
				chromeOptions.addArguments("chrome.switches", "--disable-extensions");

				// Creating the driver variable
				driver = new ChromeDriver(chromeOptions);
				driver.manage().timeouts().implicitlyWait(Default_Wait_4_Page, TimeUnit.SECONDS);
				bReturn = true;
				break;

			case "EDGE":

				System.out.println("Launching Edge Browser");
				String edge = "./" + config.getProperty("Browser_Drivers_Path") + "/msedgedriver.exe";
				System.setProperty("webdriver.edge.driver", edge);

				EdgeOptions edgeOptions = new EdgeOptions();

				// Creating the driver variable
				driver = new EdgeDriver(edgeOptions);
				driver.manage().timeouts().implicitlyWait(Default_Wait_4_Page, TimeUnit.SECONDS);
				bReturn = true;
				break;

			case "FIREFOX":

				System.out.println("Launching FireFox Browser");
				String fireFox = "./" + config.getProperty("Browser_Drivers_Path") + "/geckodriver.exe";
				System.setProperty("webdriver.gecko.driver", fireFox);

				FirefoxOptions firefoxOptions = new FirefoxOptions();

				// Creating the driver variable
				driver = new FirefoxDriver(firefoxOptions);
				driver.manage().timeouts().implicitlyWait(Default_Wait_4_Page, TimeUnit.SECONDS);
				bReturn = true;
				break;

			case "IE":

				System.out.println("Launching IE Browser");
				String IE = "./" + config.getProperty("Browser_Drivers_Path") + "/IEDriverServer.exe";
				System.setProperty("webdriver.ie.driver", IE);

				// Setting up IE browser options
				InternetExplorerOptions ieOptions = new InternetExplorerOptions();
				ieOptions.requireWindowFocus();
				ieOptions.ignoreZoomSettings();

				// Creating the driver variable
				driver = new InternetExplorerDriver(ieOptions);
				driver.manage().timeouts().implicitlyWait(Default_Wait_4_Page, TimeUnit.SECONDS);
				bReturn = true;
				break;
			}
		} catch (Exception e) {
			System.err.println("Browser driver initiation failed - Exception");
			reportStep("Browser driver initiation failed", "FAIL", false);
			e.printStackTrace();
		}
		return bReturn;
	}

// **************************************************************************************************
// @Method Name: waitForElement
// @Description: Sync handling
// @Input Parameters: driver, By object, timeout Seconds
// @Output Parameters: NIL
// *****************************************************************************************************
	public WebElement waitForElement(WebDriver driver, final By by, int timeOutInSeconds) {
		WebElement element;
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); // nullify the default timeout
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
			element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			driver.manage().timeouts().implicitlyWait(Default_Wait_4_Page, TimeUnit.SECONDS);
			return element;// return the element
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

// **************************************************************************************************
// @Method Name: verifyElementExist
// @Description: To check if the element exists
// @Input Parameters: Locator (object locator to find)
// @Output Parameters: True or False
// *************************************************************************************************
	public boolean verifyElementExist(String fieldName, By locator) {
		boolean bReturn = false;
		try {
			WebElement element = waitForElement(driver, locator, 30);
			if (element.isDisplayed() == true && element.isEnabled() == true) {
				reportStep(fieldName + " displayed as expected", "PASS", false);
				bReturn = true;
			} else {
				reportStep(fieldName + " element is not displayed", "FAIL", true);
			}
		} catch (Exception e) {
			System.out.println(fieldName + " Element not exist method - thrown Exception");
			reportStep(fieldName + " element not exist method - thrown Exception", "FAIL", true);
		}
		return bReturn;
	}

// **************************************************************************************************
// @Method Name: clickElement
// @Description: To click the element using locator
// @Input Parameters: field name and locator
// @Output Parameters: NIL
// ******************************************************************************************************
	public void clickElement(String fieldName, By locator) {
		// WebElement element = waitForElement(driver, locator, 30);
		WebElement element = driver.findElement(locator);
		try {
			flash(element);
			element.click();
			Thread.sleep(2000);
			reportStep(fieldName + " is clicked successfully", "PASS", false);
		} catch (Exception e) {
			reportStep(fieldName + " is not clicked successfully", "Fail", true);
			e.printStackTrace();
		}
	}

// **************************************************************************************************
// @Method Name: sendKeys
// @Description: Enter values in text box, list, etc.
// @Input Parameters: Field Name for reporting, By locator (Xpath, Id, Class) and Time out seconds
// @Output Parameters: NIL
// **************************************************************************************************
	public void sendKeys(String fieldName, By locator, String strValue) {
		System.out.println(locator);
		WebElement element = waitForElement(driver, locator, 10);
		try {
			// Highlight
			flash(element);
			element.clear();
			element.sendKeys(strValue);
			reportStep(strValue + " is entered in the field: " + fieldName, "PASS", false);
		} catch (Exception e) {
			reportStep(strValue + " is not entered in the field: " + fieldName, "FAIL", false);
		}
	}

// **************************************************************************************************
// @Method Name: flash
// @Description: To Highlight the Object
// @Input Parameters: NIL
// @Output Parameters: NIL
// **************************************************************************************************
	public static void flash(WebElement element) {
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		String bgcolor = element.getCssValue("backgroundColor");
		for (int i = 0; i < 3; i++) {
			changeColor("rgb(0,200,0)", element, js);
			changeColor(bgcolor, element, js);
		}
	}

// **************************************************************************************************
// @Method Name: changeColor
// @Description: To highlight in different color
// @Input Parameters: NIL
// @Output Parameters: NIL
// **************************************************************************************************
	public static void changeColor(String color, WebElement element, JavascriptExecutor js) {
		js.executeScript("arguments[0].style.backgroundColor = '" + color + "'", element);
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {

		}
	}

// **************************************************************************************************
// @Method Name: takeSnap
// @Description: Abstract method of Report class. To copy the screenshot.
// @Input Parameters: NIL
// @Output Parameters: NIL
// ***************************************************************************************************
	public long takeSnap() {
		long number = (long) Math.floor(Math.random() * 900000000L) + 10000000L;
		try {
			File screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenShot, new File("./reports/images/" + number + ".png"));
		} catch (WebDriverException e) {
			reportStep("The browser has been closed.", "FAIL", true);
		} catch (IOException e) {
			reportStep("The snapshot could not be taken", "WARN", false);
		}
		return number;
	}
}
