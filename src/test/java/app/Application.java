package app;

import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.GoogleMainPage;
import pages.GoogleResultPage;
import pages.TinkoffJobPage;
import test.BrowsersFactory;

import java.io.UnsupportedEncodingException;
import java.lang.management.GarbageCollectorMXBean;
import java.net.URLDecoder;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Иван on 16.04.2018.
 */
public class Application {

    Logger logger = LoggerFactory.getLogger(Application.class);
    private WebDriverWait wait;
    private WebDriver driver;
    public GoogleMainPage google;
    public GoogleResultPage googleResults;
    public TinkoffJobPage tinkoffJob;
    public final String browserName = System.getProperty("browser") == null ? "chrome" : System.getProperty("browser");


    public Application() {
        driver = new EventFiringWebDriver(getDriver());
        ((EventFiringWebDriver) driver).register(new BrowsersFactory.MyListener());
        wait = new WebDriverWait(driver, 10);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        //page
        google = new GoogleMainPage(driver);
        googleResults = new GoogleResultPage(driver);
        tinkoffJob = new TinkoffJobPage(driver);
    }

    public void quit() {
        driver.quit();
        driver = null;
    }

    private WebDriver getDriver() {
        return BrowsersFactory.buildDriver(browserName);
    }

}
