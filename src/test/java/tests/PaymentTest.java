package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import db.DbUtils;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    @BeforeEach
    void setUp() {
        SelenideLogger.addListener("allure", new AllureSelenide());
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
}