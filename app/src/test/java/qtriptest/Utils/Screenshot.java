package qtriptest.Utils;

import okhttp3.internal.Util;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Screenshot {

    public static String capture(WebDriver driver) throws IOException{
        String directory = System.getProperty("user.dir") + "/QKARTImages/";
        String screenshotName = System.currentTimeMillis() + "_screenshot.png";
        String filePath = directory + screenshotName;
        try{
            File screenshotDir = new File(directory);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }
        TakesScreenshot ts = (TakesScreenshot)driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        File destination = new File(filePath);
        String errPath = destination.getAbsolutePath();
        FileUtils.copyFile(source, destination);
        return errPath;
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return null; // Return null in case of failure
        }
    }
    
}
