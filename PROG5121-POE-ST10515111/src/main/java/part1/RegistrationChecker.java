package com.mycompany.prog5121.poe.st10515111.part1;

public class RegistrationChecker {

    // Method checks if username contains underscore and if length <= 5
    public boolean checkUserName(String username) {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    // Method checks if password complexity rules are met
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapital = password.matches(".*[A-Z].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[^a-zA-Z0-9].*");

        return hasCapital && hasNumber && hasSpecial;
    }

    // Method checks if cell number requirements are met
    public boolean checkCellPhoneNumber(String cellNumber) {
        if (cellNumber == null) {
            return false;
        }

        return cellNumber.matches("^\\+27\\d{9}$");
    }
}