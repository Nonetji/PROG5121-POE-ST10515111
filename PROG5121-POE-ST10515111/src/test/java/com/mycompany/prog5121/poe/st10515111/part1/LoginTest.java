package com.mycompany.prog5121.poe.st10515111.part1;

import part1.RegistrationChecker;
import part1.Login;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    // USERNAME TESTS 

    @Test
    public void testUsernameCorrectlyFormatted() {
        RegistrationChecker checker = new RegistrationChecker();
        assertTrue(checker.checkUserName("kyl_1"), 
                "Username correctly formatted with underscore and length <= 5");
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        RegistrationChecker checker = new RegistrationChecker();
        assertFalse(checker.checkUserName("kyle!!!!!!"), 
                "Username improperly formatted");
    }

    // PASSWORD TESTS 

    @Test
    public void testPasswordMeetsComplexity() {
        RegistrationChecker checker = new RegistrationChecker();
        assertTrue(checker.checkPasswordComplexity("Ch&&secke99!"), 
                "Password meets all complexity requirements");
    }

    @Test
    public void testPasswordFailsComplexity() {
        RegistrationChecker checker = new RegistrationChecker();
        assertFalse(checker.checkPasswordComplexity("password"), 
                "Password does not meet complexity requirements");
    }

    // PHONE NUMBER TESTS 

    @Test
    public void testCellPhoneNumberCorrectlyFormatted() {
        RegistrationChecker checker = new RegistrationChecker();
        assertTrue(checker.checkCellPhoneNumber("+27838968976"), 
                "Cell phone number formatted correctly");
    }

    @Test
    public void testCellPhoneNumberIncorrectlyFormatted() {
        RegistrationChecker checker = new RegistrationChecker();
        assertFalse(checker.checkCellPhoneNumber("08966553"), 
                "Cell phone number improperly formatted");
    }

    // REGISTRATION TESTS

    @Test
    public void testRegisterUserSuccess() {
        Login login = new Login();
        String actual = login.registerUser("kyl_1", "Ch&&secke99!", "+27838968976", "Kyle", "Smith");
        String expected = "User registered successfully.";
        
        assertEquals(expected, actual, "Registration response string must match expected success message.");
    }

    // LOGIN TESTS

    @Test
    public void testLoginUserSuccess() {
        Login login = new Login();
        login.registerUser("kyl_1", "Ch&&secke99!", "+27838968976", "Kyle", "Smith");

        boolean isAuthenticated = login.loginUser("kyl_1", "Ch&&secke99!");
        assertTrue(isAuthenticated, "Login should succeed with correct credentials.");

        String actualStatus = login.returnLoginStatus("kyl_1", "Ch&&secke99!");
        String expectedStatus = "Welcome back Kyle, Smith it is great to see you again.";
        
        assertEquals(expectedStatus, actualStatus, "Login status message must match required format.");
    }

    @Test
    public void testLoginUserFailure() {
        Login login = new Login();
        login.registerUser("kyl_1", "Ch&&secke99!", "+27838968976", "Kyle", "Smith");

        boolean isAuthenticated = login.loginUser("wrong_user", "wrong_pass");
        assertFalse(isAuthenticated, "Login should fail with incorrect credentials.");

        String actualStatus = login.returnLoginStatus("wrong_user", "wrong_pass");
        String expectedStatus = "Username or password incorrect, please try again.";
        
        assertEquals(expectedStatus, actualStatus, "Login status message must match required failure string.");
    }
}