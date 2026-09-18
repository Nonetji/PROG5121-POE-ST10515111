package com.mycompany.prog5121.poe.st10515111;

// linking main file with Login.java
import com.mycompany.prog5121.poe.st10515111.part1.Login;

// import java
import java.util.Scanner;

public class PROG5121POEST10515111 {

    // 3.
    // Main method handling user inputs and application workflow
    public static void main(String[] args) {

        // Scanner collecting user inputs
        Scanner userInput = new Scanner(System.in);
        Login authSystem = new Login();

        // Declaring variables
        String firstName;
        String lastName;
        String username;
        String password;
        String cellNumber;
        String registrationOutput;
        String loginUsername;
        String loginPassword;
        String loginOutput;

        // Registration Prompts
        do {
            System.out.println("USER REGISTRATION");

            System.out.print("Enter your first name: ");
            firstName = userInput.nextLine();

            System.out.print("Enter your last name: ");
            lastName = userInput.nextLine();

            System.out.print("Enter username: ");
            username = userInput.nextLine();

            System.out.print("Create a password: ");
            password = userInput.nextLine();

            System.out.print("Enter your cellphone number (+27...): ");
            cellNumber = userInput.nextLine();

            registrationOutput = authSystem.registerUser(
                    username,
                    password,
                    cellNumber,
                    firstName,
                    lastName
            );

            System.out.println(registrationOutput);

        } 
        
        while (!registrationOutput.equals("User registered successfully."));

        // Login Prompts
        do {
            System.out.println("USER LOGIN");

            System.out.print("Enter Username: ");
            loginUsername = userInput.nextLine();

            System.out.print("Enter your Password: ");
            loginPassword = userInput.nextLine();

            loginOutput = authSystem.returnLoginStatus(
                    loginUsername,
                    loginPassword
            );

            System.out.println(loginOutput);

        } 
        
        while (loginOutput.equals("Username or password incorrect, please try again."));
    }
}