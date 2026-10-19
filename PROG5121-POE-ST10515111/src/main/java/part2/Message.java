package part2;

// reuses the Part 1 cell phone check so the rule lives in one place
import part1.RegistrationChecker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Message {

    // status texts shared between the checks, SentMessage and the tests
    private static final String MESSAGE_READY = "Message ready to send.";
    private static final String CELL_CAPTURED = "Cell phone number successfully captured.";
    private static final String CELL_INVALID = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final RegistrationChecker CHECKER = new RegistrationChecker();
    private static final Set<String> USED_IDS = new HashSet<>();
    private static final List<Message> SENT_MESSAGES = new ArrayList<>();
    private static final List<Message> DISREGARDED_MESSAGES = new ArrayList<>();
    private static final List<Message> STORED_MESSAGES = new ArrayList<>();
    private static int totalMessagesSent = 0;

    // file that stored messages are written to
    private static Path storageFile = Paths.get("messages.json");

    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;

    private String actionStatus = "Message not processed.";
    private boolean disregarded = false;
    private boolean deleted = false;

    // initializes message details & generates a random ID and its hash
    public Message(int messageNumber, String recipient, String messageText) {
        this(generateUniqueMessageID(), messageNumber, recipient, messageText);
    }

    // initializes message details with a known ID
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID == null ? "" : messageID.trim();
        this.messageNumber = messageNumber;
        this.recipient = recipient == null ? "" : recipient.trim();
        this.messageText = messageText == null ? "" : messageText.trim();

        synchronized (USED_IDS) {
            USED_IDS.add(this.messageID);
        }

        this.messageHash = createMessageHash();
    }

    // creates a unique random ten digit message ID for tracking (leading zeros allowed, always 10 characters)
    private static String generateUniqueMessageID() {
        String id;

        synchronized (USED_IDS) {

            do {
                id = String.format("%010d", Math.floorMod(RANDOM.nextLong(), 10_000_000_000L));
            } while (USED_IDS.contains(id));

            USED_IDS.add(id);
        }

        return id;
    }

    // confirms that message ID is not more than ten characters
    public boolean checkMessageID() {

        return messageID.length() <= 10;
    }

    // checks this message's recipient number
    public String checkRecipientCell() {

        return checkRecipientCell(recipient);
    }

    // checks any recipient number without needing a Message object (reuses the Part 1 check)
    public static String checkRecipientCell(String recipient) {

        String trimmed = recipient == null ? null : recipient.trim();

        return CHECKER.checkCellPhoneNumber(trimmed) ? CELL_CAPTURED : CELL_INVALID;
    }

    // checks this message's length before the user chooses what to do with it
    public String validateMessage() {

        return validateMessage(messageText);
    }

    // checks any message text against the 250 character limit
    public static String validateMessage(String text) {

        int length = text == null ? 0 : text.trim().length();

        if (length > 250) {

            return "Message exceeds 250 characters by " + (length - 250) + "; please reduce the size.";
        }

        return MESSAGE_READY;
    }

    // returns the "Message ID generated" text for this message
    public String getMessageIDStatus() {

        return "Message ID generated: " + messageID;
    }

    // builds hash from the first two ID digits, message number, and first and last words (punctuation removed)
    public String createMessageHash() {
        // words made up only of punctuation are skipped
        List<String> words = new ArrayList<>();

        for (String token : messageText.split("\\s+")) {
            String word = stripPunctuation(token);

            if (!word.isEmpty()) {
                words.add(word);
            }
        }

        String firstWord = words.isEmpty() ? "" : words.get(0);
        String lastWord = words.isEmpty() ? "" : words.get(words.size() - 1);
        String idPrefix = messageID.length() >= 2 ? messageID.substring(0, 2) : messageID;

        return idPrefix + ":" + messageNumber + ":" + (firstWord + lastWord).toUpperCase();
    }

    // removes anything that is not a letter or digit
    private static String stripPunctuation(String word) {

        return word.replaceAll("[^\\p{L}\\p{N}]", "");
    }

    // returns the reason a message cannot be sent or stored, or null when it is valid
    private String validationError() {

        String lengthStatus = validateMessage();

        if (!MESSAGE_READY.equals(lengthStatus)) {
            return lengthStatus;
        }

        String cellStatus = checkRecipientCell();

        if (!CELL_CAPTURED.equals(cellStatus)) {
            return cellStatus;
        }

        return null;
    }

    // processes selected action & returns required status message
    public String SentMessage(String choice) {

        String option = choice == null ? "" : choice.trim();

        switch (option) {

            case "1":
            case "Send":
            case "send": {
                String error = validationError();

                if (error != null) {
                    actionStatus = error;
                    break;
                }

                SENT_MESSAGES.add(this);
                totalMessagesSent++;
                actionStatus = "Message successfully sent.";
                break;
            }

            // disregarding needs no recipient check, the message is simply not sent
            case "2":
            case "Disregard":
            case "disregard":
                if (!disregarded) {
                    disregarded = true;
                    DISREGARDED_MESSAGES.add(this);
                }

                actionStatus = "Press 0 to delete the message.";
                break;

            // pressing 0 after disregarding deletes the message
            case "0":
                if (disregarded) {
                    deleted = true;
                    actionStatus = "Message deleted.";
                } else {
                    actionStatus = "Invalid option. Please choose 1, 2, or 3.";
                }

                break;

            case "3":
            case "Store":
            case "store": {
                String error = validationError();

                if (error != null) {
                    actionStatus = error;
                    break;
                }

                STORED_MESSAGES.add(this);
                storeMessage();
                actionStatus = "Message successfully stored.";
                break;
            }

            default:
                actionStatus = "Invalid option. Please choose 1, 2, or 3.";
        }

        return actionStatus;
    }

    // appends the message to JSON array in the storage file
    public void storeMessage() {

        Path file = storageFile;
        String jsonObject = "  {" + "\"messageID\":\"" + escapeJson(messageID) + "\"," + "\"messageNumber\":" + messageNumber + "," + "\"recipient\":\"" + escapeJson(recipient) + "\"," + "\"message\":\"" + escapeJson(messageText) + "\"," + "\"messageHash\":\"" + escapeJson(messageHash) + "\"" + "}";

        try {
            String existing = Files.exists(file) ? new String(Files.readAllBytes(file), StandardCharsets.UTF_8).trim() : "";

            if (existing.isEmpty() || "[]".equals(existing)) {
                Files.write(file, ("[" + System.lineSeparator() + jsonObject + System.lineSeparator() + "]").getBytes(StandardCharsets.UTF_8));
            }

            else if (existing.endsWith("]")) {
                String body = existing.substring(0, existing.length() - 1).trim();
                String separator = body.endsWith("[") ? "" : "," + System.lineSeparator();
                Files.write(file, (body + separator + jsonObject + System.lineSeparator() + "]").getBytes(StandardCharsets.UTF_8));
            }
        }

        catch (IOException exception) {
            System.out.println("The message could not be stored: " + exception.getMessage());
        }
    }

    // escapes characters that could otherwise break the JSON structure
    private String escapeJson(String value) {

        return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

    // returns readable summary of every message sent during the program run
    public static String printMessages() {

        if (SENT_MESSAGES.isEmpty()) {

            return "No messages have been sent yet.";
        }

        StringBuilder result = new StringBuilder();

        for (Message message : SENT_MESSAGES) {

            result.append("Message ID: ").append(message.messageID).append(System.lineSeparator())
                .append("Message Hash: ").append(message.messageHash).append(System.lineSeparator())
                .append("Recipient: ").append(message.recipient).append(System.lineSeparator())
                .append("Message: ").append(message.messageText).append(System.lineSeparator());
        }

        return result.toString().trim();
    }

    // returns number of messages sent successfully in this program run
    public int returnTotalMessages() {

        return totalMessagesSent;
    }

    // returns number of messages sent successfully in this program run
    public static int getTotalMessagesSent() {

        return totalMessagesSent;
    }

    // clears all static state (sent / disregarded / stored lists, used IDs and the total) so tests start clean
    public static void resetState() {

        synchronized (USED_IDS) {
            USED_IDS.clear();
        }

        SENT_MESSAGES.clear();
        DISREGARDED_MESSAGES.clear();
        STORED_MESSAGES.clear();
        totalMessagesSent = 0;
    }

    // redirects where stored messages are written (used by unit tests so messages.json is not touched)
    public static void setStorageFile(Path file) {

        storageFile = file;
    }

    // returns the messages the user chose to disregard (read-only view)
    public static List<Message> getDisregardedMessages() {

        return Collections.unmodifiableList(DISREGARDED_MESSAGES);
    }

    // returns unique ID assigned to this message
    public String getMessageID() {

        return messageID;
    }

    // returns message number
    public int getMessageNumber() {

        return messageNumber;
    }

    // returns recipient's cellphone number
    public String getRecipient() {

        return recipient;
    }

    // returns message content
    public String getMessageText() {

        return messageText;
    }

    // returns generated message hash
    public String getMessageHash() {
        return messageHash;
    }

    // returns result of the last message action
    public String getActionStatus() {

        return actionStatus;
    }

    // true once the user has chosen to disregard this message
    public boolean isDisregarded() {

        return disregarded;
    }

    // true once the user has pressed 0 to delete a disregarded message
    public boolean isDeleted() {

        return deleted;
    }
}