package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import db.DbUtils;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import pages.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreditTest {

    @BeforeAll
    static void setupAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:8080");
        DbUtils.clearTables();
    }

    @Test
    @DisplayName("Успешная заявка на кредит с валидной картой (APPROVED)")
    void shouldApproveCreditWithValidCard() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var cardInfo = DataHelper.getApprovedCardInfo();
        creditPage.fillForm(cardInfo);
        creditPage.checkSuccessNotification();

        var creditStatus = DbUtils.getCreditStatus();
        assertEquals("APPROVED", creditStatus);
        assertTrue(DbUtils.isOrderLinkedToCredit());
    }

    @Test
    @DisplayName("Отклонение кредита по карте со статусом DECLINED")
    void shouldDeclineCreditWithDeclinedCard() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var cardInfo = DataHelper.getDeclinedCardInfo();
        creditPage.fillForm(cardInfo);
        creditPage.checkErrorNotification();

        var creditStatus = DbUtils.getCreditStatus();
        assertEquals("DECLINED", creditStatus);
        assertTrue(DbUtils.isOrderLinkedToCredit());
    }


    @Test
    @DisplayName("Пустое поле 'Номер карты' — заявка на кредит")
    void shouldShowValidationErrorsForEmptyCardNumberCredit() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var info = DataHelper.getEmptyCardNumberInfo();
        creditPage.fillForm(info);
    
        creditPage.checkValidationMessageForField("Номер карты", "Неверный формат");
        creditPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }
    
    @Test
    @DisplayName("Пустое поле 'Месяц' — заявка на кредит")
    void shouldShowValidationErrorsForEmptyMonthCredit() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var info = DataHelper.getEmptyMonthInfo();
        creditPage.fillForm(info);
    
        creditPage.checkValidationMessageForField("Месяц", "Неверный формат");
        creditPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }
    
    @Test
    @DisplayName("Пустое поле 'Год' — заявка на кредит")
    void shouldShowValidationErrorsForEmptyYearCredit() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var info = DataHelper.getEmptyYearInfo();
        creditPage.fillForm(info);
    
        creditPage.checkValidationMessageForField("Год", "Неверный формат");
        creditPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }
    
    @Test
    @DisplayName("Пустое поле 'Владелец' — заявка на кредит")
    void shouldShowValidationErrorsForEmptyHolderCredit() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var info = DataHelper.getEmptyHolderInfo();
        creditPage.fillForm(info);
    
        creditPage.checkValidationMessageForField("Владелец", "Поле обязательно для заполнения");
        creditPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }
    
    @Test
    @DisplayName("Пустое поле 'CVC/CVV' — заявка на кредит")
    void shouldShowValidationErrorsForEmptyCvcCredit() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var info = DataHelper.getEmptyCvcInfo();
        creditPage.fillForm(info);
    
        creditPage.checkValidationMessageForField("CVC/CVV", "Неверный формат");
        creditPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }
    
    @Test
    @DisplayName("Все поля пустые — заявка на кредит")
    void shouldShowValidationErrorsForEmptyFormCredit() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var info = DataHelper.getEmptyFormInfo();
        creditPage.fillForm(info);
    
        creditPage.checkValidationMessageForField("Номер карты", "Неверный формат");
        creditPage.checkValidationMessageForField("Месяц", "Неверный формат");
        creditPage.checkValidationMessageForField("Год", "Неверный формат");
        creditPage.checkValidationMessageForField("Владелец", "Поле обязательно для заполнения");
        creditPage.checkValidationMessageForField("CVC/CVV", "Неверный формат");
        creditPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }

    @Test
    @DisplayName("Неизвестная карта — заявка на кредит")
    void shouldDeclineUnknownCardCredit() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var info = DataHelper.getUnknownCardBinInfo();
        creditPage.fillForm(info);
        creditPage.checkErrorNotification();

        var status = DbUtils.getCreditStatus();
        assertNotNull(status, "❌ Нет записи в credit_request_entity — возможно, SUT не сохраняет запись при DECLINED");
        assertEquals("DECLINED", status);
        assertTrue(DbUtils.isOrderLinkedToCredit(), "❌ Нет связи с order_entity — возможно, заявка не записалась");
    }

    @Test
    @DisplayName("Месяц = 13 — заявка на кредит")
    void shouldShowInvalidMonthMessage13Credit() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var info = DataHelper.getInvalidMonth13();
        creditPage.fillForm(info);

        creditPage.checkValidationMessageForField("Месяц", "Неверно указан срок действия карты");
        creditPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }
    
    @Test
    @DisplayName("Месяц = 00 — заявка на кредит")
    void shouldShowInvalidMonthMessage00Credit() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var info = DataHelper.getInvalidMonth00();
        creditPage.fillForm(info);

        creditPage.checkValidationMessageForField("Месяц", "Неверно указан срок действия карты");
        creditPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }

    @Test
    @DisplayName("Месяц в прошлом — заявка на кредит")
    void shouldShowInvalidPastMonthCredit() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var info = DataHelper.getInvalidMonthPast();
        creditPage.fillForm(info);

        creditPage.checkValidationMessageForField("Месяц", "Неверно указан срок действия карты");
        creditPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }

    @Test
    @DisplayName("Год = 00 — заявка на кредит")
    void shouldShowInvalidYear00Credit() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var info = DataHelper.getInvalidYearPast();
        creditPage.fillForm(info);

        creditPage.checkValidationMessageForField("Год", "Истёк срок действия карты");
        creditPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }

    @Test
    @DisplayName("Год +7 лет — заявка на кредит")
    void shouldShowTooFarYearCredit() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var info = DataHelper.getInvalidYearTooFar();
        creditPage.fillForm(info);

        creditPage.checkValidationMessageForField("Год", "Неверно указан срок действия карты");
        creditPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }

    @Test
    @DisplayName("Владелец на кириллице — заявка на кредит")
    void shouldShowInvalidHolderCyrillicCredit() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var info = DataHelper.getInvalidHolderCyrillic();
        creditPage.fillForm(info);

        creditPage.checkValidationMessageForField("Владелец", "Неверный формат");
        creditPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }

    @Test
    @DisplayName("Владелец со спецсимволами — заявка на кредит")
    void shouldShowInvalidHolderSymbolsCredit() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var info = DataHelper.getInvalidHolderSpecialSymbols();
        creditPage.fillForm(info);

        creditPage.checkValidationMessageForField("Владелец", "Неверный формат");
        creditPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }

    @Test
    @DisplayName("Владелец — только цифры — заявка на кредит")
    void shouldShowInvalidHolderDigitsCredit() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var info = DataHelper.getInvalidHolderNumbers();
        creditPage.fillForm(info);

        creditPage.checkValidationMessageForField("Владелец", "Неверный формат");
        creditPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }

    @Test
    @DisplayName("CVC = 1 — заявка на кредит")
    void shouldShowInvalidCvcShortCredit() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var info = DataHelper.getInvalidCVCShort();
        creditPage.fillForm(info);

        creditPage.checkValidationMessageForField("CVC/CVV", "Неверный формат");
        creditPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }

    @Test
    @DisplayName("CVC = ab1 — заявка на кредит")
    void shouldShowInvalidCvcAlphaCredit() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var info = DataHelper.getInvalidCVCAlpha();
        creditPage.fillForm(info);

        creditPage.checkValidationMessageForField("CVC/CVV", "Неверный формат");
        creditPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }

    @Test
    @DisplayName("CVC = 0 — заявка на кредит")
    void shouldShowInvalidCvcZeroCredit() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var info = DataHelper.getInvalidCVCZero();
        creditPage.fillForm(info);

        creditPage.checkValidationMessageForField("CVC/CVV", "Неверный формат");
        creditPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }

    @Test
    @DisplayName("Имя владельца > 50 символов — заявка на кредит")
    void shouldShowValidationMessageForLongHolderCredit() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var info = DataHelper.getLongHolderName(); // > 50 символов
        creditPage.fillForm(info);

        creditPage.checkValidationMessageForField("Владелец", "Неверный формат");
        creditPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }

    @Test
    @DisplayName("CVC = 000 — невалидный код — заявка на кредит")
    void shouldShowValidationMessageForZeroCvcCredit() {
        var mainPage = new MainPage();
        var creditPage = mainPage.goToCreditPage();
        var info = DataHelper.getEdgeCVC(); // CVC = "000"
        creditPage.fillForm(info);

        creditPage.checkValidationMessageForField("CVC/CVV", "Неверный формат");
        creditPage.checkNoNotificationsVisible();
        assertEquals(0, DbUtils.getOrderCount());
    }
    
}