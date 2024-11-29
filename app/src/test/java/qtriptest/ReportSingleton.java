package qtriptest;

import qtriptest.Utils.Screenshot;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;

public class ReportSingleton {

    static ExtentReports reports;
    static ExtentTest test;

    // initialize or retrieve the ExtentReports instance
    public static ExtentReports getReportInstance(){

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String timestampString = String.valueOf(timestamp.getTime());
        if(reports == null){
            String reportPath = System.getProperty("user.dir") + "/ExtentReports/ExtentReportResults_"+timestampString+".html";
            reports = new ExtentReports(reportPath,true);
            reports.loadConfig(new File(System.getProperty("user.dir")+"/config.xml"));
        }
        return reports;
    }

    // start a test and retrieve the ExtentTest instance
    public static ExtentTest startTest(String testName){
        if (reports == null) {
            getReportInstance(); // Ensure reports is initialized
        }
        test = reports.startTest(testName);
        return test;
    }

    // end the current test
    public static void endTest() {
        if (reports != null && test != null) {
            reports.endTest(test);
        }
    }

     //flush the report (save it to the file system)
     public static void flushReport() {
        if (reports != null) {
            reports.flush();
        }
    }

    // to add screenshot
    public static void getScreenshot(WebDriver driver) throws IOException{
        Screenshot.capture(driver);

    }
}