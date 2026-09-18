package com.mycompany.prog5121.poe.st10515111;

import com.mycompany.prog5121.poe.st10515111.part1.Login;
import java.util.Scanner;

public class PROG5121POEST10515111 {

    public static void main(String[] args) {

        Login authSystem = new Login();

        try (Scanner userInput = new Scanner(System.in)) {

            String registrationOutput;

            // Registration Prompts Loop
            do {
                System.out.println("USER REGISTRATION");

                System.out.print("Enter your first name: ");
                String firstName = userInput.nextLine();

                System.out.print("Enter your last name: ");
                String lastName = userInput.nextLine();

                System.out.print("Enter username: ");
                String username = userInput.nextLine();

                System.out.print("Create a password: ");
                String password = userInput.nextLine();

                System.out.print("Enter your cellphone number (+27...): ");
                String cellNumber = userInput.nextLine();

                registrationOutput = authSystem.registerUser(
                        username,
                        password,
                        cellNumber,
                        firstName,
                        lastName
                );

                System.out.println(registrationOutput);

            } while (!registrationOutput.equals("User registered successfully."));

            boolean isAuthenticated;

            // Login Prompts Loop
            do {
                System.out.println("USER LOGIN");

                System.out.print("Enter Username: ");
                String loginUsername = userInput.nextLine();

                System.out.print("Enter your Password: ");
                String loginPassword = userInput.nextLine();

                isAuthenticated = authSystem.loginUser(loginUsername, loginPassword);
                String loginOutput = authSystem.returnLoginStatus(loginUsername, loginPassword);

                System.out.println(loginOutput);

            } while (!isAuthenticated);
        }
    }
}