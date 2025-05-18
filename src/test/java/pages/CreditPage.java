package pages;

import com.codeborne.selenide.SelenideElement;
import data.DataHelper;
import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class CreditPage {

    private final SelenideElement heading = $x("//h3[contains(text(),'Кредит по данным карты')]");
    private final SelenideElement cardNumberField = $x("//span[text()='Номер карты']/..//input");
    private final SelenideElement monthField = $x("//span[text()='Месяц']/..//input");
    private final SelenideElement yearField = $x("//span[text()='Год']/..//input");
    private final SelenideElement ownerField = $x("//span[text()='Владелец']/..//input");
    private final SelenideElement cvcField = $x("//span[text()='CVC/CVV']/..//input");
    private final SelenideElement continueButton = $x("//span[text()='Продолжить']/..");

    private final SelenideElement successNotification = $x("//div[contains(@class,'notification_status_ok')]");
    private final SelenideElement errorNotification = $x("//div[contains(@class,'notification_status_error')]");

    public CreditPage() {
        heading.shouldBe(visible);
    }

    public void fillForm(DataHelper.CardInfo cardInfo) {
        cardNumberField.setValue(cardInfo.getCardNumber());
        monthField.setValue(cardInfo.getMonth());
        yearField.setValue(cardInfo.getYear());
        ownerField.setValue(cardInfo.getHolder());
        cvcField.setValue(cardInfo.getCvc());
        continueButton.click();
    }

    public void checkSuccessNotification() {
        successNotification.shouldBe(visible, Duration.ofSeconds(25));
    }

    public void checkErrorNotification() {
        errorNotification.shouldBe(visible, Duration.ofSeconds(25));
    }

    public void checkWrongFormatMessageForField(String fieldLabel) {
        $x("//span[text()='" + fieldLabel + "']/../span[@class='input__sub' and text()='Неверный формат']")
                .shouldBe(visible);
    }

    public void checkRequiredFieldMessageForField(String fieldLabel) {
        $x("//span[text()='" + fieldLabel + "']/../span[@class='input__sub' and text()='Поле обязательно для заполнения']")
                .shouldBe(visible);
    }

    public void checkNoNotificationsVisible() {
        successNotification.shouldNotBe(visible, Duration.ofSeconds(25));
        errorNotification.shouldNotBe(visible, Duration.ofSeconds(25));
    }
}
