package qtriptest.tests;

import qtriptest.ReportSingleton;
import qtriptest.Utils.Screenshot;
import qtriptest.driverManager.DriverSingleton;
import qtriptest.pages.AdventureDetailsPage;
import qtriptest.pages.AdventurePage;
import qtriptest.pages.HistoryPage;
import qtriptest.pages.HomePage;
import qtriptest.pages.LoginPage;
import qtriptest.pages.RegisterPage;
import java.io.IOException;
import java.net.MalformedURLException;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
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
    ExtentTest test;


    public static void logStatus(String type, String message, String status) {
		System.out.println(String.format("%s |  %s  |  %s | %s",
		String.valueOf(java.time.LocalDateTime.now()), type, message, status));
	}

    @BeforeSuite(alwaysRun = true, enabled = true)
	public void createDriver() throws MalformedURLException {
        test = ReportSingleton.startTest("TestCase03 - Verify that adventure booking and cancellation flow");

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
    public void TestCase03(String newUserName, String password, String searchCity, String adventureName,String guestName, String date, String count) throws InterruptedException, IOException{
        try{
            test.log(LogStatus.INFO, "Test Started: Verify that adventure booking and cancellation flow");
            logStatus("test", "TestCase03", "Started");
            softAssert = new SoftAssert();
            // checking visibility and enablity of register button
            boolean isButtonVisible = homePage.isRegisteredButtonVisible();
            softAssert.assertTrue(isButtonVisible, "Register button on home page is not found");
            if(!isButtonVisible){
                test.log(LogStatus.FAIL, "Register Button is not visible");
            }
            logStatus("homepage", "register button visibility", "Success");

            //clicking on register button on home page
            homePage.clickRegister();
            test.log(LogStatus.INFO, "clicked registerbutton");
            logStatus("homepage", "clicked registerbutton", "Success");
            //verifying whether we are on registration page or not
            boolean registrationPageNavigation = registerPage.isRegistrationPageNavigationSucceed();
            softAssert.assertTrue(registrationPageNavigation, "Register page navigation failed");
            if(!registrationPageNavigation){
                test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver))+"Register Page navigation failed");
            }

            //registring new user
            registerPage.registerNewUser( newUserName,  password,  password, true);
            test.log(LogStatus.INFO, "user registration success");
            logStatus("registerpage", "user registration", "Success");

            //verifying whether we are on login page or not
            boolean loginPageNavigation = loginPage.isLoginPageNavigationSucceed();
            softAssert.assertTrue(loginPageNavigation,"Login page navigation failed");
                if(!loginPageNavigation){
                test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver))+"Login Page navigation failed");
            }
            // user logging in
            loginPage.performLogin( newUserName,  password, true);
            logStatus("loginpage", "user login", "Success");
            test.log(LogStatus.INFO, "user login success");
            //checking for visibility of logout btn on home page
            boolean checkUserOnHomePage = homePage.isSpecifiedMemberVisible();
            if(!checkUserOnHomePage){
                test.log(LogStatus.FAIL,test.addScreenCapture(Screenshot.capture(driver))+ "user is not on home page after login");
            }
            logStatus("home page", "user is on home page", "Success");
            test.log(LogStatus.INFO, "user is on home page after login");

            // search city
            homePage.searchCity(searchCity);
            logStatus("Functionality", "search city", "Success");
            test.log(LogStatus.INFO, "Searched for city");

            boolean autoCompleteText = homePage.assertAutoCompleteText(searchCity);
            softAssert.assertTrue(autoCompleteText,"Autocomplete text assertion failed");
            if(!autoCompleteText){
                test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver))+"city auto complete text check failed");
            }
            logStatus("Assertion", "Autocomplete assertion", "Success");
            test.log(LogStatus.INFO, "verified auto complete text");

            boolean citySelection = homePage.selectCity(searchCity);
            softAssert.assertTrue(citySelection,"City selection failed");
            if(!citySelection){
                test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver))+"city selection failed");
            }
            logStatus("Functionality", "city selection", "Success");
            test.log(LogStatus.INFO, "verified city selection");

            // adventure page
            boolean adventurePageNavigation = adventurePage.isAdventurePageNavigationSucceed();
            softAssert.assertTrue(adventurePageNavigation,"Adventure page navigation failed");
            if(!adventurePageNavigation){
                test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver))+"Adventure page vavigation failed");
            }
            logStatus("Navigation", "Adventure page navigation", "Success");
            test.log(LogStatus.INFO, "navigated to adventure page");

            adventurePage.selectAdventure(adventureName);
            logStatus("Adventure", "Adventure selection", "Success");
            test.log(LogStatus.INFO, "Adventure selected");

            //adventure details page
            boolean adventureDetailsNavigation = adventureDetailsPage.isDetailAdventurePageNavigationSucceed();
            softAssert.assertTrue(adventureDetailsNavigation,"navigation to detail adventure page failed");
            if(!adventureDetailsNavigation){
                test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver))+"Adventure details page vavigation failed");
            }
            logStatus("Navigation", "Adventure details page navigation", "Success");
            test.log(LogStatus.INFO, "navigated to adventure details page");

            adventureDetailsPage.bookAdventure(guestName, date, count);
            boolean reservationSuccessfull = adventureDetailsPage.isBookingSuccessful();
            softAssert.assertTrue(reservationSuccessfull,"navigation to detail adventure page failed");
            if(!reservationSuccessfull){
                test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver))+"adventure reservation failed");
            }
            logStatus("Adventure details page", "adventure reservation", "Success");
            test.log(LogStatus.INFO, "adventure reservation success");

            adventureDetailsPage.clickOnReservation();
            logStatus("Adventure details page", "click on reservation button", "Success");
            test.log(LogStatus.INFO, "click on reservation button");

            //History page
            boolean historyPageNavigation = historyPage.isHistoryPageNavigationSuccessful();
            softAssert.assertTrue(historyPageNavigation,"history page navigation failed");
            if(!historyPageNavigation){
                test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver))+"history page navigation failed");
            }
            logStatus("history page", "history page navigation", "Success");
            test.log(LogStatus.INFO, "history page navigation pass");

            historyPage.getTransactionID();
            logStatus("history page", "get transaction id", "success");

            historyPage.cancelBooking();
            logStatus("history page", "cancel booking", "success");
            test.log(LogStatus.INFO, "booking cancelled");

            boolean transactionID = historyPage.isTransactionIDNotPresent();
            softAssert.assertTrue(transactionID,"adventure booking not cancelled still exists in history page");
            if(!transactionID){
                test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver))+"transaction id on history page exists");
            }
            logStatus("history page", "no bookings available", "success");
            test.log(LogStatus.INFO, "no bookings available");

            //logout
            historyPage.clickOnLogout();
            logStatus("history page", "logout", "success");
            test.log(LogStatus.INFO, test.addScreenCapture(Screenshot.capture(driver))+"logged out");
            softAssert.assertAll();
        }catch(InterruptedException e){
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
