package qtriptest.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class LoginPage {

    private RemoteWebDriver driver;
    private final String loginPageEndPoint = "/pages/login";
    RegisterPage registerPage;

    public LoginPage(RemoteWebDriver driver, RegisterPage registerPage){
        this.driver = driver;
        this.registerPage = registerPage;
        //or remove register page from parameter and add in body as this.registerPage=new RegisterPage(driver);
        AjaxElementLocatorFactory ajax = new AjaxElementLocatorFactory(driver, 20);
        PageFactory.initElements(ajax, this);
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
        Thread.sleep(5000);
        Boolean status = false;
        if(driver.getCurrentUrl().contains(loginPageEndPoint) && loginTxt.getText().equalsIgnoreCase("Login")){
            status =true;
            return status;
        }
        return status;
    }

    public void performLogin(String email, String password, Boolean isUserDymanic) throws InterruptedException{

        if(isUserDymanic){
            email = registerPage.formattedEmail; 
        }
        //Actions action = new Actions(driver);
        Thread.sleep(3000);
        //action.moveToElement(this.emailTxtBox).sendKeys(this.emailTxtBox, email).build().perform();
        emailTxtBox.sendKeys(email);       
        passwordTxtBox.sendKeys(password);
        loginBtn.click();
    }
}
