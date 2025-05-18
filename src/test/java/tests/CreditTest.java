package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import db.DbUtils;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreditTest {

    @BeforeEach
    void setup() {
        open("http://localhost:8080");
        SelenideLogger.addListener("allure", new AllureSelenide());
        DbUtils.clearTables();
    }

    @AfterAll
    static void tearDown() {
        SelenideLogger.removeListener("allure");
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
}