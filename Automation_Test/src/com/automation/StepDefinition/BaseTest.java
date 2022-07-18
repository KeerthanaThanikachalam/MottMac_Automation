package com.automation.StepDefinition;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.automation.commonutilities.CommonWrapperMethods;
import com.relevantcodes.extentreports.ExtentTest;

public class BaseTest extends CommonWrapperMethods {

	public ITestContext testContext;

	@BeforeMethod(alwaysRun = true)
	@Parameters({ "TestName" })
	public void beforeMethod(String refTestSheetName, Method M, ITestContext context) {

		System.out.println("@BeforeMethod");

		// Sheet reference for test data
		refTestDataName = refTestSheetName;

		// Scenario_Name = context.getCurrentXmlTest().getName().toString();
		Scenario_Name = M.getName();
		Test testRunning = M.getAnnotation(Test.class);
		testDescription = testRunning.description();

		// Creation of Report Steps
		ExtentTest test = startTestCase(Scenario_Name, testDescription);
		test.assignAuthor("Automation_Assignment");
		test.assignCategory("Testing");
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod() {
		// extentreport.reportend
		System.out.println("@AfterMethod");
		// Complete the test case
		endTestcase();
	}

	@BeforeTest
	public void beforeTest() {
		System.out.println("@BeforeTest");
	}

	@AfterTest
	public void afterTest() throws FileNotFoundException, IOException, InterruptedException {
		System.out.println("@AfterTest");
		endTestcase();
	}

	@BeforeSuite
	public void beforeSuite() {
		System.out.println("Initiating Automation Suite...");
		// Creating the Test Report
		startResult();
		// Loading the Objects (Page Objects) and Variables
		loadObject();
		suiteVariables();
		// Launching the Browser
		launchBrowser(Browser);
		launchApplication(URL, "Global engineering, management and development consultants - Mott MacDonald");
	}

	@AfterSuite
	public void afterSuite() {
		if (driver != null) {
			driver.quit();
		}
		endResult();
		System.out.println("...Exiting Automation Suite");
		// Displaying the Executed Test Result
		testDisplayResult();

	}
}
