import net.lightbody.bmp.core.har.Har;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TestExample extends BaseRunner {

    Logger logger = LoggerFactory.getLogger(TestExample.class);

    @Test
    public void test1() {
        proxy.newHar();
        driver.get("https://www.google.ru/");

        Har har = proxy.endHar();
        har.getLog().getEntries().forEach(harEntry -> logger.info(harEntry.getResponse().getStatus() +
                " : " + harEntry.getRequest().getUrl()));

        proxy.stop();

//        driver.findElement(By.name("q")).sendKeys("Тинькофф работа в москве");
//        driver.findElements(By.xpath("//ul[@role='listbox']/li"));
//
//        //ожидание, игнорирующее StaleElementReferenceException
//        wait
//                .ignoring(StaleElementReferenceException.class)
//                .withMessage("Что-то пошло не так...")
//                .pollingEvery(Duration.ofMillis(500))
//                .until(d -> {
//                    //список поисковой выдачи
//                    By listItems = By.xpath("//ul[@role='listbox']/li[@role='presentation' and .//*[@role='option']]");
//                    List<WebElement> elements = driver.findElements(listItems);
//                    for (WebElement el : elements) {
//                        System.out.println(el.getText());
//                        //из списка вариантов дожиаемся появления нужного, кликаем
//                        if (el.getText().equals("тинькофф работа в москве")) el.click();
//                        break;
//                    }
//                    //Ожидание появления заголовка
//                    return d.getTitle().equals("тинькофф работа в москве - Поиск в Google");
//                });
//
//
//        String tinkoffJobLink = "rabota.tinkoff.ru";
//
//        wait.until(d -> xpathSearcherByText(tinkoffJobLink).size() > 0);
//        xpathSearcherByText(tinkoffJobLink).get(0).click();
//
//        //сайт открывается в новом окне, явно переключаемся к окну с заголовком  "Работа с Тинькофф"
//        wait.until(d -> {
//            boolean check = false;
//            for (String title : driver.getWindowHandles()) {
//                driver.switchTo().window(title);
//                System.out.println(d.getTitle());
//                check = d.getTitle().equals("Работа с Тинькофф");
//            }
//            return check;
//        });
//
//        //Заполняем форму максиально быстро, пытаясь игнорировать анимацию страницы
//        wait.ignoring(StaleElementReferenceException.class)
//                .ignoring(ElementNotInteractableException.class)
//                .until(d -> {
//                    driver.findElement(By.name("name")).sendKeys("Иванов Иван Иванович");
//                    driver.findElement(By.name("email")).click();
//                    return true;
//                });
//
//        driver.get("https://www.tinkoff.ru/career/vacancies/");
//        wait.until(d -> d.getTitle().equals("Вакансии"));
//
//        String surname = "Иванова";
//        driver.findElement(By.name("name")).sendKeys(surname);
//        wait.until(d -> checkPopularNameRequest(surname));
//
//        //закрываем активную вкладку
//        driver.close();
//        logger.info("Закрыта активная вкладка");
//
//        //переключаемся к первой попавшейся вкладке (а у нас она теперь одна)
//        driver.switchTo().window(driver.getWindowHandles().iterator().next());
//
//        //By можно сделать свой на базе сеществующего
//        By searchField = By.name("q");
//        String testText = "Проба ввода текста";
//
//        /*демонстрация Actions
//        на поисковое поле устанавливаетя фокус
//        выполняется клик
//        с помощью сочетания клавиш CTRL+A+DELETE очищается содержимое поля
//        делее, заполняется поле новым значением
//         */
//        Actions actions = new Actions(driver);
//        actions.moveToElement(driver.findElement(searchField))
//                .click()
//                .sendKeys(Keys.CONTROL + "a" + Keys.DELETE)
//                .sendKeys(testText)
//                .sendKeys(Keys.ENTER)
//                .perform();
//
//        //с полем input getText()  не сработает, значение нужно явно забрать из атрибута value
//        wait.until(d -> d.findElement(searchField)
//                .getAttribute("value")
//                .equals(testText));
    }

    //универсальный xpath локатор, вернет все элементы, содержащие текст
    private List<WebElement> xpathSearcherByText(String searchText) {
        String xpath = String.format("//*[contains(text(),'%s')]", searchText);
        return driver.findElements(By.xpath(xpath));
    }

    private boolean checkPopularNameRequest(String surname) {
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
    }
}
