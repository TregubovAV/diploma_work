
package data;

import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataHelper {
    private DataHelper() {}
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");

    @Value
    public static class CardInfo {
        String cardNumber;
        String month;
        String year;
        String holder;
        String cvc;
    }

    // === Валидные карты ===

    public static CardInfo getApprovedCardInfo() {
        return new CardInfo("4444 4444 4444 4441", getCurrentMonth(), getCurrentYear(), "John Smith", "123");
    }

    public static CardInfo getDeclinedCardInfo() {
        return new CardInfo("4444 4444 4444 4442", getCurrentMonth(), getCurrentYear(), "John Smith", "123");
    }

    public static CardInfo getUnknownCardInfo() {
        return new CardInfo("4000 0000 0000 0002", getCurrentMonth(), getCurrentYear(), "John Smith", "123");
    }

    public static CardInfo getUnknownCardBinInfo() {
        return new CardInfo("1234 5678 9012 3456", getCurrentMonth(), getCurrentYear(), "John Smith", "123");
    }

    // === Пустые поля ===

    public static CardInfo getEmptyCardNumberInfo() {
        return new CardInfo("", getCurrentMonth(), getCurrentYear(), "John Smith", "123");
    }

    public static CardInfo getEmptyMonthInfo() {
        return new CardInfo("4444 4444 4444 4441", "", getCurrentYear(), "John Smith", "123");
    }

    public static CardInfo getEmptyYearInfo() {
        return new CardInfo("4444 4444 4444 4441", getCurrentMonth(), "", "John Smith", "123");
    }

    public static CardInfo getEmptyHolderInfo() {
        return new CardInfo("4444 4444 4444 4441", getCurrentMonth(), getCurrentYear(), "", "123");
    }

    public static CardInfo getEmptyCvcInfo() {
        return new CardInfo("4444 4444 4444 4441", getCurrentMonth(), getCurrentYear(), "John Smith", "");
    }

    public static CardInfo getEmptyFormInfo() {
        return new CardInfo("", "", "", "", "");
    }

    // === Невалидные данные ===

    public static CardInfo getInvalidMonth13() {
        return new CardInfo("4444 4444 4444 4441", "13", getCurrentYear(), "John Smith", "123");
    }

    public static CardInfo getInvalidMonth00() {
        return new CardInfo("4444 4444 4444 4441", "00", getCurrentYear(), "John Smith", "123");
    }

    public static CardInfo getInvalidMonthPast() {
        int pastMonth = LocalDate.now().minusMonths(1).getMonthValue();
        return new CardInfo("4444 4444 4444 4441", String.format("%02d", pastMonth), getCurrentYear(), "John Smith", "123");
    }

    public static CardInfo getInvalidYearTooFar() {
        return new CardInfo("4444 4444 4444 4441", getCurrentMonth(), String.valueOf((LocalDate.now().getYear() + 7) % 100), "John Smith", "123");
    }

    public static CardInfo getInvalidYearPast() {
        return new CardInfo("4444 4444 4444 4441", getCurrentMonth(), "00", "John Smith", "123");
    }

    public static CardInfo getInvalidHolderCyrillic() {
        return new CardInfo("4444 4444 4444 4441", getCurrentMonth(), getCurrentYear(), "Иван Иванов", "123");
    }

    public static CardInfo getInvalidHolderSpecialSymbols() {
        return new CardInfo("4444 4444 4444 4441", getCurrentMonth(), getCurrentYear(), "@#$%", "123");
    }

    public static CardInfo getInvalidHolderNumbers() {
        return new CardInfo("4444 4444 4444 4441", getCurrentMonth(), getCurrentYear(), "123456", "123");
    }

    public static CardInfo getLongHolderName() {
        return new CardInfo("4444 4444 4444 4441", getCurrentMonth(), getCurrentYear(), "Jonathan Livingston Seagull the Third from California", "123");
    }

    public static CardInfo getInvalidCVCShort() {
        return new CardInfo("4444 4444 4444 4441", getCurrentMonth(), getCurrentYear(), "John Smith", "1");
    }

    public static CardInfo getInvalidCVCAlpha() {
        return new CardInfo("4444 4444 4444 4441", getCurrentMonth(), getCurrentYear(), "John Smith", "ab1");
    }

    public static CardInfo getInvalidCVCZero() {
        return new CardInfo("4444 4444 4444 4441", getCurrentMonth(), getCurrentYear(), "John Smith", "0");
    }

    public static CardInfo getEdgeCVC() {
        return new CardInfo("4444 4444 4444 4441", getCurrentMonth(), getCurrentYear(), "John Smith", "000");
    }

    // === Хелперы ===

    public static String getCurrentMonth() {
        return LocalDate.now().format(formatter);
    }

    public static String getCurrentYear() {
        return String.valueOf(LocalDate.now().getYear() % 100);
    }
}
