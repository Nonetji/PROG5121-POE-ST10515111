package com.mycompany.prog5121.poe.st10515111.part1;

public class Login {

    private String username;
    private String password;
    private String cellphoneNumber;
    private String firstName;
    private String lastName;

    private final RegistrationChecker checker = new RegistrationChecker();

    public boolean loginUser(String usernameInput, String passwordInput) {
        return username != null 
                && password != null 
                && username.equals(usernameInput) 
                && password.equals(passwordInput);
    }

    public String returnLoginStatus(String usernameInput, String passwordInput) {
        if (loginUser(usernameInput, passwordInput)) {
            return "Welcome back " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    public String registerUser(String username, String password, String cellNumber, String firstName, String lastName) {

        if (!checker.checkUserName(username)) {
            return "Username is incorrectly formatted, please ensure that your username contains an underscore and is no more than 5 characters in length.";
        }

        if (!checker.checkPasswordComplexity(password)) {
            return "Password is incorrectly formatted, please ensure that the password contains at least 8 characters, a capital letter, a number and a special character.";
        }

        if (!checker.checkCellPhoneNumber(cellNumber)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }

        this.username = username;
        this.password = password;
        this.cellphoneNumber = cellNumber;
        this.firstName = firstName;
        this.lastName = lastName;

        return "User registered successfully.";
    }
}