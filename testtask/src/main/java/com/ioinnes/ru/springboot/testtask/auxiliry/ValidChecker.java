package com.ioinnes.ru.springboot.testtask.auxiliry;

public class ValidChecker {
    public static boolean isUsernameValid(String username) {
        return username != null && !username.isEmpty();
    }
    public static boolean isEmailValid(String email) {
        return email != null && email.matches(EmailChecker.emailRegex);
    }

    public static boolean isStatusValid(String status) {
        return "online".equals(status) || "offline".equals(status) || status == null;
    }
}
