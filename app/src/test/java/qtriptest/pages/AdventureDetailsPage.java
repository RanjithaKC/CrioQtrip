package qtriptest.pages;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class AdventureDetailsPage {
    private RemoteWebDriver driver;
    String URL = "";

    public AdventureDetailsPage(RemoteWebDriver driver){
        this.driver = driver;
        AjaxElementLocatorFactory ajax = new AjaxElementLocatorFactory(driver, 20);
        PageFactory.initElements(ajax, this);
    }

    public static void bookAdventure(String s, String r, int t){

    }

    public static boolean isBookingSuccessful(){
        return true;
    }
}