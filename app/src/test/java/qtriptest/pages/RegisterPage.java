package qtriptest.pages;

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

           System.out.println("formatted user name : "+formattedEmail);
           emailTextBox.sendKeys(email);
           passwordTxtBox.sendKeys(password);
           confirmPassword = password;
           confirmPasswordTxtBox.sendKeys(confirmPassword);
           registerNowButton.click();
        }
    }
}
