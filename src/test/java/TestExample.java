import org.junit.Test;

public class TestExample extends BaseRunner {

    @Test
    public void test1() {
        app.getPage("https://www.google.ru/");
        app.openSearchResultsPageByRequest("Тинькофф работа в москве");
        app.clickSearchResultsByLinkContains("rabota.tinkoff.ru");
        app.switchToWindow("Работа с Тинькофф");
        app.typeNameField("Иванов Иван Иванович");
        app.getPage("https://www.tinkoff.ru/career/vacancies/");
        app.switchToWindow("Вакансии");
        app.typeNameField("Иванова");
        app.checkPopularNameRequest("Иванова");
        app.closeCurrentTab();
        app.switchToMainTab();
    }
}
