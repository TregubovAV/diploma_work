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
    static void setupAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
        DbUtils.clearTables();
    }

    @AfterEach
    void tearDown() {
        DbUtils.clearTables();
    }

    @Test
    void shouldApprovePaymentWithValidCard() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var info = DataHelper.getApprovedCardInfo(); 
        paymentPage.fillForm(info);
        paymentPage.checkSuccessNotification();
        assertEquals("APPROVED", DbUtils.getPaymentStatus());
        assertTrue(DbUtils.isOrderLinkedToPayment()); 
    }

    @Test
    void shouldDeclinePaymentWithDeclinedCard() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var info = DataHelper.getDeclinedCardInfo(); 
        paymentPage.fillForm(info);
        paymentPage.checkErrorNotification();
        assertEquals("DECLINED", DbUtils.getPaymentStatus());
        assertTrue(DbUtils.isOrderLinkedToPayment()); 
    }



    @Test
    @DisplayName("Пустое поле 'Номер карты' — оплата по карте")
    void shouldShowValidationErrorsForEmptyCardNumberPayment() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var info = DataHelper.getEmptyCardNumberInfo();
        paymentPage.fillForm(info);
    
        paymentPage.checkValidationMessageForField("Номер карты", "Неверный формат");
        paymentPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }
    
    @Test
    @DisplayName("Пустое поле 'Месяц' — оплата по карте")
    void shouldShowValidationErrorsForEmptyMonthPayment() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var info = DataHelper.getEmptyMonthInfo();
        paymentPage.fillForm(info);
    
        paymentPage.checkValidationMessageForField("Месяц", "Неверный формат");
        paymentPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }
    
    @Test
    @DisplayName("Пустое поле 'Год' — оплата по карте")
    void shouldShowValidationErrorsForEmptyYearPayment() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var info = DataHelper.getEmptyYearInfo();
        paymentPage.fillForm(info);
    
        paymentPage.checkValidationMessageForField("Год", "Неверный формат");
        paymentPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }
    
    @Test
    @DisplayName("Пустое поле 'Владелец' — оплата по карте")
    void shouldShowValidationErrorsForEmptyHolderPayment() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var info = DataHelper.getEmptyHolderInfo();
        paymentPage.fillForm(info);
    
        paymentPage.checkValidationMessageForField("Владелец", "Поле обязательно для заполнения");
        paymentPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }
    
    @Test
    @DisplayName("Пустое поле 'CVC/CVV' — оплата по карте")
    void shouldShowValidationErrorsForEmptyCvcPayment() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var info = DataHelper.getEmptyCvcInfo();
        paymentPage.fillForm(info);
    
        paymentPage.checkValidationMessageForField("CVC/CVV", "Неверный формат");
        paymentPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }
    
    @Test
    @DisplayName("Все поля пустые — оплата по карте")
    void shouldShowValidationErrorsForEmptyFormPayment() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var info = DataHelper.getEmptyFormInfo();
        paymentPage.fillForm(info);
    
        paymentPage.checkValidationMessageForField("Номер карты", "Неверный формат");
        paymentPage.checkValidationMessageForField("Месяц", "Неверный формат");
        paymentPage.checkValidationMessageForField("Год", "Неверный формат");
        paymentPage.checkValidationMessageForField("Владелец", "Поле обязательно для заполнения");
        paymentPage.checkValidationMessageForField("CVC/CVV", "Неверный формат");
        paymentPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }
}