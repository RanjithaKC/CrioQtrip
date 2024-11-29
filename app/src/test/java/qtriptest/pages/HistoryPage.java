
package qtriptest.pages;

import qtriptest.SeleniumWrapper;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HistoryPage {

    private static  RemoteWebDriver driver;
    public final String historyPageEndPoint = "/reservations/";
    WebDriverWait wait;

    public HistoryPage(RemoteWebDriver driver){
        this.driver = driver;
        AjaxElementLocatorFactory ajax = new AjaxElementLocatorFactory(driver, 20);
        PageFactory.initElements(ajax, this);
        wait = new WebDriverWait(driver, 10);

    }

    @FindBy(xpath = "//table[@class='table']/thead//th")
    private List<WebElement> historyPageHeaders;

    @FindBy(xpath = "//table[@class='table']/tbody/tr/th")
    private List<WebElement> transactionIDColumn;

    @FindBy(xpath = "//table[@class='table']/tbody/tr/td//button[@class='cancel-button']")
    private List<WebElement> cancelButtons;

    @FindBy(xpath = "//table[@class='table']/tbody/tr/td[2]")
    private List<WebElement> adventureNamesColumn;

    @FindBy(xpath = "//div[contains(text(),'Logout')]")
    private WebElement logoutBtn;

    @FindBy(xpath = "//a[text()='Home']")
    private WebElement homeBtn;

    public Boolean isHistoryPageNavigationSuccessful() throws InterruptedException{
        Thread.sleep(2000);
        boolean status = false;
        if(driver.getCurrentUrl().contains(historyPageEndPoint)){
            status = true;
        }
        return status;
    }

    public List<String> getTransactionID(){
        List<String> transactionIDList = new ArrayList<>();
        for(WebElement header : historyPageHeaders){
            if(header.getText().equalsIgnoreCase("Transaction ID")){
                for(WebElement transactionID : transactionIDColumn){
                    String ID = transactionID.getText();
                    transactionIDList.add(ID);
                }
            }
        }
        return transactionIDList;
    }

    public Boolean verifyAllBookingsCancelled() {
        // Retrieve the list of transaction IDs after the page refresh
        boolean status = false;
        List<String> currentTransactionIDs = getTransactionID();
        if (currentTransactionIDs.isEmpty()) {
            status = true;
            System.out.println("All bookings have been successfully cancelled. No Transaction IDs found.");
        } else {
            System.out.println("Some bookings were not cancelled. Transaction IDs still found:");
            for (String id : currentTransactionIDs) {
                System.out.println("Transaction ID: " + id);
            }
        }
        return status;
    }

    public void cancelAllBookings() {
        if (!cancelButtons.isEmpty()) {
            for (WebElement cancelButton : cancelButtons) {
                cancelButton.click();
                System.out.println("Booking cancelled successfully.");
            }
            System.out.println("All bookings have been cancelled.");
        } else {
            System.out.println("No bookings available to cancel.");
        }
    }

    public String cancelBooking() throws InterruptedException {
        boolean status = false;
        String cancelledTransactionID = null;
        // Find the rows that contain transaction IDs and cancel buttons
        for (int i = 0; i < transactionIDColumn.size(); i++) {
            // Get the text of the current transaction ID
            String currentTransactionID = transactionIDColumn.get(i).getText().trim(); 
          //  System.out.println("transaction Id : "+currentTransactionID); 
                // Click the corresponding "Cancel" button
                cancelButtons.get(i).click();
                //Thread.sleep(1000);
                wait.until(ExpectedConditions.invisibilityOf(transactionIDColumn.get(i)));
                System.out.println("Booking with Transaction ID: " + currentTransactionID + " has been cancelled.");
                cancelledTransactionID = currentTransactionID;
                status = true;
                break;
            }
            return cancelledTransactionID;
    }

    public boolean isTransactionIDNotPresent() throws InterruptedException {
        boolean status = false;
        driver.navigate().refresh();
        String cancelledTransactionID = cancelBooking();
        // Re-fetch the transaction IDs after the page refresh
        List<String> currentTransactionIDs = getTransactionID();
        if(currentTransactionIDs.isEmpty()){
            status = true;
            System.out.println("no bookings available");
            return status;
        }
        if(!currentTransactionIDs.isEmpty()){
        for(String transaction :currentTransactionIDs){
            if(!transaction.equals(cancelledTransactionID)){
                status=true;
            }
        }
    }
    return status;  
}

public void clickOnLogout(){
    wait.until(ExpectedConditions.visibilityOf(logoutBtn));
    //logoutBtn.click();
    SeleniumWrapper.click(logoutBtn, driver);
}

public void clickOnHomeBtn() throws InterruptedException {
    Thread.sleep(2000);
    //homeBtn.click();
    SeleniumWrapper.click(homeBtn, driver);
}


}