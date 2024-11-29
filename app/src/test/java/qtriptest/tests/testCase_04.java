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
import java.util.List;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class testCase_04 {

    public static RemoteWebDriver driver;
    HomePage homePage;
    RegisterPage registerPage;
    AdventurePage adventurePage;
    LoginPage loginPage;
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
        test = ReportSingleton.startTest("TestCase04 - Verify that Booking history can be viewed");
		logStatus("driver", "Initializing driver", "Started");
        driver = DriverSingleton.getDriver("chrome");
        DriverSingleton.launchApp("https://qtripdynamic-qa-frontend.vercel.app/");
		logStatus("driver", "Initializing driver", "Success");
        // initializing pages
        homePage = new HomePage(driver);
        adventurePage = new AdventurePage(driver);
        loginPage = new LoginPage(driver, registerPage);
        adventureDetailsPage = new AdventureDetailsPage(driver);
        historyPage = new HistoryPage(driver);
        registerPage = new RegisterPage(driver);
	}

    @Test(dataProvider = "testcase4Data", dataProviderClass = ExternalDataProvider.class, description = "Verify that Booking history can be viewed", enabled = true, priority=4, groups = {"Reliability Flow"})
    public void TestCase04(String userName, String password, String dataSet1, String dataSet2, String dataSet3) throws InterruptedException {
        softAssert = new SoftAssert();
        String[] DS1 = dataSet1.split(";");
        String[] DS2 = dataSet2.split(";");
        String[] DS3 = dataSet3.split(";");

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
            registerPage.registerNewUser( userName,  password,  password, true);
            test.log(LogStatus.INFO, "user registration success");
            logStatus("registerpage", "user registration", "Success");

            //verifying whether we are on login page or not
            boolean loginPageNavigation = loginPage.isLoginPageNavigationSucceed();
            softAssert.assertTrue(loginPageNavigation,"Login page navigation failed");
                if(!loginPageNavigation){
                test.log(LogStatus.FAIL,test.addScreenCapture(Screenshot.capture(driver))+ "Login Page navigation failed");
            }
            // user logging in
            loginPage.performLogin( userName,  password, true);
            logStatus("loginpage", "user login", "Success");
            test.log(LogStatus.INFO, "user login success");
            //checking for visibility of logout btn on home page
            boolean checkUserOnHomePage = homePage.isSpecifiedMemberVisible();
            if(!checkUserOnHomePage){
                test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver))+"user is not on home page after login");
            }
            logStatus("home page", "user is on home page", "Success");
            test.log(LogStatus.INFO, "user is on home page after login");

            // Step 4-5: Make bookings using the data from Excel
            logStatus("booking", "booking adventure for 3 sets of data", "started");
            bookAdventure(DS1);
            bookAdventure(DS2);
            bookAdventure(DS3);

            // Logout after verifying bookings
            homePage.logoutUser();
            logStatus("history page", "log out", "Success");
            test.log(LogStatus.INFO, "logged out from history page");

            softAssert.assertAll();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Exception occured : "+e.getMessage());
        }
    }
    
    private void bookAdventure(String[] bookingData) throws InterruptedException, IOException {
        String cityName = bookingData[0];
        String adventureName = bookingData[1];
        String customerName = bookingData[2];
        String bookingDate = bookingData[3];
        String guestCount = bookingData[4];

        // Search and select city
        homePage.searchCity(cityName);
        logStatus("Functionality", "search city", "Success");
        test.log(LogStatus.INFO, "Searched for city");

        boolean citySelection = homePage.selectCity(cityName);
        softAssert.assertTrue(citySelection,"City selection failed");
        if(!citySelection){
            test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver))+"city selection failed");
        }
        logStatus("Functionality", "city selection", "Success");
        test.log(LogStatus.INFO, "verified city selection");

        // Select Adventure
        boolean adventurePageNavigation = adventurePage.isAdventurePageNavigationSucceed();
        softAssert.assertTrue(adventurePageNavigation,"Adventure page navigation failed");
        if(!adventurePageNavigation){
            test.log(LogStatus.FAIL,test.addScreenCapture(Screenshot.capture(driver))+ "Adventure page vavigation failed");
        }
        logStatus("Navigation", "Adventure page navigation", "Success");
        test.log(LogStatus.INFO, "navigated to adventure page");

        adventurePage.selectAdventure(adventureName);
        logStatus("Adventure", "Adventure selection", "Success");
        test.log(LogStatus.INFO, "Adventure selected");

        // Reserve Adventure
        boolean adventureDetailsNavigation = adventureDetailsPage.isDetailAdventurePageNavigationSucceed();
        softAssert.assertTrue(adventureDetailsNavigation,"navigation to detail adventure page failed");
        if(!adventureDetailsNavigation){
            test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver))+"Adventure details page vavigation failed");
        }
        logStatus("Navigation", "Adventure details page navigation", "Success");
        test.log(LogStatus.INFO, "navigated to adventure details page");

        adventureDetailsPage.bookAdventure(customerName, bookingDate, guestCount);
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

        // history page
        boolean historyPageNavigation = historyPage.isHistoryPageNavigationSuccessful();
        softAssert.assertTrue(historyPageNavigation,"history page navigation failed");
        if(!historyPageNavigation){
            test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver))+"history page navigation failed");
        }
        logStatus("history page", "history page navigation", "Success");
        test.log(LogStatus.INFO, "history page navigation pass");
        
        List<String> transactionIDs = historyPage.getTransactionID();
        // for(String id : transactionIDs){
        //     System.out.println("id : "+id);
        // }
        assert !transactionIDs.isEmpty() : "Booking not recorded in history.";
        if(transactionIDs.isEmpty()){
            test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver))+"bookings on history page is empty");
        }
        logStatus("history page", "reservations on history page", "Success");
        test.log(LogStatus.INFO, "reservations on history page pass");

        historyPage.clickOnHomeBtn();
        logStatus("history page", "click on home button from history page", "Success");
        test.log(LogStatus.INFO, test.addScreenCapture(Screenshot.capture(driver))+"clicked home button from history page pass");        
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
