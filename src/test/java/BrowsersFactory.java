import com.google.common.io.Files;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

import java.io.File;
import java.io.IOException;


public class BrowsersFactory {

    public static class MyListener extends AbstractWebDriverEventListener {

        @Override
        public void beforeFindBy(By by, WebElement element, WebDriver driver) {
            System.out.println("Обращение к элементу " + by);
        }

        @Override
        public void afterFindBy(By by, WebElement element, WebDriver driver) {
            System.out.println("Найден элемент " + by);
        }

        @Override
        public void onException(Throwable throwable, WebDriver driver) {
            File tmp = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File file = new File("target", "sccreen-" + System.currentTimeMillis() + ".png");
            try {
                Files.copy(tmp, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.err.println(file.getAbsolutePath());
        }
    }

    static WebDriver buildDriver(String browserName) {
        switch (browserName) {

            case "chrome_invisible":
                ChromeOptions chromeInvisibleOpt = new ChromeOptions();
                chromeInvisibleOpt.addArguments("--disable-notifications");
                chromeInvisibleOpt.addArguments("--headless");
                return new ChromeDriver(chromeInvisibleOpt);

            case "firefox":
                //Disable login to console and redirect log to an external file
                System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
                System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "./src/test/java/firefox_logs/log");

                FirefoxOptions ffOpt = new FirefoxOptions();
                ffOpt.addPreference("dom.webnotifications.enabled", false);
                return new FirefoxDriver(ffOpt);

            default:
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-notifications");
                return new ChromeDriver(options);
        }
    }
}