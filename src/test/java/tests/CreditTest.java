package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import db.DbUtils;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import pages.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
    }

    @AfterEach
    void tearDown() {
        DbUtils.clearTables();
    }

    @Test
    void shouldApproveCreditWithValidApprovedCard() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var cardInfo = DataHelper.getApprovedCardInfo();
        creditPage.fillForm(cardInfo);
        creditPage.checkSuccessNotification();
        assertEquals("APPROVED", DbUtils.getCreditStatus());
    }

    @Test
    void shouldDeclineCreditWithDeclinedCard() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var cardInfo = DataHelper.getDeclinedCardInfo();
        creditPage.fillForm(cardInfo);
        creditPage.checkErrorNotification();
        assertEquals("DECLINED", DbUtils.getCreditStatus());
    }

    @Test
    void shouldShowErrorIfCardNumberIsEmpty() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var cardInfo = DataHelper.getCardWithEmptyNumber();
        creditPage.fillForm(cardInfo);
        creditPage.checkWrongFormatMessageForField("Номер карты");
    }

    @Test
    void shouldShowErrorIfMonthIsEmpty() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var cardInfo = DataHelper.getCardWithEmptyMonth();
        creditPage.fillForm(cardInfo);
        creditPage.checkWrongFormatMessageForField("Месяц");
    }

    @Test
    void shouldShowErrorIfYearIsEmpty() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var cardInfo = DataHelper.getCardWithEmptyYear();
        creditPage.fillForm(cardInfo);
        creditPage.checkWrongFormatMessageForField("Год");
    }

    @Test
    void shouldShowErrorIfOwnerIsEmpty() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var cardInfo = DataHelper.getCardWithEmptyOwner();
        creditPage.fillForm(cardInfo);
        creditPage.checkRequiredFieldMessageForOwner();
    }

    @Test
    void shouldShowErrorIfCVCIsEmpty() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var cardInfo = DataHelper.getCardWithEmptyCVC();
        creditPage.fillForm(cardInfo);
        creditPage.checkWrongFormatMessageForField("CVC/CVV");
    }

    @Test
    void shouldShowErrorIfCardNumberIsInvalid() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var cardInfo = DataHelper.getCardWithInvalidNumber();
        creditPage.fillForm(cardInfo);
        creditPage.checkWrongFormatMessageForField("Номер карты");
    }

    @Test
    void shouldShowErrorIfCVCIsInvalid() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var cardInfo = DataHelper.getCardWithInvalidCVC();
        creditPage.fillForm(cardInfo);
        creditPage.checkWrongFormatMessageForField("CVC/CVV");
    }
}