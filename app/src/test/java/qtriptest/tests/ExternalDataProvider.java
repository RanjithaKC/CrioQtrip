package qtriptest.tests;

import qtriptest.DP;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.DataProvider;

public class ExternalDataProvider {

    @DataProvider(name="qtripData")
        public Object[][] qtripData() throws IOException{
            DP dataProvider = new DP();
            return dataProvider.dpMethod("TestCase01");   
        }

    @DataProvider(name="qtripCityData")
    public Object[][] qtripCityData() throws IOException{
        DP dataProvider = new DP();
        return dataProvider.dpMethod("TestCase02");
    }

    @DataProvider(name="adventureData")
    public Object[][] qtripAdventureData() throws IOException{
        DP dataProvider = new DP();
        return dataProvider.dpMethod("TestCase03");
    }

    @DataProvider(name="testcase4Data")
    public Object[][] testCase4Data() throws IOException{
        DP dataProvider = new DP();
        return dataProvider.dpMethod("TestCase04");
    }

}


    



