package qtriptest.tests;

import qtriptest.ReportSingleton;
import qtriptest.Utils.Screenshot;
import qtriptest.driverManager.DriverSingleton;
import qtriptest.pages.AdventurePage;
import qtriptest.pages.HistoryPage;
import qtriptest.pages.HomePage;
import java.net.MalformedURLException;
import java.net.URL;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
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
    ExtentTest test;

   // private static boolean isInitialSetupDone = false; // Static variable to track setup execution

    public static void logStatus(String type, String message, String status) {
		System.out.println(String.format("%s |  %s  |  %s | %s",
				String.valueOf(java.time.LocalDateTime.now()), type, message, status));
	}

    @BeforeSuite(alwaysRun = true, enabled = true)
	public void createDriver() throws MalformedURLException {
        test = ReportSingleton.startTest("TestCase02 - Verify that Search and filters flow");
		logStatus("driver", "Initializing driver", "Started");
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
            test.log(LogStatus.INFO, "Test Started: Verify that Search and filters flow");
            //Thread.sleep(3000);
            //this step is added just bcz while executiong testcase02 is getting executed at last after executing testcase03
            //homePage.clickOnHomeBtn();
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
            logStatus("Functionality", "search city", "Success");
            test.log(LogStatus.INFO, "Searched for city");

            //softAssert.assertTrue(homePage.assertAutoCompleteText("Bengaluru"),"Autocomplete text assertion failed");
            boolean autoCompleteText = homePage.assertAutoCompleteText(cityName);
            softAssert.assertTrue(autoCompleteText,"Autocomplete text assertion failed");
            if(!autoCompleteText){
                test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver))+"city auto complete text check failed");
            }
            logStatus("Assertion", "Autocomplete assertion", "Success");
            test.log(LogStatus.INFO, "verified auto complete text");

            //softAssert.assertTrue(homePage.selectCity("Bengaluru"),"City selection failed");
            boolean citySelection = homePage.selectCity(cityName);
            softAssert.assertTrue(citySelection,"City selection failed");
            if(!citySelection){
                test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver))+"city selection failed");
            }
            logStatus("Functionality", "city selection", "Success");
            test.log(LogStatus.INFO, "verified city selection");

            boolean adventurePageNavigation = adventurePage.isAdventurePageNavigationSucceed();
            softAssert.assertTrue(adventurePageNavigation,"Adventure page navigation failed");
            if(!adventurePageNavigation){
                test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver))+"Adventure page vavigation failed");
            }
            logStatus("Navigation", "Adventure page navigation", "Success");
            test.log(LogStatus.INFO, "navigated to adventure page");

            adventurePage.getResultCount();
            logStatus("size", "extracting adventure card count", "Success");

            //softAssert.assertTrue(adventurePage.setFilterByHour("2-6 Hours"),"selected value is different from the input value");
            boolean durationFilterCheck = adventurePage.setFilterByHour(durationFilter);
            softAssert.assertTrue(durationFilterCheck,"selected value is different from the input value");
            if(!durationFilterCheck){
                test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver))+"invalid duration filter");
            }
            logStatus("Filter", "Duration Filter", "Success");
            test.log(LogStatus.INFO, "Duration filter applied");

            //softAssert.assertTrue(adventurePage.verifyDurationResults("2-6 Hours"),"duration on adventure card is not in the Input Range");
            boolean durationResultOnEachCard = adventurePage.verifyDurationResults(durationFilter);
            softAssert.assertTrue(durationResultOnEachCard,"duration on adventure card is not in the Input Range");
            if(!durationResultOnEachCard){
                test.log(LogStatus.FAIL,test.addScreenCapture(Screenshot.capture(driver))+ "invalid duration on cards");
            }
            logStatus("verification", "verify Duration on each card", "Success");
            test.log(LogStatus.INFO, "Duration on each card is verified");

            //adventurePage.setCategoryValue("Cycling Routes");
            adventurePage.setCategoryValue(categoryFilter);
            logStatus("Filter", "Applied Category Filter", "Success");
            test.log(LogStatus.INFO, "Applied Category Filter");

            //softAssert.assertTrue(adventurePage.verifyCategoryOnEachCard("Cycling"),"category on card is not same as the input category");
            boolean categoryOnEachCard = adventurePage.verifyCategoryOnEachCard(categoryFilter);
            softAssert.assertTrue(categoryOnEachCard,"category on card is not same as the input category");
            if(!categoryOnEachCard){
                test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver))+"invalid category on cards");
            }
            logStatus("Verification", "Verify Category on each card", "Success");
            test.log(LogStatus.INFO, "verified Category on each card");

            adventurePage.clearDurationFilter();
            adventurePage.clearCategoryFilter();
            logStatus("Function", "clear the applied filters", "Success");
            test.log(LogStatus.INFO, "cleared the applied filters");
 
            //System.out.println("ExpectedFilteredResults : "+ExpectedFilteredResults);
            //System.out.println("ExpectedUnFilteredResults : "+ExpectedUnFilteredResults);

            boolean cardCount = adventurePage.verifyAllRecords();
            softAssert.assertTrue(cardCount,"the count of adventure card before and after clearing filter are different");
            if(!cardCount){
                test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver))+"invalid count of cards before filter and after clearing filter");
            }
            logStatus("verification", "verifying the card count before and after applying filter", "Success");
            test.log(LogStatus.INFO, "verified the card count before and after applying filter");

            adventurePage.clickOnHomeButton();
            logStatus("Functional", "click on home button", "Success");
            test.log(LogStatus.INFO, test.addScreenCapture(Screenshot.capture(driver))+"clicked on home button");

            softAssert.assertAll();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Exception occured : "+e.getMessage());
        }
    }


    @AfterSuite(enabled = true)
	public static void quitDriver() throws MalformedURLException {
        ReportSingleton.endTest();
        ReportSingleton.flushReport();
		//driver.close();
		driver.quit();
		logStatus("driver", "Quitting driver", "Success");
	}
}
