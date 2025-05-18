package data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataHelper {
    private DataHelper() {}

    private static final Faker faker = new Faker(Locale.ENGLISH);
    private static final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");
    private static final DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yy");

    @Value
    public static class CardInfo {
        String cardNumber;
        String month;
        String year;
        String holder;
        String cvc;
    }

    // === Атомарные генераторы ===

    public static String getApprovedCardNumber() {
        return "4444 4444 4444 4441";
    }

    public static String getDeclinedCardNumber() {
        return "4444 4444 4444 4442";
    }

    public static String generateHolder() {
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String generateCVC() {
        return String.format("%03d", new Random().nextInt(1000));
    }

    public static String formatMonth(LocalDate date) {
        return date.format(monthFormatter);
    }

    public static String formatYear(LocalDate date) {
        return date.format(yearFormatter);
    }

    public static String getCurrentMonth() {
        return formatMonth(LocalDate.now());
    }

    public static String getCurrentYear() {
        return formatYear(LocalDate.now());
    }

    // === Валидные карты ===

    public static CardInfo getApprovedCardInfo() {
        return new CardInfo(getApprovedCardNumber(), getCurrentMonth(), getCurrentYear(), generateHolder(), generateCVC());
    }

    public static CardInfo getDeclinedCardInfo() {
        return new CardInfo(getDeclinedCardNumber(), getCurrentMonth(), getCurrentYear(), generateHolder(), generateCVC());
    }

    public static CardInfo getUnknownCardInfo() {
        return new CardInfo("4000 0000 0000 0002", getCurrentMonth(), getCurrentYear(), generateHolder(), generateCVC());
    }

    public static CardInfo getUnknownCardBinInfo() {
        return new CardInfo("1234 5678 9012 3456", getCurrentMonth(), getCurrentYear(), generateHolder(), generateCVC());
    }

    // === Пустые поля ===

    public static CardInfo getEmptyCardNumberInfo() {
        return new CardInfo("", getCurrentMonth(), getCurrentYear(), generateHolder(), generateCVC());
    }

    public static CardInfo getEmptyMonthInfo() {
        return new CardInfo(getApprovedCardNumber(), "", getCurrentYear(), generateHolder(), generateCVC());
    }

    public static CardInfo getEmptyYearInfo() {
        return new CardInfo(getApprovedCardNumber(), getCurrentMonth(), "", generateHolder(), generateCVC());
    }

    public static CardInfo getEmptyHolderInfo() {
        return new CardInfo(getApprovedCardNumber(), getCurrentMonth(), getCurrentYear(), "", generateCVC());
    }

    public static CardInfo getEmptyCvcInfo() {
        return new CardInfo(getApprovedCardNumber(), getCurrentMonth(), getCurrentYear(), generateHolder(), "");
    }

    public static CardInfo getEmptyFormInfo() {
        return new CardInfo("", "", "", "", "");
    }

    // === Невалидные данные ===

    public static CardInfo getInvalidMonth13() {
        return new CardInfo(getApprovedCardNumber(), "13", getCurrentYear(), generateHolder(), generateCVC());
    }

    public static CardInfo getInvalidMonth00() {
        return new CardInfo(getApprovedCardNumber(), "00", getCurrentYear(), generateHolder(), generateCVC());
    }

    public static CardInfo getInvalidMonthPast() {
        String pastMonth = formatMonth(LocalDate.now().minusMonths(1));
        return new CardInfo(getApprovedCardNumber(), pastMonth, getCurrentYear(), generateHolder(), generateCVC());
    }

    public static CardInfo getInvalidYearTooFar() {
        String farYear = formatYear(LocalDate.now().plusYears(7));
        return new CardInfo(getApprovedCardNumber(), getCurrentMonth(), farYear, generateHolder(), generateCVC());
    }

    public static CardInfo getInvalidYearPast() {
        return new CardInfo(getApprovedCardNumber(), getCurrentMonth(), "00", generateHolder(), generateCVC());
    }

    public static CardInfo getInvalidHolderCyrillic() {
        return new CardInfo(getApprovedCardNumber(), getCurrentMonth(), getCurrentYear(), "Иван Иванов", generateCVC());
    }

    public static CardInfo getInvalidHolderSpecialSymbols() {
        return new CardInfo(getApprovedCardNumber(), getCurrentMonth(), getCurrentYear(), "@#$%", generateCVC());
    }

    public static CardInfo getInvalidHolderNumbers() {
        return new CardInfo(getApprovedCardNumber(), getCurrentMonth(), getCurrentYear(), "123456", generateCVC());
    }

    public static CardInfo getLongHolderName() {
        return new CardInfo(getApprovedCardNumber(), getCurrentMonth(), getCurrentYear(), "Jonathan Livingston Seagull the Third from California", generateCVC());
    }

    public static CardInfo getInvalidCVCShort() {
        return new CardInfo(getApprovedCardNumber(), getCurrentMonth(), getCurrentYear(), generateHolder(), "1");
    }

    public static CardInfo getInvalidCVCAlpha() {
        return new CardInfo(getApprovedCardNumber(), getCurrentMonth(), getCurrentYear(), generateHolder(), "ab1");
    }

    public static CardInfo getInvalidCVCZero() {
        return new CardInfo(getApprovedCardNumber(), getCurrentMonth(), getCurrentYear(), generateHolder(), "0");
    }

    public static CardInfo getEdgeCVC() {
        return new CardInfo(getApprovedCardNumber(), getCurrentMonth(), getCurrentYear(), generateHolder(), "000");
    }
}