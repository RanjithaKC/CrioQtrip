package qtriptest.tests;

import qtriptest.driverManager.DriverSingleton;
import qtriptest.pages.AdventurePage;
import qtriptest.pages.HistoryPage;
import qtriptest.pages.HomePage;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class testCase_02 {

    public static RemoteWebDriver driver;
    HomePage homePage;
    //RegisterPage registerPage;
    AdventurePage adventurePage;
    SoftAssert softAssert;
   // private static boolean isInitialSetupDone = false; // Static variable to track setup execution

    public static void logStatus(String type, String message, String status) {
		System.out.println(String.format("%s |  %s  |  %s | %s",
				String.valueOf(java.time.LocalDateTime.now()), type, message, status));
	}

    @BeforeSuite(alwaysRun = true, enabled = true)
	public void createDriver() throws MalformedURLException {
		logStatus("driver", "Initializing driver", "Started");
		// final DesiredCapabilities capabilities = new DesiredCapabilities();
		// capabilities.setBrowserName(BrowserType.CHROME);
        // // Add Chrome options to disable caching
        // ChromeOptions options = new ChromeOptions();
        // options.addArguments("--disable-cache", "--disable-application-cache", "--disable-offline-load-stale-cache", "--disk-cache-size=0");
        // capabilities.merge(options);

		// driver = new RemoteWebDriver(new URL("http://localhost:8082/wd/hub"), capabilities);
        // driver.manage().window().maximize();
        // driver.get("https://qtripdynamic-qa-frontend.vercel.app/");
        // System.out.println("Navigated to: " + driver.getCurrentUrl());
        driver = DriverSingleton.getDriver("chrome");
        DriverSingleton.launchApp("https://qtripdynamic-qa-frontend.vercel.app/");
		logStatus("driver", "Initializing driver", "Success");
        // initializing pages
        homePage = new HomePage(driver);
        adventurePage = new AdventurePage(driver);
	}

    @Test(dataProvider = "qtripCityData", dataProviderClass = ExternalDataProvider.class, description = "Verify that Search and filters flow",enabled = true, priority=2, groups = {"Search and Filter flow"})
    public void TestCase02(String cityName,String categoryFilter, String durationFilter, String ExpectedFilteredResults, String ExpectedUnFilteredResults) throws InterruptedException{
        logStatus("test", "TestCase02", "Started");
        softAssert = new SoftAssert();
        try{
            //Thread.sleep(3000);
            //this step is added just bcz while executiong testcase02 is getting executed at last after executing testcase03
            //homePage.clickOnHomeBtn();
            logStatus("Functionality", "search city", "Started");
            //homePage.searchCity("Bengaluru");

            // try to send invalid data as given in test case
            // if (!isInitialSetupDone) {
            //     logStatus("Functionality", "search city with invalid input", "Started");
            //     homePage.searchCity("svgwhs");
            //     softAssert.assertTrue(homePage.assertAutoCompleteText("No City found"), "Autocomplete text assertion failed");
            //     driver.navigate().refresh();
            //     isInitialSetupDone = true; 
            // }

            homePage.searchCity(cityName);
            logStatus("Assertion", "Autocomplete assertion", "Started");
            //softAssert.assertTrue(homePage.assertAutoCompleteText("Bengaluru"),"Autocomplete text assertion failed");
            softAssert.assertTrue(homePage.assertAutoCompleteText(cityName),"Autocomplete text assertion failed");
            logStatus("Functionality", "city selection", "Started");
            //softAssert.assertTrue(homePage.selectCity("Bengaluru"),"City selection failed");
            softAssert.assertTrue(homePage.selectCity(cityName),"City selection failed");
            logStatus("Navigation", "Adventure page navigation", "Started");
            softAssert.assertTrue(adventurePage.isAdventurePageNavigationSucceed(),"Adventure page navigation failed");
            logStatus("size", "extracting adventure card count", "Started");
            adventurePage.getResultCount();
            logStatus("Filter", "Duration Filter", "Started");
            //softAssert.assertTrue(adventurePage.setFilterByHour("2-6 Hours"),"selected value is different from the input value");
            softAssert.assertTrue(adventurePage.setFilterByHour(durationFilter),"selected value is different from the input value");
            logStatus("verification", "verify Duration on each card", "Started");
            //softAssert.assertTrue(adventurePage.verifyDurationResults("2-6 Hours"),"duration on adventure card is not in the Input Range");
            softAssert.assertTrue(adventurePage.verifyDurationResults(durationFilter),"duration on adventure card is not in the Input Range");
            logStatus("Filter", "Category Filter", "Started");
            //adventurePage.setCategoryValue("Cycling Routes");
            adventurePage.setCategoryValue(categoryFilter);
            logStatus("Verification", "Verify Category on each card", "Started");
            //softAssert.assertTrue(adventurePage.verifyCategoryOnEachCard("Cycling"),"category on card is not same as the input category");
            softAssert.assertTrue(adventurePage.verifyCategoryOnEachCard(categoryFilter),"category on card is not same as the input category");
            logStatus("Function", "clear the applied filters", "Started");
            adventurePage.clearDurationFilter();
            adventurePage.clearCategoryFilter();
            
            System.out.println("ExpectedFilteredResults : "+ExpectedFilteredResults);
            System.out.println("ExpectedUnFilteredResults : "+ExpectedUnFilteredResults);

            logStatus("verification", "verifying the card count before and after applying filter", "Started");
            softAssert.assertTrue(adventurePage.verifyAllRecords(),"the count of adventure card before and after clearing filter are different");
            logStatus("Functional", "click on home button", "Started");
            adventurePage.clickOnHomeButton();
            softAssert.assertAll();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Exception occured : "+e.getMessage());
        }
    }


    @AfterSuite(enabled = true)
	public static void quitDriver() throws MalformedURLException {
		driver.close();
		driver.quit();
		logStatus("driver", "Quitting driver", "Success");
	}
}
