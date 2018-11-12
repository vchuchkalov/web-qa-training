import org.junit.Test;

public class TestExample extends BaseRunner {

    @Test
    public void test1() {
        app.getPage("https://www.google.ru/")
                .openSearchResultsPageByRequest("Тинькофф работа в москве")
                .clickSearchResultsByLinkContains("rabota.tinkoff.ru")
                .switchToWindow("Работа с Тинькофф")
                .typeNameField("Иванов Иван Иванович")
                .getPage("https://www.tinkoff.ru/career/vacancies/")
                .switchToWindow("Вакансии")
                .typeNameField("Иванова")
                .checkPopularNameRequest("Иванова")
                .closeCurrentTab()
                .switchToMainTab()
                .inputBySearchField("Проба");
    }
}
