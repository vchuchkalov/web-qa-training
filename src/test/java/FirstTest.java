import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
public class FirstTest {
    private WebDriver driver;
    private String baseUrl;
    @Before
    public void setUp() {
        driver = new ChromeDriver();
        baseUrl = "https://moscow-job.tinkoff.ru/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }
    @Test
    public void testFirst() throws InterruptedException {
        driver.get(baseUrl);
        driver.findElement(By.name("fio")).click();
        driver.findElement(By.name("fio")).clear();
        driver.findElement(By.name("fio")).sendKeys("Иван");
        driver.findElement(By.name("email")).sendKeys(Keys.TAB);
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='обработку личных данных'])[1]/following::div[2]")).click();
        assertEquals("Недостаточно информации. Введите фамилию, имя и отчество через пробел (Например: Иванов Иван Алексеевич)", driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Фамилия, имя и отчество'])[1]/following::div[3]")).getText());
    }
    @After
    public void tearDown() {
        driver.quit();
    }
}
