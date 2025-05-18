package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import db.DbUtils;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import pages.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
}