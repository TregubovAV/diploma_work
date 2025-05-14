package data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.Year;
import java.util.Locale;
import java.util.Random;

public class DataHelper {
    private DataHelper() {}

    private static final Faker faker = new Faker(new Locale("en"));
    private static final Random random = new Random();

    @Value
    public static class CardInfo {
        String cardNumber;
        String month;
        String year;
        String holder;
        String cvc;
    }

    // === Позитивные карты ===

    public static CardInfo getApprovedCardInfo() {
        return new CardInfo("4444 4444 4444 4441", getValidMonth(), getValidYear(), getValidHolder(), getValidCVC());
    }

    public static CardInfo getDeclinedCardInfo() {
        return new CardInfo("4444 4444 4444 4442", getValidMonth(), getValidYear(), getValidHolder(), getValidCVC());
    }

    // === Отдельные сценарии ===

    public static CardInfo getCardWithEmptyNumber() {
        return new CardInfo("", getValidMonth(), getValidYear(), getValidHolder(), getValidCVC());
    }

    public static CardInfo getCardWithEmptyMonth() {
        return new CardInfo("4444 4444 4444 4441", "", getValidYear(), getValidHolder(), getValidCVC());
    }

    public static CardInfo getCardWithEmptyYear() {
        return new CardInfo("4444 4444 4444 4441", getValidMonth(), "", getValidHolder(), getValidCVC());
    }

    public static CardInfo getCardWithEmptyOwner() {
        return new CardInfo("4444 4444 4444 4441", getValidMonth(), getValidYear(), "", getValidCVC());
    }

    public static CardInfo getCardWithEmptyCVC() {
        return new CardInfo("4444 4444 4444 4441", getValidMonth(), getValidYear(), getValidHolder(), "");
    }

    public static CardInfo getCardWithInvalidNumber() {
        return new CardInfo("1234 5678 9012", getValidMonth(), getValidYear(), getValidHolder(), getValidCVC());
    }

    public static CardInfo getCardWithInvalidCVC() {
        return new CardInfo("4444 4444 4444 4441", getValidMonth(), getValidYear(), getValidHolder(), "12");
    }

    public static CardInfo getEmptyForm() {
        return new CardInfo("", "", "", "", "");
    }

    // === Вспомогательные генераторы ===

    public static String getValidMonth() {
        int month = random.nextInt(12) + 1;
        return String.format("%02d", month);
    }

    public static String getValidYear() {
        int year = Year.now().getValue() % 100 + 1;
        return String.format("%02d", year);
    }

    public static String getValidHolder() {
        return faker.name().fullName();
    }

    public static String getValidCVC() {
        return String.format("%03d", random.nextInt(1000));
    }

    public static String getInvalidMonth() {
        return "13";
    }

    public static String getInvalidYear() {
        return "99";
    }

    public static String getInvalidHolder() {
        return "Иван Иванов"; // кириллица
    }

    public static String getInvalidCVC() {
        return "1a2";
    }
}