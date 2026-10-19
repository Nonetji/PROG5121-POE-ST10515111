package com.mycompany.prog5121.poe.st10515111;

// imports the Login class from the part1 package
import part1.Login;
// imports message class
import part2.Message;

// imports Scanner
import java.util.Scanner;

public class PROG5121POEST10515111 {

    public static void main(String[] args) {
        
        // user input is stored in variable 
        Login authSystem = new Login();

        // try instantiates scanner
        try (Scanner userInput = new Scanner(System.in)) {
            String registrationOutput;

            // registration prompts loop
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

                registrationOutput = authSystem.registerUser(username, password, cellNumber, firstName, lastName);
                System.out.println(registrationOutput);

            } 
            
            while (!registrationOutput.equals("User registered successfully."));

            boolean isAuthenticated;

            // login prompts loop
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
            
            // displays welcome message after successful authentication
            System.out.println("Welcome to QuickChat.");
            
            // keeps menu available until user selects "Quit"
            boolean running = true;

            while (running) {
                System.out.println();
                System.out.println("QUICKCHAT MENU");
                System.out.println("1. Send Messages");
                System.out.println("2. Coming Soon.");
                System.out.println("3. Quit");

                System.out.print("Choose an option: ");
                String menuChoice = userInput.nextLine().trim();

                switch (menuChoice) {
                    case "1":
                        sendMessages(userInput);
                        break;

                    // displays sent messages & total sent-message count
                    case "2":
                        System.out.println(Message.printMessages());

                        System.out.println(
                            "Total messages sent: "+ Message.getTotalMessagesSent());
                        break;

                    case "3":
                        running = false;
                        System.out.println("Thank you for using QuickChat.");
                        break;

                    default:
                        System.out.println("Invalid menu option. Please enter 1, 2, or 3.");
                }
            }
        }
    }
    
// collects requested number of messages & processes each one in order
private static void sendMessages(Scanner userInput) {

    int numberOfMessages;

    // only accepts a positive whole number for requested message count
    while (true) {

        System.out.print("How many messages would you like to send? ");

        String countInput = userInput.nextLine().trim();

        try {

            numberOfMessages = Integer.parseInt(countInput);

            if (numberOfMessages > 0) {
                break;
            }

        } 
        
        catch (NumberFormatException ignored) {
        // requests another input when value is not a whole number
        }

        System.out.println("Please enter a whole number greater than zero.");
    }

    // uses array to retain each message created during this send session
    Message[] messages = new Message[numberOfMessages];

    for (int i = 0; i < numberOfMessages; i++) {

        System.out.println();
        System.out.println("Message " + (i + 1) + " of " + numberOfMessages);

        String recipient;

        // repeats prompt until a valid recipient phone number is entered
        while (true) {

            System.out.print("Enter recipient cellphone number (+27 followed by 9 digits): ");

            recipient = userInput.nextLine().trim();

            Message recipientCheck = new Message(i + 1,recipient,"Validation message");

            String recipientStatus = recipientCheck.checkRecipientCell();

            if ("Cell phone number successfully captured.".equals(recipientStatus)) {

                break;

            } else {

                System.out.println(recipientStatus);
            }
        }

        String messageText;

        // prevents messages longer than 250 characters from being processed
        while (true) {

            System.out.print("Enter your message (maximum 250 characters): ");

            messageText = userInput.nextLine();

            if (messageText.length() <= 250) {
                break;
            }

            System.out.println("Message exceeds 250 characters by " + (messageText.length() - 250) + "; please reduce the size.");
        }

        // creates the message after recipient & content are validated
        messages[i] = new Message(i + 1, recipient, messageText);

        System.out.println(messages[i].validateMessage());

        System.out.println("Message ID generated: " + messages[i].getMessageID());

        System.out.println("Message Hash: " + messages[i].getMessageHash());

        System.out.println(messages[i].checkRecipientCell());

        System.out.println();
        System.out.println("Choose an option:");
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");

        System.out.print("Enter your choice: ");

        String actionChoice = userInput.nextLine().trim();

        String status = messages[i].SentMessage(actionChoice);

        System.out.println(status);

        // shows required message details after a successful send
        if ("Message successfully sent.".equals(status)) {

            System.out.println("Message ID: " + messages[i].getMessageID());

            System.out.println("Message Hash: " + messages[i].getMessageHash());

            System.out.println("Recipient: " + messages[i].getRecipient());

            System.out.println("Message: " + messages[i].getMessageText());
        }
    }

    // reports running total of successfully sent messages
    int totalSent = messages.length == 0 ? 0 : messages[0].returnTotalMessages();

    System.out.println("Total messages sent: " + totalSent);
    }
}