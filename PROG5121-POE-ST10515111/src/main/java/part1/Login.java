package com.mycompany.prog5121.poe.st10515111.part1;

import java.util.Scanner;

public class Login {

    // 1.a.
    // Declaring variables
    private String registeredUsername;
    private String registeredPassword;
    private String registeredCellPhone;
    private String firstName;
    private String lastName;

    public Login() {
        this.registeredUsername = "";
        this.registeredPassword = "";
        this.registeredCellPhone = "";
        this.firstName = "";
        this.lastName = "";
    }

    public Login(String firstName, String lastName) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Check if username format is met
    public boolean checkUserName(String username) {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    // Check if password requirements are met 
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) return false;

        Scanner userInputCapital = new Scanner(password);
        boolean hasCapital = userInputCapital.findInLine("[A-Z]") != null;
        userInputCapital.close();

        Scanner userInputNumber = new Scanner(password);
        boolean hasNumber = userInputNumber.findInLine("[0-9]") != null;
        userInputNumber.close();

        Scanner userInputSpecial = new Scanner(password);
        boolean hasSpecial = userInputSpecial.findInLine("[^a-zA-Z0-9]") != null;
        userInputSpecial.close();

        return hasCapital && hasNumber && hasSpecial;
    }

    // 1.b. 
    // Check if cell phone format is met
    // Reference/Attribution: Regular expression pattern adapted from standard E.164 international phone number format criteria.
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null || cellPhoneNumber.length() > 10) return false;

        Scanner userInputPhone = new Scanner(cellPhoneNumber);
        String match = userInputPhone.findInLine("^\\+\\d{1,3}\\d{1,9}$");
        userInputPhone.close();

        return match != null;
    }
    
    // 2.a. 
    // Verify login credentials
    public boolean loginUser(String usernameInput, String passwordInput) {
        return this.registeredUsername.equals(usernameInput) && this.registeredPassword.equals(passwordInput);
    }

    // Display welcome/error messages based on user inputs
    public String returnLoginStatus(String usernameInput, String passwordInput) {
        if (loginUser(usernameInput, passwordInput)) {
            return "Welcome " + this.firstName + " " + this.lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}