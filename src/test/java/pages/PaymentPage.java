package pages;

import com.codeborne.selenide.SelenideElement;
import data.DataHelper;
import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
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

    public PaymentPage() {
        heading.shouldBe(visible);
    }

    // Заполнение формы одним объектом CardInfo
    public void fillForm(DataHelper.CardInfo info) {
        cardNumberField.setValue(info.getCardNumber());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        ownerField.setValue(info.getHolder());
        cvcField.setValue(info.getCvc());
        continueButton.click();
    }

    // Проверка успешной оплаты
    public void checkSuccessNotification() {
        successNotification.shouldBe(visible, Duration.ofSeconds(15));
    }

    // Проверка ошибки оплаты
    public void checkErrorNotification() {
        errorNotification.shouldBe(visible, Duration.ofSeconds(15));
    }

    // Проверка "Неверный формат" под нужным полем
    public void checkWrongFormatMessageForField(String fieldName) {
        $x("//span[text()='" + fieldName + "']/../span[@class='input__sub' and text()='Неверный формат']")
                .shouldBe(visible);
    }

    // Проверка "Поле обязательно" для поля "Владелец"
    public void checkRequiredFieldMessageForOwner() {
        $x("//span[text()='Владелец']/../span[@class='input__sub' and text()='Поле обязательно для заполнения']")
                .shouldBe(visible);
    }
}