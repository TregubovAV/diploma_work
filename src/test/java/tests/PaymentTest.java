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

    @Test
    @DisplayName("Неизвестная карта — оплата по карте")
    void shouldDeclineUnknownCardPayment() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var info = DataHelper.getUnknownCardBinInfo();
        paymentPage.fillForm(info);
        paymentPage.checkErrorNotification();

        var status = DbUtils.getPaymentStatus();
        assertNotNull(status, "❌ Нет записи в payment_entity — возможно, SUT не сохраняет запись при DECLINED");
        assertEquals("DECLINED", status);
        assertTrue(DbUtils.isOrderLinkedToPayment(), "❌ Нет связи с order_entity — возможно, заявка не записалась");
    }

    @Test
    @DisplayName("Месяц = 13 — оплата по карте")
    void shouldShowInvalidMonthMessage13Payment() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var info = DataHelper.getInvalidMonth13();
        paymentPage.fillForm(info);

        paymentPage.checkValidationMessageForField("Месяц", "Неверно указан срок действия карты");
        paymentPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }
    
    @Test
    @DisplayName("Месяц = 00 — оплата по карте")
    void shouldShowInvalidMonthMessage00Payment() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var info = DataHelper.getInvalidMonth00();
        paymentPage.fillForm(info);

        paymentPage.checkValidationMessageForField("Месяц", "Неверно указан срок действия карты");
        paymentPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }

    @Test
    @DisplayName("Месяц в прошлом — оплата по карте")
    void shouldShowInvalidPastMonthPayment() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var info = DataHelper.getInvalidMonthPast();
        paymentPage.fillForm(info);

        paymentPage.checkValidationMessageForField("Месяц", "Неверно указан срок действия карты");
        paymentPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }

    @Test
    @DisplayName("Год = 00 — оплата по карте")
    void shouldShowInvalidYear00Payment() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var info = DataHelper.getInvalidYearPast();
        paymentPage.fillForm(info);

        paymentPage.checkValidationMessageForField("Год", "Истёк срок действия карты");
        paymentPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }

    @Test
    @DisplayName("Год +7 лет — оплата по карте")
    void shouldShowTooFarYearPayment() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var info = DataHelper.getInvalidYearTooFar();
        paymentPage.fillForm(info);

        paymentPage.checkValidationMessageForField("Год", "Неверно указан срок действия карты");
        paymentPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }

    
    @Test
    @DisplayName("Владелец на кириллице — оплата по карте")
    void shouldShowInvalidHolderCyrillicPayment() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var info = DataHelper.getInvalidHolderCyrillic();
        paymentPage.fillForm(info);
    
        paymentPage.checkValidationMessageForField("Владелец", "Неверный формат");
        paymentPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }
    
    @Test
    @DisplayName("Владелец со спецсимволами — оплата по карте")
    void shouldShowInvalidHolderSymbolsPayment() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var info = DataHelper.getInvalidHolderSpecialSymbols();
        paymentPage.fillForm(info);
    
        paymentPage.checkValidationMessageForField("Владелец", "Неверный формат");
        paymentPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }
    
    @Test
    @DisplayName("Владелец — только цифры — оплата по карте")
    void shouldShowInvalidHolderDigitsPayment() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var info = DataHelper.getInvalidHolderNumbers();
        paymentPage.fillForm(info);
    
        paymentPage.checkValidationMessageForField("Владелец", "Неверный формат");
        paymentPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }

    @Test
    @DisplayName("CVC = 1 — оплата по карте")
    void shouldShowInvalidCvcShortPayment() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var info = DataHelper.getInvalidCVCShort();
        paymentPage.fillForm(info);

        paymentPage.checkValidationMessageForField("CVC/CVV", "Неверный формат");
        paymentPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }

    @Test
    @DisplayName("CVC = ab1 — оплата по карте")
    void shouldShowInvalidCvcAlphaPayment() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var info = DataHelper.getInvalidCVCAlpha();
        paymentPage.fillForm(info);

        paymentPage.checkValidationMessageForField("CVC/CVV", "Неверный формат");
        paymentPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }

    @Test
    @DisplayName("CVC = 0 — оплата по карте")
    void shouldShowInvalidCvcZeroPayment() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var info = DataHelper.getInvalidCVCZero();
        paymentPage.fillForm(info);

        paymentPage.checkValidationMessageForField("CVC/CVV", "Неверный формат");
        paymentPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }

    @Test
    @DisplayName("Имя владельца > 50 символов — оплата по карте")
    void shouldShowValidationMessageForLongHolderPayment() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var info = DataHelper.getLongHolderName(); // > 50 символов
        paymentPage.fillForm(info);

        paymentPage.checkValidationMessageForField("Владелец", "Неверный формат");
        paymentPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }

    @Test
    @DisplayName("CVC = 000 — невалидный код — оплата по карте")
    void shouldShowValidationMessageForZeroCvcPayment() {
        var mainPage = new MainPage();
        var paymentPage = mainPage.goToPaymentPage();
        var info = DataHelper.getEdgeCVC(); // CVC = "000"
        paymentPage.fillForm(info);

        paymentPage.checkValidationMessageForField("CVC/CVV", "Неверный формат");
        paymentPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }

}