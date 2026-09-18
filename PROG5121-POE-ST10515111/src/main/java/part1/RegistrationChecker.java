package com.mycompany.prog5121.poe.st10515111.part1;

public class RegistrationChecker {

    // 1.a.
    // Declare variables prior to method implementations
    String username;
    String password;
    String cellNumber;

    // method checks if username contains underscore and if length <= 5
    public boolean checkUserName() {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    // method checks if password complexity rules are met
    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapital = password.matches(".*[A-Z].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[^a-zA-Z0-9].*");

        return hasCapital && hasNumber && hasSpecial;
    }

    // 1.b.
    // method checks if cell number requirements are met
    // Reference/Attribution: Regular expression pattern adapted from standard E.164 international phone number format criteria.
    public boolean checkCellPhoneNumber() {
        if (cellNumber == null) {
            return false;
        }

        return cellNumber.matches("^\\+27\\d{9}$");
    }
}