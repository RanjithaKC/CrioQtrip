package qtriptest.tests;

import qtriptest.driverManager.DriverSingleton;
import qtriptest.pages.AdventureDetailsPage;
import qtriptest.pages.AdventurePage;
import qtriptest.pages.HistoryPage;
import qtriptest.pages.HomePage;
import qtriptest.pages.LoginPage;
import qtriptest.pages.RegisterPage;
import java.net.MalformedURLException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;
import org.testng.annotations.Test;

public class testCase_03 {

    public static RemoteWebDriver driver;
    HomePage homePage;
    RegisterPage registerPage;
    LoginPage loginPage;
    AdventurePage adventurePage;
    AdventureDetailsPage adventureDetailsPage;
    HistoryPage historyPage;

    SoftAssert softAssert;

    public static void logStatus(String type, String message, String status) {
		System.out.println(String.format("%s |  %s  |  %s | %s",
				String.valueOf(java.time.LocalDateTime.now()), type, message, status));
	}

    @BeforeSuite(alwaysRun = true, enabled = true)
	public void createDriver() throws MalformedURLException {
		logStatus("driver", "Initializing driver", "Started");
        driver = DriverSingleton.getDriver("chrome");
        DriverSingleton.launchApp("https://qtripdynamic-qa-frontend.vercel.app/");
		logStatus("driver", "Initializing driver", "Success");
        // initializing pages
        homePage = new HomePage(driver);
        registerPage = new RegisterPage(driver);
        loginPage = new LoginPage(driver, registerPage);
        adventurePage = new AdventurePage(driver);
        adventureDetailsPage = new AdventureDetailsPage(driver);
        historyPage = new HistoryPage(driver);
	}

    @Test(dataProvider = "adventureData", dataProviderClass = ExternalDataProvider.class, description="Verify that adventure booking and cancellation flow",enabled=true, priority=3, groups = {"Booking and Cancellation Flow"})
    public void TestCase03(String newUserName, String password, String searchCity, String adventureName,String guestName, String date, String count) throws InterruptedException{
        try{
        logStatus("test", "TestCase03", "Started");
        softAssert = new SoftAssert();
        // checking visibility and enablity of register button
        softAssert.assertTrue(homePage.isRegisteredButtonVisible(), "Register button on home page is not found");
        logStatus("homepage", "registerbutton visibility", "Success");

        //clicking on register button on home page
        homePage.clickRegister();
        logStatus("homepage", "clicked registerbutton", "Success");
        //verifying whether we are on registration page or not
        softAssert.assertTrue(registerPage.isRegistrationPageNavigationSucceed(), "Register page navigation failed");

        //registring new user
        registerPage.registerNewUser( newUserName,  password,  password, true);
        logStatus("registerpage", "user registration", "Success");

        //verifying whether we are on login page or not
        softAssert.assertTrue(loginPage.isLoginPageNavigationSucceed(),"Login page navigation failed");
        // user logging in
        loginPage.performLogin( newUserName,  password, true);
        logStatus("loginpage", "user login", "Success");

        //checking for visibility of logout btn on home page
        homePage.isSpecifiedMemberVisible();
        logStatus("Functional", "search city", "started");

        // search city
        homePage.searchCity(searchCity);
        softAssert.assertTrue(homePage.assertAutoCompleteText(searchCity),"Autocomplete text assertion failed");
        logStatus("Functional", "city selection", "started");
        softAssert.assertTrue(homePage.selectCity(searchCity),"City selection failed");

        // adventure page
        softAssert.assertTrue(adventurePage.isAdventurePageNavigationSucceed(),"Adventure page navigation failed");
        logStatus("Functional", "adventure selection", "started");
        adventurePage.selectAdventure(adventureName);

        //adventure details page
        softAssert.assertTrue(adventureDetailsPage.isDetailAdventurePageNavigationSucceed(),"vavigation to detail adventure page failed");
        logStatus("Functional", "book adventure", "started");
        adventureDetailsPage.bookAdventure(guestName, date, count);
        softAssert.assertTrue(adventureDetailsPage.isBookingSuccessful(),"navigation to detail adventure page failed");
        logStatus("Functional", "click on reservation", "started");
        adventureDetailsPage.clickOnReservation();

        //History page
        softAssert.assertTrue(historyPage.isHistoryPageNavigationSuccessful(),"history page navigation failed");
        logStatus("verify", "get transaction id", "started");
        historyPage.getTransactionID();
        logStatus("Functional", "cancel booking", "started");
        historyPage.cancelBooking();
        softAssert.assertTrue(historyPage.isTransactionIDNotPresent(),"adventure booking not cancelled still exists in history page");
        logStatus("Functional", "click on logout", "started");

        //logout
        historyPage.clickOnLogout();
        softAssert.assertAll();
    }catch(InterruptedException e){
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
