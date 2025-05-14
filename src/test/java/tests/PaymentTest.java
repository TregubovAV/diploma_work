package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import db.DbUtils;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import pages.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

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
    void shouldApprovePaymentWithValidApprovedCard() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var cardInfo = DataHelper.getApprovedCardInfo();
        paymentPage.fillForm(cardInfo);
        paymentPage.checkSuccessNotification();
        assertEquals("APPROVED", DbUtils.getPaymentStatus());
    }

    @Test
    void shouldDeclinePaymentWithDeclinedCard() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var cardInfo = DataHelper.getDeclinedCardInfo();
        paymentPage.fillForm(cardInfo);
        paymentPage.checkErrorNotification();
        assertEquals("DECLINED", DbUtils.getPaymentStatus());
    }

    @Test
    void shouldShowErrorIfCardNumberIsEmpty() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var cardInfo = DataHelper.getCardWithEmptyNumber();
        paymentPage.fillForm(cardInfo);
        paymentPage.checkWrongFormatMessageForField("Номер карты");
    }

    @Test
    void shouldShowErrorIfMonthIsEmpty() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var cardInfo = DataHelper.getCardWithEmptyMonth();
        paymentPage.fillForm(cardInfo);
        paymentPage.checkWrongFormatMessageForField("Месяц");
    }

    @Test
    void shouldShowErrorIfYearIsEmpty() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var cardInfo = DataHelper.getCardWithEmptyYear();
        paymentPage.fillForm(cardInfo);
        paymentPage.checkWrongFormatMessageForField("Год");
    }

    @Test
    void shouldShowErrorIfOwnerIsEmpty() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var cardInfo = DataHelper.getCardWithEmptyOwner();
        paymentPage.fillForm(cardInfo);
        paymentPage.checkRequiredFieldMessageForOwner();
    }

    @Test
    void shouldShowErrorIfCVCIsEmpty() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var cardInfo = DataHelper.getCardWithEmptyCVC();
        paymentPage.fillForm(cardInfo);
        paymentPage.checkWrongFormatMessageForField("CVC/CVV");
    }

    @Test
    void shouldShowErrorIfCardNumberIsInvalid() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var cardInfo = DataHelper.getCardWithInvalidNumber();
        paymentPage.fillForm(cardInfo);
        paymentPage.checkWrongFormatMessageForField("Номер карты");
    }

    @Test
    void shouldShowErrorIfCVCIsInvalid() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var cardInfo = DataHelper.getCardWithInvalidCVC();
        paymentPage.fillForm(cardInfo);
        paymentPage.checkWrongFormatMessageForField("CVC/CVV");
    }
}