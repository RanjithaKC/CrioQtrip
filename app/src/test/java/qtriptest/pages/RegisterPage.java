package qtriptest.pages;

import qtriptest.SeleniumWrapper;
import java.util.UUID;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RegisterPage {

    private RemoteWebDriver driver;
    private static final String registerPageEndPoint = "/pages/register/";
    static String formattedEmail = " ";
    WebDriverWait wait;
    String registerPageUrl = "https://qtripdynamic-qa-frontend.vercel.app/pages/register/";

    public RegisterPage(RemoteWebDriver driver){
        this.driver=driver;
        AjaxElementLocatorFactory ajax = new AjaxElementLocatorFactory(driver, 20);
        PageFactory.initElements(ajax, this);
        wait = new WebDriverWait(driver, 10);

    }

    @FindBy(xpath = "//h2[contains(text(),'Register')]")
    private WebElement registerTxt;

    @FindBy(id = "floatingInput")
    private WebElement emailTextBox;

    @FindBy(xpath = "//input[@name='password']")
    private WebElement passwordTxtBox;

    @FindBy(xpath = "//input[@name='confirmpassword']")
    private WebElement confirmPasswordTxtBox;

    @FindBy(xpath = "//button[contains(text(),'Register Now')]")
    private WebElement registerNowButton;

    public Boolean isRegistrationPageNavigationSucceed(){
        Boolean status = false;
        SeleniumWrapper.navigate(driver, registerPageUrl);
        if(driver.getCurrentUrl().contains(registerPageEndPoint) || registerTxt.getText().equalsIgnoreCase("Register")){
            status = true;
            return status;
        }
        return status;
    }

    public void registerNewUser(String email, String password, String confirmPassword, Boolean makeUserNameDynamic)throws InterruptedException{
        //Thread.sleep(3000);
        wait.until(ExpectedConditions.visibilityOf(emailTextBox));
       if(makeUserNameDynamic){
           UUID uuid = UUID.randomUUID();
          email = String.format("testmail_%s@email.com",uuid.toString());
           RegisterPage.formattedEmail=email;

           System.out.println("formatted user name on register page : "+formattedEmail);
           //emailTextBox.sendKeys(email);
           SeleniumWrapper.sendKeys(emailTextBox, email);
           //passwordTxtBox.sendKeys(password);
           System.out.println("password on registerpage : "+password);
           SeleniumWrapper.sendKeys(passwordTxtBox, password);
           confirmPassword = password;
           //confirmPasswordTxtBox.sendKeys(confirmPassword);
           System.out.println("confirm password on registerpage : "+confirmPassword);
           SeleniumWrapper.sendKeys(confirmPasswordTxtBox, confirmPassword);
           //registerNowButton.click();
           SeleniumWrapper.click(registerNowButton, driver);
           Thread.sleep(5000);
        }
    }
}
