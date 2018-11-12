package pages;

import app.Application;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.PageFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class TinkoffJobPage extends Page {
    public TinkoffJobPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void typeNameField(String value){
        //Заполняем форму максиально быстро, пытаясь игнорировать анимацию страницы
        wait.ignoring(StaleElementReferenceException.class)
                .ignoring(ElementNotInteractableException.class)
                .until(d -> {
                    driver.findElement(By.name("name")).sendKeys(value);
                    driver.findElement(By.name("email")).click();
                    return true;
                });
    }

    public void checkPopularNameRequest(String surname) {
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
    }
}
