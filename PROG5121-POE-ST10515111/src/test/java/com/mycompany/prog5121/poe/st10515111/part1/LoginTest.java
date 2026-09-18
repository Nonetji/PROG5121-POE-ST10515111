package com.mycompany.prog5121.poe.st10515111.part1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    // 4.
    // Test if username is correctly formatted
    @Test
    public void testUsernameCorrectlyFormatted() {
        RegistrationChecker checker = new RegistrationChecker();
        checker.username = "kyl_1";
        
        assertTrue(checker.checkUserName(), "Username containing underscore and <= 5 chars should return true.");
    }

    // Test if username is incorrectly formatted
    @Test
    public void testUsernameIncorrectlyFormatted() {
        RegistrationChecker checker = new RegistrationChecker();
        checker.username = "kyle!!!!!!";
        
        assertFalse(checker.checkUserName(), "Username exceeding 5 chars should return false.");
    }

    // Test if password meets complexity requirements
    @Test
    public void testPasswordMeetsComplexity() {
        RegistrationChecker checker = new RegistrationChecker();
        checker.password = "Ch&&secke99!";
        
        assertTrue(checker.checkPasswordComplexity(), "Password meeting complexity requirements should return true.");
    }

    // Test if password does not meet complexity requirements
    @Test
    public void testPasswordFailsComplexity() {
        RegistrationChecker checker = new RegistrationChecker();
        checker.password = "password";
        
        assertFalse(checker.checkPasswordComplexity(), "Simple password without numbers/specials should return false.");
    }

    // Test if cell phone number is Correctly Formatted
    @Test
    public void testCellPhoneNumberCorrectlyFormatted() {
        RegistrationChecker checker = new RegistrationChecker();
        checker.cellNumber = "+27838968976";
        
        assertTrue(checker.checkCellPhoneNumber(), "Cell number starting with +27 and 9 trailing digits should return true.");
    }

    // Test if cell phone number is incorrectly formatted
    @Test
    public void testCellPhoneNumberIncorrectlyFormatted() {
        RegistrationChecker checker = new RegistrationChecker();
        checker.cellNumber = "08966553";
        
        assertFalse(checker.checkCellPhoneNumber(), "Cell number missing +27 international prefix should return false.");
    }

    // Test for registration success
    @Test
    public void testRegisterUserSuccess() {
        Login login = new Login();
        String result = login.registerUser("kyl_1", "Ch&&secke99!", "+27838968976", "Kyle", "Smith");
        
        assertEquals("User registered successfully.", result);
    }

    // Test for login success
    @Test
    public void testLoginUserSuccess() {
        Login login = new Login();
        login.registerUser("kyl_1", "Ch&&secke99!", "+27838968976", "Kyle", "Smith");

        boolean isAuthenticated = login.loginUser("kyl_1", "Ch&&secke99!");
        assertTrue(isAuthenticated, "Matching login details should authenticate successfully.");

        String actualStatus = login.returnLoginStatus("kyl_1", "Ch&&secke99!");
        assertEquals("Welcome back kyl_1", actualStatus);
    }

    // Test for login failure
    @Test
    public void testLoginUserFailure() {
        Login login = new Login();
        login.registerUser("kyl_1", "Ch&&secke99!", "+27838968976", "Kyle", "Smith");

        boolean isAuthenticated = login.loginUser("wrong_user", "wrong_pass");
        assertFalse(isAuthenticated, "Incorrect login details should fail authentication.");

        String actualStatus = login.returnLoginStatus("wrong_user", "wrong_pass");
        assertEquals("Username or password incorrect, please try again.", actualStatus);
    }
}