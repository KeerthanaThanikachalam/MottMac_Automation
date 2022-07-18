package com.automation.pages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import com.automation.StepDefinition.DriverSuite;

public class Links_Search_Validation extends DriverSuite {

	public boolean status = true;

	public void navigate_Homepage() {

		if (driver.findElement(By.xpath(prop.getProperty("HomePage.AcceptCookies.Button"))).isDisplayed() == true) {
			clickElement("Accept Recommended Cookies Button", By.xpath(prop.getProperty("HomePage.AcceptCookies.Button")));
		}
		if (driver.findElement(By.xpath(prop.getProperty("HomePage.GlobalEnglish.Button"))).isDisplayed() == true) {
			clickElement("Global(English) Button", By.xpath(prop.getProperty("HomePage.GlobalEnglish.Button")));
		}
	}

	public void allLinks_Validation() throws InterruptedException {
		try {
			String url = "";
			int responseCode = 200, count = 0;
			ArrayList<String> list = new ArrayList<String>();
			List<WebElement> allLinks = driver.findElements(By.tagName("a"));
			reportStep("Total links on the Home page: " + allLinks.size(), "PASS", false);
			Iterator<WebElement> iterator = allLinks.iterator();
			while (iterator.hasNext()) {
				url = iterator.next().getAttribute("href");
				System.out.println(url);
				if (url == null || url.isEmpty()) {
					System.out.println("URL is either not configured for anchor tag or it is empty");
					continue;
				}

				CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
				HttpURLConnection httpURLConnect = (HttpURLConnection) (new URL(url).openConnection());
				httpURLConnect.setRequestMethod("HEAD");
				httpURLConnect.connect();
				responseCode = httpURLConnect.getResponseCode();
				if (responseCode >= 400 && responseCode != 405) {
					System.out.println(url + " is a broken link");
					status = false;
					list.add(url);// Adding url in arraylist

				} else {
					System.out.println(url + " is a valid link");
					count++;
				}

			}
			reportStep("Available " + count + " links are validated successfully", "PASS", false);

			if (!status) {
				System.out.println("\nBroken links are " + list);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void jobSearch_Validation() {
		int iTemp = 0;
		String searchRole = "";
		clickElement("Careers Header", By.xpath(prop.getProperty("HomePage.Careers.HeaderLink")));
		verifyElementExist("Careers Header", By.xpath(prop.getProperty("CareersPage.Careers.Header")));
		if (driver.findElement(By.xpath(prop.getProperty("HomePage.GlobalEnglish.Button"))).isDisplayed() == true) {
			clickElement("Global(English) Button", By.xpath(prop.getProperty("HomePage.GlobalEnglish.Button")));
		}

		clickElement("Search Header", By.xpath(prop.getProperty("CareersPage.Search.HeaderLink")));
		verifyElementExist("Careers Search Header", By.xpath(prop.getProperty("SearchPage.CareersSearch.Header")));
		if (driver.findElement(By.xpath(prop.getProperty("HomePage.GlobalEnglish.Button"))).isDisplayed() == true) {
			clickElement("Global(English) Button", By.xpath(prop.getProperty("HomePage.GlobalEnglish.Button")));
		}
		sendKeys("Search Box", By.xpath(prop.getProperty("SearchPage.SearchBox.InputText")), "Software engineer");
		clickElement("Search Button", By.xpath(prop.getProperty("SearchPage.SearchBox.SearchButton")));

		List<WebElement> search = driver.findElements(By.xpath(prop.getProperty("SearchPage.SearchResults.Text")));
		for (int i = 1; i < search.size(); i++) {

			searchRole = driver
					.findElement(By
							.xpath(prop.getProperty("SearchPage.SearchResults.Text") + "[" + i + "]/div/div/div[1]/h6"))
					.getText();
			if (searchRole.equalsIgnoreCase("Software engineer")) {
				clickElement("View Job",
						By.xpath(prop.getProperty("SearchPage.SearchResults.Text") + "[" + i + "]/div/div/div[6]/a"));
				iTemp++;
			}
		}

		if (iTemp == 0) {
			searchRole = driver
					.findElement(By.xpath(prop.getProperty("SearchPage.SearchResults.Text") + "[1]/div/div/div[1]/h6"))
					.getText();
			clickElement("View Job",
					By.xpath(prop.getProperty("SearchPage.SearchResults.Text") + "[1]/div/div/div[6]/a"));
			verifyElementExist(searchRole + " Header",
					By.xpath(prop.getProperty("SearchPage.ViewJob.Header").replace("&Value", searchRole)));
		}

	}

}
