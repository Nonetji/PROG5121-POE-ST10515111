package com.mycompany.prog5121.poe.st10515111.part1;

public class Login {
    
    // Declaring variables
    String username;
    String password;
    String cellphoneNumber;
    String firstName;
    String lastName;
    String usernameInput;
    String passwordInput;

    // Registration checker
    RegistrationChecker checker = new RegistrationChecker();

    // method verifies login credentials
    public boolean loginUser(String usernameInput, String passwordInput) {
        return username != null
                && password != null
                && username.equals(usernameInput)
                && password.equals(passwordInput);
    }

    // method displays welcome/error messages based on user inputs
    public String returnLoginStatus(String usernameInput, String passwordInput) {

        if (loginUser(usernameInput, passwordInput)) {
            return "Welcome back " + username;
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    // Registration method
    public String registerUser(String username, String password, String cellNumber,
            String firstName, String lastName) {

        // Store registration details
        this.username = username;
        this.password = password;
        this.cellphoneNumber = cellNumber;
        this.firstName = firstName;
        this.lastName = lastName;

        // Send registration details to RegistrationChecker
        checker.username = username;
        checker.password = password;
        checker.cellNumber = cellNumber;

        // Check username
        if (!checker.checkUserName()) {
            return "Username is incorrectly formatted, please ensure that your username contains an underscore and is no more than 5 characters in length.";
        }

        // Check password
        if (!checker.checkPasswordComplexity()) {
            return "Password is incorrectly formatted, please ensure that the password contains at least 8 characters, a capital letter, a number and a special character.";
        }

        // Check cellphone number
        if (!checker.checkCellPhoneNumber()) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }

        return "User registered successfully.";
    }
}