import org.junit.Test;
import org.openqa.selenium.By;

import static org.junit.Assert.assertEquals;

public class FirstTest extends BaseRunner {

    @Test
    public void testFirst() {
        driver.get(baseUrl);
        driver.findElement(By.name("fio")).click();
        driver.findElement(By.name("email")).click();
        driver.findElement(By.name("phone")).click();
        driver.findElement(By.name("city")).click();
        driver.findElement(By.cssSelector(".SelectItem__placeholder_3_ypX")).click();
        driver.findElement(By.cssSelector("div.SelectItem__contentWrapper_3eEeN")).click();
        driver.findElement(By.cssSelector(".FormField__lastRow_18MZF:nth-child(2)")).click();

        assertEquals("Поле обязательное", driver
                .findElement(By.cssSelector("div.Error__errorMessage_q8BBY")).getText());
        assertEquals("Поле обязательное", driver
                .findElement(By.cssSelector(".Row__row_AjrJL:nth-child(2) > .FormField__field_1iwkM:nth-child(1) > .Error__errorMessage_q8BBY")).getText());
        assertEquals("Необходимо указать номер телефона", driver
                .findElement(By.cssSelector(".FormField__field_1iwkM:nth-child(2) > .Error__errorMessage_q8BBY")).getText());
        assertEquals("Поле обязательное", driver
                .findElement(By.cssSelector(".Row__row_AjrJL:nth-child(3) .Error__errorMessage_q8BBY")).getText());
        assertEquals("Поле обязательное", driver
                .findElement(By.cssSelector(".Row__row_AjrJL:nth-child(4) .Error__errorMessage_q8BBY")).getText());
    }
}
