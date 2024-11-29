package qtriptest.pages;

import qtriptest.SeleniumWrapper;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.formula.functions.Index;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AdventurePage {

    private RemoteWebDriver driver;
    private final String adventurePageEndPoint = "/pages/adventures/";
    private int initialCount = 0;
    WebDriverWait wait;

    public AdventurePage(RemoteWebDriver driver){
        this.driver=driver;
        AjaxElementLocatorFactory ajax = new AjaxElementLocatorFactory(driver, 20);
        PageFactory.initElements(ajax, this);
        wait = new WebDriverWait(driver, 10);
    }

    @FindBy(xpath = "//h1[contains(text(),'Explore all adventures')]")
    private WebElement exploreAllAdventureTxt;

    @FindBy(id = "category-select")
    private WebElement categoryDropDown;

    @FindBy(id = "duration-select")
    private WebElement durationDropDown;

    @FindBy(xpath = "//div[@class='activity-card']//div[contains(@class,'d-block')]//p")
    private List<WebElement> durationResults;

    @FindBy(className = "category-banner")
    private List<WebElement> categoryOnCard;

    @FindBy(className = "activity-card")
    private List<WebElement> activityCards;


    @FindBy(xpath = "//div[contains(@onclick,'clearDuration(event)')]")
    private WebElement durationClearElement;

    @FindBy(xpath = "//div[contains(@onclick,'clearCategory(event)')]")
    private WebElement categoryClearElement;

    @FindBy(xpath = "//a[contains(text(),'Home')]")
    private WebElement homeButton;

    @FindBy(xpath = "//div[@class='activity-card']//div//h5")
    private List<WebElement> adventureNames;

    public boolean isAdventurePageNavigationSucceed()throws InterruptedException{
       // Thread.sleep(5000);
        wait.until(ExpectedConditions.urlContains(adventurePageEndPoint));
        wait.until(ExpectedConditions.textToBePresentInElement(exploreAllAdventureTxt, "Explore all adventures"));
        Boolean status = false;
        if(driver.getCurrentUrl().contains(adventurePageEndPoint) && exploreAllAdventureTxt.getText().equalsIgnoreCase("Explore all adventures")){
            status =true;
            return status;
        }
        return status;
    }

    public Boolean setFilterByHour(String text) throws InterruptedException{
        boolean status = false;
        String selectedValue = AdventurePage.handlingDropDown(durationDropDown, text);
        Thread.sleep(3000);
        if(selectedValue.equalsIgnoreCase(text)){
            status=true;
        }
        return status;
    }

    public Boolean verifyDurationResults(String text){
        boolean status = false;
        String[] inputRange = text.split("-");
        int minDuration = Integer.parseInt(inputRange[0].trim());
        int maxDuration = Integer.parseInt(inputRange[1].replaceAll("\\D", ""));
        System.out.println("minDuration : "+minDuration);
        System.out.println("maxDuration : "+maxDuration);
        List<String> sortedDurationList = addDurationResultsToList();
        for(String duration : sortedDurationList){
            int hour = Integer.parseInt(duration.replaceAll("\\D", ""));
            //System.out.println("hour : "+hour);
            if(hour>=minDuration && hour<=maxDuration){
                status = true;
                System.out.println("Duration " + hour + " Hours is within the filter range.");
            }else{
                System.out.println("Duration " + hour + " Hours is outside the filter range.");
            }
        }
        return status;
    }

    public List<String> addDurationResultsToList(){
        List<String> durationList = new ArrayList<>();    
        for(WebElement durationResult : durationResults){
          // System.out.println(durationResult.getText());
            if(durationResult.getText().contains("Hours")){
                durationList.add(durationResult.getText());
            }
        }
        return durationList;
    }

    public void setCategoryValue(String categoryTxt){
        handlingDropDown(categoryDropDown, categoryTxt);
        wait.until(ExpectedConditions.numberOfElementsToBeLessThan(By.xpath("//div[@class='activity-card']"), 5));
    }

    public Boolean verifyCategoryOnEachCard(String categoryTxt) throws InterruptedException{
        //Thread.sleep(5000);
        wait.until(ExpectedConditions.visibilityOfAllElements(categoryOnCard));
        boolean status = true;
       // String categoryTxtFirstWord = categoryTxt.split(" ")[0];
        String categoryTxtFirstWord = categoryTxt.substring(0,categoryTxt.indexOf(" "));
        for(WebElement category : categoryOnCard){
           // System.out.println("incoming category : "+categoryTxtFirstWord);
           // System.out.println("category on card : "+category.getText());
            if(!category.getText().contains(categoryTxtFirstWord)){
                status = false;
                return status;
            }
        }
        return status;
    }

    public int getResultCount(){
       this.initialCount = activityCards.size();
      // System.out.println("initial count : "+initialCount);
       return initialCount;
    }

    public void clearDurationFilter(){
        durationClearElement.click();
    }

    public void clearCategoryFilter(){
        categoryClearElement.click();
    }

    public Boolean verifyAllRecords() throws InterruptedException{
        Thread.sleep(3000);
        int finalCount = activityCards.size();
      //  System.out.println("initial count : "+initialCount);
      //  System.out.println("final count : "+finalCount);
        if(initialCount == finalCount){
            return true;
        }
        return false;
    }

    public void clickOnHomeButton() throws InterruptedException{
       // Thread.sleep(3000);
       wait.until(ExpectedConditions.visibilityOf(homeButton));
       // homeButton.click();
       SeleniumWrapper.click(homeButton, driver);
    }

    public void selectAdventure(String adventureTxt) throws InterruptedException{
       // Thread.sleep(3000);
       wait.until(ExpectedConditions.visibilityOfAllElements(adventureNames));
        for(WebElement adventure : adventureNames){
          //  System.out.println("adventure name : "+adventure.getText());
            if(adventure.getText().equalsIgnoreCase(adventureTxt)){
                adventure.click();
                break;
            }
        }
    }

    public static String handlingDropDown(WebElement element,String text){
        Select s = new Select(element);
        s.selectByVisibleText(text);
        WebElement selectedOption = s.getFirstSelectedOption();
      //  System.out.println("selected option : "+selectedOption.getText());
        return selectedOption.getText();
    }
}