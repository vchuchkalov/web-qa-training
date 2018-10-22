import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BaseRunner {
    private static ThreadLocal<WebDriver> tl = new ThreadLocal<>();
    WebDriver driver;
    private String browserName = System.getProperty("browser");
    String baseUrl;

//    @Before
//    public void setUp() {
//        driver = getDriver();
//        driver.manage().window().maximize();
//        baseUrl = "https://moscow-job.tinkoff.ru/";
//        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
//    }

    @Before
    public void setUp(){
        if (tl.get() != null) {
            driver = tl.get();
        } else {
            driver = getDriver();
            tl.set(driver);
        }
        driver.manage().window().maximize();
        baseUrl = "https://moscow-job.tinkoff.ru/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            driver.quit();
            driver = null;
        }));
    }

    @After
    public void tearDown() {
//        driver.quit();
    }

    private WebDriver getDriver() {
        try {
            BrowsersFactory.valueOf(System.getProperty("browser"));
        } catch (NullPointerException | IllegalArgumentException e) {
            browserName = randomBrowserPicker();
            System.setProperty("browser", browserName);
        }
        return BrowsersFactory.valueOf(browserName).create();
    }
    private String randomBrowserPicker() {
        System.out.println("\nThe driver is not set. Running a random browser...");
        int pick = new Random().nextInt(BrowsersFactory.values().length);
        return BrowsersFactory.values()[pick].toString();
    }
}