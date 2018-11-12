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

import java.io.UnsupportedEncodingException;
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
    public final String browserName = System.getProperty("browser") == null ? "chrome" : System.getProperty("browser");


    public Application() {
        driver = new EventFiringWebDriver(getDriver());
        ((EventFiringWebDriver) driver).register(new BrowsersFactory.MyListener());
        wait = new WebDriverWait(driver, 10);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    public void quit() {
        driver.quit();
        driver = null;
    }

    public Application getPage(String url) {
        driver.navigate().to(url);
        return this;
    }

    public boolean isLoadedByTitleContains(String substring) {
        wait.until(d -> d.getTitle().contains(substring));
        return true;
    }

    public Application openSearchResultsPageByRequest(String request) {
        driver.findElement(By.name("q")).sendKeys(request);
        driver.findElements(By.xpath("//ul[@role='listbox']/li"));

        //ожидание, игнорирующее StaleElementReferenceException
        wait
                .ignoring(StaleElementReferenceException.class)
                .withMessage("Что-то пошло не так...")
                .pollingEvery(Duration.ofMillis(500))
                .until(d -> {
                    //список поисковой выдачи
                    By listItems = By.xpath("//ul[@role='listbox']/li[@role='presentation' and .//*[@role='option']]");
                    List<WebElement> elements = driver.findElements(listItems);
                    for (WebElement el : elements) {
                        System.out.println(el.getText());
                        //из списка вариантов дожиаемся появления нужного, кликаем
                        if (el.getText().equals(request.toLowerCase())) el.click();
                        break;
                    }
                    //Ожидание появления заголовка
                    return d.getTitle().contains(request.toLowerCase() + " - Поиск в Google");
                });
        return this;
    }

    public Application clickSearchResultsByLinkContains(String link){
        wait.until(d -> xpathSearcherByText(link).size() > 0);
        xpathSearcherByText(link).get(0).click();
        return this;
    }

    public Application switchToWindow(String windowName){
        wait.until(d -> {
            boolean check = false;
            for (String title : driver.getWindowHandles()) {
                driver.switchTo().window(title);
                System.out.println(d.getTitle());
                check = d.getTitle().equals(windowName);
            }
            return check;
        });
        return this;
    }

    public Application typeNameField(String value){
        //Заполняем форму максиально быстро, пытаясь игнорировать анимацию страницы
        wait.ignoring(StaleElementReferenceException.class)
                .ignoring(ElementNotInteractableException.class)
                .until(d -> {
                    driver.findElement(By.name("name")).sendKeys(value);
                    driver.findElement(By.name("email")).click();
                    return true;
                });
        return this;
    }

    public Application checkPopularNameRequest(String surname) {
       wait.until(d-> {
           boolean match = false;
           String messageResponse;
           String response;
           List<String> responseList = new ArrayList<>();
           for (LogEntry entry : driver.manage().logs().get(LogType.PERFORMANCE)) {
               messageResponse = entry.getMessage();
               if (messageResponse.contains("get_popular_names") && messageResponse.contains("requestWillBeSent")) {
                   JSONObject jsonObject;
                   try {
                       jsonObject = new JSONObject(messageResponse);
                       response = jsonObject.getJSONObject("message").getJSONObject("params").getJSONObject("request").get("postData").toString();
                       response = URLDecoder.decode(response, "UTF-8");
                       logger.info(response);
                       responseList.add(response);
                   } catch (JSONException e) {
                       logger.error("JSONException: Ошибка преобразования JSON объекта, для получения параметров операции", e);
                   } catch (UnsupportedEncodingException e) {
                       logger.error("UnsupportedEncodingException: Ошибка преобразования строки в utf-8", e);
                   } catch (NullPointerException e) {
                       logger.error("NullPointerException: Метод get_popular_names не выполнился", e);
                   }
               }
           }
           for (String res : responseList) {
               if (res.contains(surname)) {
                   match = true;
                   break;
               }
           }
           return match;
       });
        return this;
    }

    public Application switchToMainTab(){
        driver.switchTo().window(driver.getWindowHandles().iterator().next());
        return this;
    }

    public void inputBySearchField(String value){
        By searchField = By.name("q");
        String testText = "Проба ввода текста";

        /*демонстрация Actions
        на поисковое поле устанавливаетя фокус
        выполняется клик
        с помощью сочетания клавиш CTRL+A+DELETE очищается содержимое поля
        делее, заполняется поле новым значением
         */
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(searchField))
                .click()
                .sendKeys(Keys.CONTROL + "a" + Keys.DELETE)
                .sendKeys(testText)
                .sendKeys(Keys.ENTER)
                .perform();

        //с полем input getText()  не сработает, значение нужно явно забрать из атрибута value
        wait.until(d -> d.findElement(searchField)
                .getAttribute("value")
                .equals(testText));
    }

    //универсальный xpath локатор, вернет все элементы, содержащие текст
    public List<WebElement> xpathSearcherByText(String searchText) {
        String xpath = String.format("//*[contains(text(),'%s')]", searchText);
        return driver.findElements(By.xpath(xpath));
    }

    public Application closeCurrentTab(){
        driver.close();
        logger.info("Закрыта активная вкладка");
        return this;
    }

    private WebDriver getDriver() {
        return BrowsersFactory.buildDriver(browserName);
    }

}
