package qtriptest.pages;

import qtriptest.SeleniumWrapper;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    private RemoteWebDriver driver;
    WebDriverWait wait;

    public HomePage(RemoteWebDriver driver){
        this.driver=driver;
        AjaxElementLocatorFactory ajax = new AjaxElementLocatorFactory(driver, 20);
        PageFactory.initElements(ajax, this);
        wait = new WebDriverWait(driver, 10);
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

    @FindBy(id = "autocomplete")
    private WebElement searchTxtBox;

    @FindBy(xpath = "//ul[@id='results']//h5")
    private WebElement noCityFoundText;

    @FindBy(xpath = "//ul[@id='results']//li")
    private WebElement searchResults;

    @FindBy(id = "results")
    private WebElement autoCompleteTxt;

    @FindBy(xpath = "//a[text()='Home']")
    private WebElement homeBtn;

    public Boolean isRegisteredButtonVisible() throws InterruptedException{
       // Thread.sleep(3000);
       wait.until(ExpectedConditions.elementToBeClickable(registerButton));
        System.out.println("Verifying registration button displayed and enabled");
        return registerButton.isDisplayed() && registerButton.isEnabled();
    }

    public void clickRegister()throws InterruptedException{
        System.out.println("Navigating to register page");
        //registerButton.click();
        //Thread.sleep(3000);
        SeleniumWrapper.click(registerButton, driver);
    }

    public Boolean isSpecifiedMemberVisible() throws InterruptedException{
        //Thread.sleep(5000);
        wait.until(ExpectedConditions.visibilityOf(logoutButton));
       return logoutButton.getText().equals("Logout");
    }

    public Boolean isUserLoggedIn() throws InterruptedException{
        //Thread.sleep(5000);
        wait.until(ExpectedConditions.visibilityOf(logoutButton));
        return logoutButton.getText().equals("Logout");
    }

    public void logoutUser() throws InterruptedException{
       // logoutButton.click();
       SeleniumWrapper.click(logoutButton, driver);
       Thread.sleep(3000);
    }

    public void searchCity(String city) throws InterruptedException{
        Thread.sleep(3000);
        wait.until(ExpectedConditions.visibilityOf(searchTxtBox));
        // searchTxtBox.clear();
        // searchTxtBox.sendKeys(city);
        SeleniumWrapper.sendKeys(searchTxtBox, city);
    }

    public Boolean assertAutoCompleteText(String city) throws InterruptedException{
        Thread.sleep(3000);
         boolean isdisplayed = false;       
    try {
        // Check if the search result element is visible and matches the city
        if (wait.until(ExpectedConditions.visibilityOf(searchResults)).getText().equals(city) && searchResults.isDisplayed()) {
            isdisplayed = true;
        }
    } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException e) {
        System.out.println("Search result element not found for city: " + city);
        System.out.println("Exception occured : "+e.getMessage());
    }
    // If the first condition failed, let's check for the "No City found" message
    if (!isdisplayed) {
        try {
            // Check if the "No City found" message is visible
            if (wait.until(ExpectedConditions.visibilityOf(noCityFoundText)).getText().equalsIgnoreCase("No City found") && noCityFoundText.isDisplayed()) {
                isdisplayed = true;
            }
        } catch (TimeoutException | NoSuchElementException e) {
            System.out.println("\"No City found\" message not visible: " + e.getMessage());
        }
    }
    return isdisplayed;
}
       
    public Boolean selectCity(String city) throws InterruptedException{
        Thread.sleep(2000);
        wait.until(ExpectedConditions.visibilityOf(searchResults));
        boolean status = false;
       try{ 
        if(searchResults.getText().equals(city)){
            status = true;
            //searchResults.click();
            SeleniumWrapper.click(searchResults, driver);
        }
    }catch (NoSuchElementException | StaleElementReferenceException e) {
        System.out.println("\"No City found\" message not visible: " + e.getMessage());
    } 
       return status;
    }
    public void clickOnHomeBtn() throws InterruptedException {
        Thread.sleep(2000);
       // homeBtn.click();
       SeleniumWrapper.click(homeBtn, driver);
    }

}
