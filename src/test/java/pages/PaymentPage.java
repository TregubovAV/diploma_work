package pages;

import com.codeborne.selenide.SelenideElement;
import data.DataHelper;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$x;

public class PaymentPage {

    private final SelenideElement heading = $x("//h3[contains(text(),'Оплата по карте')]");
    private final SelenideElement cardNumberField = $x("//span[text()='Номер карты']/..//input");
    private final SelenideElement monthField = $x("//span[text()='Месяц']/..//input");
    private final SelenideElement yearField = $x("//span[text()='Год']/..//input");
    private final SelenideElement ownerField = $x("//span[text()='Владелец']/..//input");
    private final SelenideElement cvcField = $x("//span[text()='CVC/CVV']/..//input");
    private final SelenideElement continueButton = $x("//span[text()='Продолжить']/..");

    private final SelenideElement successNotification = $x("//div[contains(@class,'notification_status_ok')]");
    private final SelenideElement errorNotification = $x("//div[contains(@class,'notification_status_error')]");
    private final SelenideElement successText = successNotification.$x(".//div[@class='notification__content']");
    private final SelenideElement errorText = errorNotification.$x(".//div[@class='notification__content']");

    public PaymentPage() {
        heading.shouldBe(visible);
    }

    public void fillForm(DataHelper.CardInfo info) {
        cardNumberField.setValue(info.getCardNumber());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        ownerField.setValue(info.getHolder());
        cvcField.setValue(info.getCvc());
        continueButton.click();
    }

    public void checkSuccessNotification() {
        successNotification.shouldBe(visible, Duration.ofSeconds(25));
        successText.shouldHave(text("Операция одобрена Банком."), Duration.ofSeconds(25));
    }

    public void checkErrorNotification() {
        errorNotification.shouldBe(visible, Duration.ofSeconds(25));
        errorText.shouldHave(text("Ошибка! Банк отказал в проведении операции."), Duration.ofSeconds(25));
    }

    public void checkValidationMessageForField(String fieldLabel, String expectedMessage) {
        $x("//span[contains(@class, 'input__inner')][.//span[@class='input__top' and text()='" + fieldLabel + "']]//span[contains(@class, 'input__sub') and text()='" + expectedMessage + "']")
            .shouldBe(visible, Duration.ofSeconds(3));
    }

    public void checkNoNotificationsVisible() {
        successNotification.shouldNotBe(visible, Duration.ofSeconds(25));
        errorNotification.shouldNotBe(visible, Duration.ofSeconds(25));
    }
}