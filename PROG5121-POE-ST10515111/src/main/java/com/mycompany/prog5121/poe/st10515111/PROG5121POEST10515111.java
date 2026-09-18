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
        System.out.println("=== USER REGISTRATION ===");
        System.out.print("Enter First Name: ");
        firstName = userInput.nextLine();

        System.out.print("Enter Last Name: ");
        lastName = userInput.nextLine();

        System.out.print("Enter Username: ");
        username = userInput.nextLine();

        System.out.print("Enter Password: ");
        password = userInput.nextLine();

        System.out.print("Enter Cell Phone (+27...): ");
        cellNumber = userInput.nextLine();

        registrationOutput = authSystem.registerUser(username, password, cellNumber, firstName, lastName);
        System.out.println("\n" + registrationOutput);

        // Login Prompts
        System.out.println("\n=== USER LOGIN ===");
        System.out.print("Enter Username: ");
        loginUsername = userInput.nextLine();

        System.out.print("Enter Password: ");
        loginPassword = userInput.nextLine();

        loginOutput = authSystem.returnLoginStatus(loginUsername, loginPassword);
        System.out.println("\n" + loginOutput);

        userInput.close();
    }
}