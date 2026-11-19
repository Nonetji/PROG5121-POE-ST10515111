package part3;

import java.util.List;
import java.util.Scanner;

// the "Stored Messages" sub menu (main menu option 4)
public class StoredMessagesMenu {

    public static void run(Scanner userInput, MessageManager manager) {

        // the JSON file is the source of truth for stored messages, so it is read again every time the menu opens
        manager.loadStoredMessages();

        boolean back = false;

        while (!back) {

            System.out.println();
            System.out.println("STORED MESSAGES");
            System.out.println("a. Display sender and recipient of all stored messages");
            System.out.println("b. Display the longest stored message");
            System.out.println("c. Search for a message ID");
            System.out.println("d. Search for all messages to a recipient");
            System.out.println("e. Delete a message using its hash");
            System.out.println("f. Display a report of all stored messages");
            System.out.println("g. Back to main menu");

            System.out.print("Choose an option: ");

            String choice = userInput.nextLine().trim().toLowerCase();

            switch (choice) {

                case "a":
                    System.out.println(manager.displaySenderAndRecipient());
                    break;

                case "b":
                    String longest = manager.longestStoredMessage();
                    System.out.println(longest == null ? "There are no stored messages." : "Longest stored message: " + longest);
                    break;

                case "c":
                    System.out.print("Enter the message ID: ");
                    System.out.println(manager.searchByMessageID(userInput.nextLine()));
                    break;

                case "d":
                    System.out.print("Enter the recipient's cellphone number: ");
                    List<String> found = manager.messagesForRecipient(userInput.nextLine());

                    if (found.isEmpty()) {
                        System.out.println("No messages found for that recipient.");
                    } else {
                        for (String text : found) {
                            System.out.println("\"" + text + "\"");
                        }
                    }
                    break;

                case "e":
                    System.out.print("Enter the message hash: ");
                    System.out.println(manager.deleteByHash(userInput.nextLine()));
                    break;

                case "f":
                    System.out.println(manager.reportStoredMessages());
                    break;

                case "g":
                    back = true;
                    break;

                default:
                    System.out.println("Invalid option. Please choose a, b, c, d, e, f or g.");
            }
        }
    }
}