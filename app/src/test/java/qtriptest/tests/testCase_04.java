package qtriptest.tests;

import qtriptest.driverManager.DriverSingleton;
import qtriptest.pages.AdventureDetailsPage;
import qtriptest.pages.AdventurePage;
import qtriptest.pages.HistoryPage;
import qtriptest.pages.HomePage;
import qtriptest.pages.LoginPage;
import qtriptest.pages.RegisterPage;
import java.net.MalformedURLException;
import java.util.List;
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
            softAssert.assertTrue(homePage.isRegisteredButtonVisible(), "Register button on home page is not found");
            logStatus("homepage", "registerbutton visibility", "Success");
            //clicking on register button on home page
            homePage.clickRegister();
            logStatus("homepage", "clicked registerbutton", "Success");
            //verifying whether we are on registration page or not
            softAssert.assertTrue(registerPage.isRegistrationPageNavigationSucceed(), "Register page navigation failed");
            //registring new user
            registerPage.registerNewUser( userName,  password,  password, true);
            logStatus("registerpage", "user registration", "Success");
            //verifying whether we are on login page or not
            softAssert.assertTrue(loginPage.isLoginPageNavigationSucceed(),"Login page navigation failed");
            // user logging in
            loginPage.performLogin( userName,  password, true);
            logStatus("loginpage", "user login", "Success");
            //checking for visibility of logout btn on home page
            homePage.isSpecifiedMemberVisible();

            // Step 4-5: Make bookings using the data from Excel
            logStatus("booking", "booking adventure for 3 sets of data", "started");
            bookAdventure(DS1);
            bookAdventure(DS2);
            bookAdventure(DS3);

            // Logout after verifying bookings
            homePage.logoutUser();
            softAssert.assertAll();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Exception occured : "+e.getMessage());
        }
    }
    
    private void bookAdventure(String[] bookingData) throws InterruptedException {
        String cityName = bookingData[0];
        String adventureName = bookingData[1];
        String customerName = bookingData[2];
        String bookingDate = bookingData[3];
        String guestCount = bookingData[4];

        // Search and select city
        logStatus("Functional", "search and select city", "started");
        homePage.searchCity(cityName);
        homePage.selectCity(cityName);

        // Select Adventure
        logStatus("Functional", "select adventure", "started");
        adventurePage.selectAdventure(adventureName);

        // Reserve Adventure
        logStatus("Functional", "reserving adventure", "started");
        adventureDetailsPage.bookAdventure(customerName, bookingDate, guestCount);
        softAssert.assertTrue(adventureDetailsPage.isBookingSuccessful(),"navigation to detail adventure page failed");
        adventureDetailsPage.clickOnReservation();

        // history page
        softAssert.assertTrue(historyPage.isHistoryPageNavigationSuccessful(),"history page navigation failed");
        List<String> transactionIDs = historyPage.getTransactionID();
        for(String id : transactionIDs){
            System.out.println("id : "+id);
        }
        assert !transactionIDs.isEmpty() : "Booking not recorded in history.";

        historyPage.clickOnHomeBtn();
    }

    @AfterSuite(enabled = true)
	public static void quitDriver() throws MalformedURLException {
		driver.close();
		driver.quit();
		logStatus("driver", "Quitting driver", "Success");
	}
}
