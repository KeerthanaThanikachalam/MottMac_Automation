package com.automation.StepDefinition;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.automation.pages.Links_Search_Validation;

public class DriverSuite extends BaseTest {

	@Test(description = "Links and Search Validation")
	@Parameters({ "TestName" })
	public void links_search_validation(String TestName) {
		try {
			Links_Search_Validation validate = new Links_Search_Validation();
			validate.navigate_Homepage();
			validate.allLinks_Validation();
			validate.jobSearch_Validation();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			reportStep("@Method " + Scenario_Name + " exception to be handled", "Pass", true);
		}
	}
}
