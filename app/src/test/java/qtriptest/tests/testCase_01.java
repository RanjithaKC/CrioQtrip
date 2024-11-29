package qtriptest.tests;

import qtriptest.ReportSingleton;
import qtriptest.Utils.Screenshot;
import qtriptest.driverManager.DriverSingleton;
import qtriptest.pages.HomePage;
import qtriptest.pages.LoginPage;
import qtriptest.pages.RegisterPage;
import java.io.IOException;
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
import org.testng.asserts.SoftAssert;
import org.testng.annotations.Test;


public class testCase_01 {
    public static RemoteWebDriver driver;
    HomePage homePage;
    RegisterPage registerPage;
    LoginPage loginPage;
    SoftAssert softAssert;
    ExtentTest test;

    
    public static void logStatus(String type, String message, String status) {
		System.out.println(String.format("%s |  %s  |  %s | %s",
				String.valueOf(java.time.LocalDateTime.now()), type, message, status));
	}

    @BeforeSuite(alwaysRun = true, enabled = true)
	public void createDriver() throws MalformedURLException {
        test = ReportSingleton.startTest("TestCase01 - Registration and Login flow");
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
        registerPage = new RegisterPage(driver);
        loginPage = new LoginPage(driver, registerPage);
	}

    @Test(dataProvider = "qtripData", dataProviderClass = ExternalDataProvider.class, description = "Registration and Login flow",enabled = true, priority = 1, groups = {"Login Flow"})
    public void TestCase01(String userName, String password) throws InterruptedException, IOException{
        softAssert = new SoftAssert();
        try{   
            // homePage.navigateToRegisterPage();
            // checking visibility and enablity of register button
            test.log(LogStatus.INFO, "Test Started: Registration and Login Flow");
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
                test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver)) + "Register Page navigation failed");
            }
            //registring new user
            //registerPage.registerNewUser( "testUser",  "abc@123",  "abc@123", true);
            registerPage.registerNewUser( userName,  password,  password, true);
            test.log(LogStatus.INFO, "user registration success");
            logStatus("registerpage", "user registration", "Success");
            //verifying whether we are on login page or not
            boolean loginPageNavigation = loginPage.isLoginPageNavigationSucceed();
            softAssert.assertTrue(loginPageNavigation,"Login page navigation failed");
            if(!loginPageNavigation){
                test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver))+"Login Page navigation failed");
            }
            //String email = registerPage.formattedEmail;
            // loginPage.performLogin( email,  "abc@123", false);
            // user logging in
            loginPage.performLogin( userName,  password, true);
            logStatus("loginpage", "user login", "Success");
            test.log(LogStatus.INFO, "user login success");
            //checking for visibility of logout btn on home page
            boolean checkUserOnHomePage = homePage.isSpecifiedMemberVisible();
            if(!checkUserOnHomePage){
                test.log(LogStatus.FAIL, test.addScreenCapture(Screenshot.capture(driver))+"user is not on home page after login");
            }
            //click on logout
            homePage.logoutUser();
            logStatus("functional", "user logout", "Success");
            test.log(LogStatus.INFO, test.addScreenCapture(Screenshot.capture(driver))+"user logedOut successfully");

            softAssert.assertAll();
        }catch(InterruptedException e){
            System.out.println("Exception occured : "+e.getMessage());
        }
    }

    @AfterSuite(enabled = true)
	public static void quitDriver() throws MalformedURLException {
        ReportSingleton.endTest();
        ReportSingleton.flushReport();
		driver.close();
		driver.quit();
		logStatus("driver", "Quitting driver", "Success");
	}
}
