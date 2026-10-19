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

            // the user sets, once, how many messages they will enter in this run of the application
            int numberOfMessages = askNumberOfMessages(userInput);

            // uses array to retain each message created during the run
            Message[] messages = new Message[numberOfMessages];
            boolean messagesEntered = false;
            
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
                        // the set number of messages can only be entered once
                        if (messagesEntered) {
                            System.out.println("You have already entered all " + numberOfMessages + " of your messages.");
                        } else {
                            sendMessages(userInput, messages);
                            messagesEntered = true;
                        }
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

    // asks (once, when the application starts) how many messages the user wants to enter
    private static int askNumberOfMessages(Scanner userInput) {

        // only accepts a positive whole number for requested message count
        while (true) {

            System.out.print("How many messages would you like to enter? ");

            String countInput = userInput.nextLine().trim();

            try {

                int numberOfMessages = Integer.parseInt(countInput);

                if (numberOfMessages > 0) {
                    return numberOfMessages;
                }

            } 
            
            catch (NumberFormatException ignored) {
                // requests another input when value is not a whole number
            }

            System.out.println("Please enter a whole number greater than zero.");
        }
    }

    // collects every message in the set quantity & processes each one in order
    private static void sendMessages(Scanner userInput, Message[] messages) {

        for (int i = 0; i < messages.length; i++) {

            System.out.println();
            System.out.println("Message " + (i + 1) + " of " + messages.length);

            String recipient;

            // repeats prompt until a valid recipient phone number is entered (reuses the Part 1 cell check)
            while (true) {

                System.out.print("Enter recipient cellphone number (+27 followed by 9 digits): ");

                recipient = userInput.nextLine().trim();

                String recipientStatus = Message.checkRecipientCell(recipient);

                if ("Cell phone number successfully captured.".equals(recipientStatus)) {

                    break;
                }

                System.out.println(recipientStatus);
            }

            String messageText;

            // prevents messages longer than 250 characters from being processed
            while (true) {

                System.out.print("Enter your message (maximum 250 characters): ");

                messageText = userInput.nextLine();

                String lengthStatus = Message.validateMessage(messageText);

                if ("Message ready to send.".equals(lengthStatus)) {
                    break;
                }

                System.out.println(lengthStatus);
            }

            // creates the message after recipient & content are validated (message number starts at 0)
            messages[i] = new Message(i, recipient, messageText);

            System.out.println(messages[i].validateMessage());

            System.out.println(messages[i].getMessageIDStatus());

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

            // a disregarded message is deleted when the user presses 0
            if ("Press 0 to delete the message.".equals(status)) {

                String deleteChoice = userInput.nextLine().trim();

                if ("0".equals(deleteChoice)) {

                    System.out.println(messages[i].SentMessage("0"));
                }
            }

            // shows required message details after a successful send
            if ("Message successfully sent.".equals(status)) {

                System.out.println("Message ID: " + messages[i].getMessageID());

                System.out.println("Message Hash: " + messages[i].getMessageHash());

                System.out.println("Recipient: " + messages[i].getRecipient());

                System.out.println("Message: " + messages[i].getMessageText());
            }
        }

        // reports total of successfully sent messages once all messages have been entered
        System.out.println();
        System.out.println("Total messages sent: " + Message.getTotalMessagesSent());
    }
}
 