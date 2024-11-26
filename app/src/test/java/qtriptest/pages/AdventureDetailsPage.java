package qtriptest.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.FindBy;

public class AdventureDetailsPage {
    private RemoteWebDriver driver;
    private final String detailAdventurePageEndPoint = "/detail/";
    WebDriverWait wait;

    public AdventureDetailsPage(RemoteWebDriver driver){
        this.driver = driver;
        AjaxElementLocatorFactory ajax = new AjaxElementLocatorFactory(driver, 20);
        PageFactory.initElements(ajax, this);
        wait = new WebDriverWait(driver, 10);
    }

    @FindBy(name = "name")
    private WebElement nameTxtBox;

    @FindBy(name = "date")
    private WebElement dataTxtBox;

    @FindBy(name = "person")
    private WebElement countTxtBox;

    @FindBy(className = "reserve-button")
    private WebElement reserveButton;

    @FindBy(id = "reservation-person-cost")
    private WebElement perPersonCost;

    @FindBy(id = "reservation-cost")
    private WebElement totalReservationCost;

    @FindBy(id = "reserved-banner")
    private WebElement bookingSuccessfullMsg;

    @FindBy(xpath = "//a[contains(text(),'Reservations')]")
    private WebElement reservationLinkTxt;

    public boolean isDetailAdventurePageNavigationSucceed()throws InterruptedException{
        //Thread.sleep(5000);
        wait.until(ExpectedConditions.urlContains(detailAdventurePageEndPoint));
        Boolean status = false;
        if(driver.getCurrentUrl().contains(detailAdventurePageEndPoint)){
            status =true;
        }
        return status;
    }

    public void bookAdventure(String guestName, String date , String count) throws InterruptedException{
        //Thread.sleep(2000);
        wait.until(ExpectedConditions.visibilityOf(nameTxtBox));
        nameTxtBox.sendKeys(guestName);
        dataTxtBox.sendKeys(date);
        countTxtBox.clear();
        countTxtBox.sendKeys(count);
        Thread.sleep(2000);
       boolean status = isTotalCostIsCorrect(count);
       System.out.println("total cost correctness : "+status);
        reserveButton.click();
        Thread.sleep(3000);
    }

    public Boolean isTotalCostIsCorrect(String count){
        boolean status = false;
       int perPersonPrice = Integer.parseInt(perPersonCost.getText());
       int totalHeadCount = Integer.parseInt(count);
      // int totalPrice =(perPersonPrice*count);
      int totalPrice = perPersonPrice*totalHeadCount;
       String totalCost = String.valueOf(totalPrice);
       if(totalReservationCost.getText().equals(totalCost)){
        System.out.println("total cost : "+totalReservationCost.getText());
        status = true;
       }
       return status;
    }

    public boolean isBookingSuccessful() throws InterruptedException{
        //Thread.sleep(3000);
        wait.until(ExpectedConditions.attributeContains(bookingSuccessfullMsg, "class", "alert alert-success"));
        boolean status = false;
       // System.out.println("booking msg : "+bookingSuccessfullMsg.getText());
       // System.out.println("booking attribute : "+bookingSuccessfullMsg.getAttribute("class"));
       // if(bookingSuccessfullMsg.getAttribute("class").equals("alert alert-success")){
        if(bookingSuccessfullMsg.getText().contains("successful")){
            status=true;
        }
        return status;
    }

    public void clickOnReservation(){
        reservationLinkTxt.click();
    }
}