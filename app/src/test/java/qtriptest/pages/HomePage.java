package qtriptest.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class HomePage {
    private RemoteWebDriver driver;
    String homePageUrl = "https://qtripdynamic-qa-frontend.vercel.app/";

    public HomePage(RemoteWebDriver driver){
        this.driver=driver;
        AjaxElementLocatorFactory ajax = new AjaxElementLocatorFactory(driver, 20);
        PageFactory.initElements(ajax, this);
    }

    @FindBy(xpath = "//a[text()='Register']")
    private WebElement registerButton;

    @FindBy(className = "nav-link login")
    private WebElement loginHereButton;

    @FindBy(xpath = "//a[contains(text(),'Reservations')]")
    private WebElement reservationButton;

    @FindBy(xpath = "//a[contains(text(),'Home')]")
    private WebElement homeButton;

    @FindBy(xpath = "//div[contains(text(),'Logout')]")
    private WebElement logoutButton;

    public void navigateToRegisterPage() {
        if(this.driver.getCurrentUrl()!= homePageUrl){
            this.driver.get(homePageUrl);
        }
       // System.out.println("current url = "+driver.getCurrentUrl());
    }

    public Boolean isRegisteredButtonVisible() throws InterruptedException{
        Thread.sleep(3000);
        System.out.println("Verifying registration button displayed and enabled");
        return registerButton.isDisplayed() && registerButton.isEnabled();
    }

    public void clickRegister()throws InterruptedException{
        System.out.println("Navigating to register page");
        registerButton.click();
        Thread.sleep(3000);
    }

    public Boolean isSpecifiedMemberVisible() throws InterruptedException{
        Thread.sleep(5000);
       return logoutButton.getText().equals("Logout");
    }

    public Boolean isUserLoggedIn(){
        return true;
    }

    public void logoutUser(){
        logoutButton.click();
    }

    public static void searchCity(String s){

    }

    public static Boolean assertAutoCompleteText(String s){
        return true;
    }

    public static void selectCity(String s){
        
    }

}
