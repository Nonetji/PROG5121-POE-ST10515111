package com.mycompany.prog5121.poe.st10515111.part1;

import java.util.Scanner;

public class RegistrationChecker {

    // 1.a.
    // Declare variables prior to method implementations
    private String username;
    private String password;
    private String cellPhoneNumber;

    public RegistrationChecker(String username, String password, String cellPhoneNumber) {
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
    }

    // check if username contains underscore and if length <= 5
    public boolean checkUserName() {
        return this.username != null && this.username.contains("_") && this.username.length() <= 5;
    }

    // check if password complexity rules are met
    public boolean checkPasswordComplexity() {
        if (this.password == null || this.password.length() < 8) {
            return false;
        }

        Scanner userInputCapital = new Scanner(this.password);
        boolean hasCapital = userInputCapital.findInLine("[A-Z]") != null;
        userInputCapital.close();

        Scanner userInputNumber = new Scanner(this.password);
        boolean hasNumber = userInputNumber.findInLine("[0-9]") != null;
        userInputNumber.close();

        Scanner userInputSpecial = new Scanner(this.password);
        boolean hasSpecial = userInputSpecial.findInLine("[^a-zA-Z0-9]") != null;
        userInputSpecial.close();

        return hasCapital && hasNumber && hasSpecial;
    }

    // 1.b. 
    // check if cell number requirements are met
    // Reference/Attribution: Regular expression pattern adapted from standard E.164 international phone number format criteria.
    public boolean checkCellPhoneNumber() {
        if (this.cellPhoneNumber == null || this.cellPhoneNumber.length() > 10) {
            return false;
        }

        Scanner userInputPhone = new Scanner(this.cellPhoneNumber);
        String match = userInputPhone.findInLine("^\\+\\d{1,3}\\d{1,9}$");
        userInputPhone.close();

        return match != null;
    }
}