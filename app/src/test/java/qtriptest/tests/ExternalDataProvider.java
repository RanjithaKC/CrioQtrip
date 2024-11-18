package qtriptest.tests;

import qtriptest.DP;
import java.io.IOException;
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

    @DataProvider(name="qtripNewUserData")
    public Object[][] qtripNewUserData() throws IOException{
        DP dataProvider = new DP();
        return dataProvider.dpMethod("TestCase03");
    }
    }

