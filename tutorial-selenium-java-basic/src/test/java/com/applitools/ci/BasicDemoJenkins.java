package com.applitools.ci;

import com.applitools.eyes.*;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.jupiter.api.Test;

public class BasicDemoJenkins {

	@Test
	public void test() {

		System.setProperty("webdriver.chrome.driver",
				"C:\\Program Files (x86)\\TRICENTIS\\Tosca Testsuite\\TBox\\Appium-Chromedriver\\win\\chromedriver83.exe");

		// Use Chrome browser
		WebDriver driver = new ChromeDriver();

		// Initialize the Runner for your test.
		EyesRunner runner = new ClassicRunner();

		// Initialize the eyes SDK
		Eyes eyes = new Eyes(runner);

		setUp(eyes);

		try {

			TestDemoApp(driver, eyes);

		} finally {

			tearDown(driver, runner);

		}

	}

	private static void setUp(Eyes eyes) {

		// Initialize the eyes configuration.
		Configuration config = new Configuration();

		// Add this configuration if your tested page includes fixed elements.
		// config.setStitchMode(StitchMode.CSS);

		// You can get your api key from the Applitools dashboard
		config.setApiKey("KUcmq5ZydFs6OHYVt0Zf5gGLTNCCh8J1D2Y102V104105UbUE110");

		/*
		 * Update for Jenkins
		 * 
		 * Jenkins exports the batch ID to the APPLITOOLS_BATCH_ID environment variable.
		 * You need to update your tests code to use this ID in order for your tests to
		 * appear in the Applitools window report in Jenkins. In addition, Jenkins
		 * exports a suggested batch name to the APPLITOOLS_BATCH_NAME environment
		 * variable. Using this batch name is optional (the batch name is used for
		 * display purposes only).
		 */
		BatchInfo batchInfo = new BatchInfo(System.getenv("APPLITOOLS_BATCH_NAME"));

		// If the test runs via Jenkins, set the batch ID accordingly.
		String batchId = System.getenv("APPLITOOLS_BATCH_ID");
		if (batchId != null) {
			batchInfo.setId(batchId);
		}
		eyes.setBatch(batchInfo);

		// set new batch
		// config.setBatch(new BatchInfo("Demo batch"));

		// set the configuration to eyes
		eyes.setConfiguration(config);
	}

	private static void TestDemoApp(WebDriver driver, Eyes eyes) {
		// Set AUT's name, test name and viewport size (width X height)
		// We have set it to 800 x 600 to accommodate various screens. Feel free to
		// change it.
		eyes.open(driver, "Demo App", "Smoke Test", new RectangleSize(800, 600));

		// Navigate the browser to the "ACME" demo app.
		// driver.get("https://demo.applitools.com");

		// To see visual bugs after the first run, use the commented line below instead.
		driver.get("https://demo.applitools.com/index_v2.html");

		// Visual checkpoint #1 - Check the login page. using the fluent API
		// https://applitools.com/docs/topics/sdk/the-eyes-sdk-check-fluent-api.html?Highlight=fluent%20api
		eyes.check(Target.window().fully().withName("Login Window"));

		// This will create a test with two test steps.
		driver.findElement(By.id("log-in")).click();

		// Visual checkpoint #2 - Check the app page.
		eyes.check(Target.window().fully().withName("App Window"));

		// End the test.
		eyes.closeAsync();

	}

	private static void tearDown(WebDriver driver, EyesRunner runner) {
		driver.quit();

		// Wait and collect all test results
		// we pass false to this method to suppress the exception that is thrown if we
		// find visual differences
		TestResultsSummary allTestResults = runner.getAllTestResults(false);

		// Print results
		System.out.println(allTestResults);
	}

}
