package qtriptest;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SeleniumWrapper {

    public static boolean click(WebElement elementToClick, WebDriver driver){
            if(elementToClick.isDisplayed()){
                scrolling(driver, elementToClick);
                elementToClick.click();
                return true;
            }
            return false;
    }

    public static void sendKeys(WebElement inputBox, String keysToSend){
        inputBox.clear();
        inputBox.sendKeys(keysToSend);
    }

    public static boolean navigate(WebDriver driver, String url){
        String currentUrl = driver.getCurrentUrl();
        if(!currentUrl.equals(url)){
            //driver.get(url);
            driver.navigate().to(url);
        }
        String urlAfterNavigation = driver.getCurrentUrl();
        if(urlAfterNavigation.equals(url)){
            return true;
        }
        return false;
    }

    public static WebElement findElementWithRetry(WebDriver driver, By locator, int retryCount){
        int attempts =0;
        while(attempts<retryCount){
            try{
            WebElement element = driver.findElement(locator);
            System.out.println("Element found on attempt " + (attempts + 1));
            return element;

            } catch (NoSuchElementException e) {
            attempts++;
            System.out.println("Element not found at Attempt " + attempts + " of " + retryCount);
            }
        }
        throw new NoSuchElementException("Element not found after " + retryCount + " attempts: " + locator.toString());
    }

    public static void scrolling(WebDriver driver, WebElement ele){
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].scrollIntoView(true)",ele);
    }
}
