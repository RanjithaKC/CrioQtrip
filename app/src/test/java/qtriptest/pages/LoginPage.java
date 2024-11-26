package qtriptest.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private RemoteWebDriver driver;
    private final String loginPageEndPoint = "/pages/login";
    RegisterPage registerPage;
    WebDriverWait wait;


    public LoginPage(RemoteWebDriver driver, RegisterPage registerPage){
        this.driver = driver;
        this.registerPage = registerPage;
        //or remove register page from parameter and add in body as this.registerPage=new RegisterPage(driver);
        AjaxElementLocatorFactory ajax = new AjaxElementLocatorFactory(driver, 20);
        PageFactory.initElements(ajax, this);
        wait = new WebDriverWait(driver, 10);
    }

    @FindBy(xpath = "//h2[text()='Login']")
    private WebElement loginTxt;

    @FindBy(id = "floatingInput")
    private WebElement emailTxtBox;

    @FindBy(id = "floatingPassword")
    private WebElement passwordTxtBox;

    @FindBy(xpath = "//button[text()='Login to QTrip']")
    private WebElement loginBtn;

    public boolean isLoginPageNavigationSucceed()throws InterruptedException{
        //Thread.sleep(5000);
        wait.until(ExpectedConditions.and(ExpectedConditions.urlContains("/pages/login")));
        Boolean status = false;
        if(driver.getCurrentUrl().contains(loginPageEndPoint) && loginTxt.getText().equalsIgnoreCase("Login")){
            status =true;
            return status;
        }
        return status;
    }

    public void performLogin(String email, String password, Boolean isUserDymanic) throws InterruptedException{
        if (isUserDymanic) {
        email = registerPage.formattedEmail; 
        }
            // Wait for email input box to be visible before sending keys
         wait.until(ExpectedConditions.visibilityOf(emailTxtBox));
        emailTxtBox.sendKeys(email);

        // Wait for password input box to be visible before sending keys
        wait.until(ExpectedConditions.visibilityOf(passwordTxtBox));
        passwordTxtBox.sendKeys(password);

        // Wait for login button to be clickable before clicking it
        wait.until(ExpectedConditions.elementToBeClickable(loginBtn));
        loginBtn.click();
    }
}
