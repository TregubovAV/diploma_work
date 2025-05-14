package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class MainPage {

    private final SelenideElement paymentButton = $x("//span[text()='Купить']/..");
    private final SelenideElement creditButton = $x("//span[text()='Купить в кредит']/..");

    public PaymentPage goToPaymentPage() {
        paymentButton.click();
        return new PaymentPage();
    }

    public CreditPage goToCreditPage() {
        creditButton.click();
        return new CreditPage();
    }
}