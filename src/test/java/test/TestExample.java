package test;

import org.junit.Test;
import pages.GoogleMainPage;
import pages.GoogleResultPage;
import pages.TinkoffJobPage;

public class TestExample extends BaseRunner {

    @Test
    public void test1() {

        GoogleMainPage googleMainPage = app.google;
        googleMainPage.open();
        googleMainPage.openSearchResultsPageByRequest("Тинькофф работа в москве");

        GoogleResultPage googleResultPage = app.googleResults;
        googleResultPage.clickSearchResultsByLinkContains("rabota.tinkoff.ru");

        TinkoffJobPage tinkoffJob = app.tinkoffJob;
        tinkoffJob.switchToWindow("Работа с Тинькофф");
        tinkoffJob.typeNameField("Иванов Иван Иванович");

        tinkoffJob.getPage("https://www.tinkoff.ru/career/vacancies/");
        tinkoffJob.switchToWindow("Вакансии");
        tinkoffJob.typeNameField("Иванова");
        tinkoffJob.checkPopularNameRequest("Иванова");
        tinkoffJob.closeCurrentTab();
        tinkoffJob.switchToMainTab();

        googleResultPage.inputBySearchField("Проба");
    }
}
