package qtriptest.tests;

import qtriptest.DP;
import java.io.IOException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DataProviderImpl {

    // @DataProvider (name = "qtripData")
    // public Object[][] qtripData() throws IOException{
    //     DP dataProvider = new DP();
    //     return dataProvider.dpMethod("TestCase01");
    // }
    
    @Test(dataProvider="qtripData",dataProviderClass = ExternalDataProvider.class, enabled=false)
    
    public static void testDataProvider(String userName, String password){
        System.out.println("UserName :: "+userName+ " || "+ "Password :: "+password);
    }
    
    @Test(dataProvider="qtripCityData",dataProviderClass = ExternalDataProvider.class, enabled=false)
    public static void cityTestDataProvider(String cityName, String categoryFilter, String durationFilter, String expectedFilteredResults, String expectedUnFilteredResults){
        System.out.println("CityName :: "+cityName+ " || "+ "CategoryFilter :: "+categoryFilter+ " || "+"DurationFilter :: "+durationFilter+ " || "+ "ExpectedFilteredResults :: "+expectedFilteredResults+ " || "+ "ExpectedUnFilteredResults :: "+expectedUnFilteredResults);
    }

    @Test(dataProvider="qtripNewUserData",dataProviderClass = ExternalDataProvider.class, enabled=false)
    public static void newUserTestDataProvider(String newUserName, String password, String searchCity, String adventureName, String guestName, String date, String count){
        System.out.println("NewUserName :: "+newUserName+ " || "+ "Password :: "+password+ " || "+"SearchCity :: "+searchCity+ " || "+ "AdventureName :: "+adventureName+ " || "+ "GuestName :: "+guestName+ " || "+ "Date :: "+date+ " || "+ "Count :: "+count);
    }

}
//above code(@dataprovider) is just for example. data provider is test class level so in order to use it on multiple classes
//without duplication of code we have different approach like utility.
//will move this method from test level to class level like ExternalDataProvider.java
// and we have to specify dataprovider class along with dataprovider name