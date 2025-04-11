package test;

import com.codeborne.selenide.Condition;
import data.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import org.openqa.selenium.Keys;

public class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSuccessfullyReplanMeetingWithAutocompleteAndCalendar() {
        var user = DataGenerator.Registration.generateUser("ru");
        var firstMeetingDate = DataGenerator.generateDate(4);
        var secondMeetingDate = DataGenerator.generateDate(5);

        // Город через автозаполнение
        $("[data-test-id=city] input").setValue(user.getCity().substring(0, 3));
        $$(".menu-item__control").find(Condition.exactText(user.getCity())).click();

        // Первая дата через календарь
        $("[data-test-id=date] input").click();
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);

        $("[data-test-id=name] input").setValue(user.getName());
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(Condition.exactText("Запланировать")).click();

        // Проверка первого успешного планирования
        $("[data-test-id=success-notification]")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15));
        $("[data-test-id=success-notification] button").click();

        // Вторая дата
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);

        $$("button").find(Condition.exactText("Запланировать")).click();

        $("[data-test-id=replan-notification]")
                .shouldHave(Condition.text("Необходимо подтверждение"), Duration.ofSeconds(15));
        $$("button").find(Condition.exactText("Перепланировать")).click();

        $("[data-test-id=success-notification]")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(15));
    }
}