package util;

public class ValidationUtil {

    private static final String DATE_TIME_REGEX = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";
    /**
     * Проверяет, что строка с датой и временем не null, не пустая и соответствует формату.
     *
     * @param dateTimeStr строка с датой и временем
     * @param message     сообщение об ошибке
     * @throws IllegalArgumentException если строка некорректна
     */
    public static void checkValidDateTimeString(String dateTimeStr, String message) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            throw new IllegalArgumentException(message); // Проверка на null и пустую строку
        }

        if (!dateTimeStr.matches(DATE_TIME_REGEX)) {
            throw new IllegalArgumentException(message); // Проверка на соответствие формату
        }
    }

    private ValidationUtil() {
    }
    public static void checkValidPhoneNumber(String phoneNumber, String message) {
        if (phoneNumber == null || !phoneNumber.matches("^\\+?[0-9]{10,15}$")) {
            throw new IllegalArgumentException(message);
        }
    }
    public static void checkNotNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkNotEmpty(String str, String message) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }


    public static void checkNotEmptyEmail(String email, String message) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void checkPositive(Double number, String message) {
        if (number == null || number < 0) {
            throw new IllegalArgumentException(message);
        }
    }
}
