package part3;

import part2.Message;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// holds Part 3 arrays & the features that work on them
public class MessageManager {

    private final String sender;

    // array list
    private final List<Message> sentMessages = new ArrayList<>();
    private final List<Message> disregardedMessages = new ArrayList<>();
    private final List<Message> storedMessages = new ArrayList<>();
    private final List<String> messageHashes = new ArrayList<>();
    private final List<String> messageIDs = new ArrayList<>();

    // sender is the logged-in user, shown next to each stored message's recipient
    public MessageManager(String sender) {
        this.sender = sender == null ? "" : sender.trim();
    }

    // populating the arrays

    // adds message to the Sent Message array
    public void addSent(Message message) {
        sentMessages.add(message);
        track(message);
    }

    // adds message to Disregarded Messages array
    public void addDisregarded(Message message) {
        disregardedMessages.add(message);
        track(message);
    }

    // adds message to Stored Messages array
    public void addStored(Message message) {
        storedMessages.add(message);
        track(message);
    }

    // records message's hash & ID in their arrays
    private void track(Message message) {

        if (!messageHashes.contains(message.getMessageHash())) {
            messageHashes.add(message.getMessageHash());
        }

        if (!messageIDs.contains(message.getMessageID())) {
            messageIDs.add(message.getMessageID());
        }
    }

    // reads JSON file into the Stored Messages array
    public void loadStoredMessages() {

        for (Message old : storedMessages) {
            forget(old);
        }

        storedMessages.clear();

        Path file = Message.getStorageFile();

        if (!Files.exists(file)) {
            return;
        }

        try {
            String json = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);

            for (Map<String, String> entry : parseJson(json)) {
                String id = entry.getOrDefault("messageID", "");
                String recipient = entry.getOrDefault("recipient", "");
                String text = entry.getOrDefault("message", "");
                int number = parseNumber(entry.get("messageNumber"));

                addStored(new Message(id, number, recipient, text));
            }
        }

        catch (IOException exception) {
            System.out.println("The stored messages could not be read: " + exception.getMessage());
        }
    }

    // stored message features (menu options a to f)

    // a. sender & recipient of every stored message
    public String displaySenderAndRecipient() {

        if (storedMessages.isEmpty()) {
            return "There are no stored messages.";
        }

        StringBuilder result = new StringBuilder();

        for (Message message : storedMessages) {
            result.append("Sender: ").append(sender).append(" | Recipient: ").append(message.getRecipient()).append(System.lineSeparator());
        }

        return result.toString().trim();
    }

    // b. longest stored message (first one wins a tie), or null when nothing is stored
    public String longestStoredMessage() {

        String longest = null;

        for (Message message : storedMessages) {
            if (longest == null || message.getMessageText().length() > longest.length()) {
                longest = message.getMessageText();
            }
        }

        return longest;
    }

    // c. finds a message by ID in the sent & stored arrays, or null when there is no match
    public Message findByMessageID(String id) {

        String wanted = id == null ? "" : id.trim();

        for (Message message : sentMessages) {
            if (message.getMessageID().equals(wanted)) {
                return message;
            }
        }

        for (Message message : storedMessages) {
            if (message.getMessageID().equals(wanted)) {
                return message;
            }
        }

        return null;
    }

    // c. text shown to the user for a message ID search
    public String searchByMessageID(String id) {

        Message message = findByMessageID(id);

        if (message == null) {
            return "No message found with ID " + (id == null ? "" : id.trim()) + ".";
        }

        return "Recipient: " + message.getRecipient() + System.lineSeparator() + "Message: " + message.getMessageText();
    }

    // d. the text of every sent or stored message for one recipient (sent first, then stored)
    public List<String> messagesForRecipient(String recipient) {

        String wanted = recipient == null ? "" : recipient.trim();
        List<String> found = new ArrayList<>();

        for (Message message : sentMessages) {
            if (message.getRecipient().equals(wanted)) {
                found.add(message.getMessageText());
            }
        }

        for (Message message : storedMessages) {
            if (message.getRecipient().equals(wanted)) {
                found.add(message.getMessageText());
            }
        }

        return found;
    }

    // e. deletes stored message by its hash, rewrites the JSON file and reports the result
    public String deleteByHash(String hash) {

        String wanted = hash == null ? "" : hash.trim();
        Message target = null;

        for (Message message : storedMessages) {
            if (message.getMessageHash().equals(wanted)) {
                target = message;
                break;
            }
        }

        if (target == null) {
            return "No stored message found with hash " + wanted + ".";
        }

        storedMessages.remove(target);
        forget(target);

        try {
            Message.writeStoredMessages(storedMessages);
        }

        catch (IOException exception) {
            return "The message was removed but the file could not be updated: " + exception.getMessage();
        }

        return "Message: \"" + target.getMessageText() + "\" successfully deleted.";
    }

    // f. report of every stored message
    public String reportStoredMessages() {

        return buildReport(storedMessages, "There are no stored messages.");
    }

    // report of every sent message (hash, recipient and message)
    public String reportSentMessages() {

        return buildReport(sentMessages, "No messages have been sent yet.");
    }

    private String buildReport(List<Message> messages, String emptyText) {

        if (messages.isEmpty()) {
            return emptyText;
        }

        StringBuilder report = new StringBuilder();

        for (Message message : messages) {
            report.append("Message ID: ").append(message.getMessageID()).append(System.lineSeparator())
                .append("Message Hash: ").append(message.getMessageHash()).append(System.lineSeparator())
                .append("Recipient: ").append(message.getRecipient()).append(System.lineSeparator())
                .append("Message: ").append(message.getMessageText()).append(System.lineSeparator())
                .append(System.lineSeparator());
        }

        return report.toString().trim();
    }

    // read-only views of the arrays 

    public List<Message> getSentMessages() {
        return Collections.unmodifiableList(sentMessages);
    }

    public List<Message> getDisregardedMessages() {
        return Collections.unmodifiableList(disregardedMessages);
    }

    public List<Message> getStoredMessages() {
        return Collections.unmodifiableList(storedMessages);
    }

    public List<String> getMessageHashes() {
        return Collections.unmodifiableList(messageHashes);
    }

    public List<String> getMessageIDs() {
        return Collections.unmodifiableList(messageIDs);
    }

    // just the text of each sent message, in order
    public List<String> getSentMessageTexts() {

        List<String> texts = new ArrayList<>();

        for (Message message : sentMessages) {
            texts.add(message.getMessageText());
        }

        return texts;
    }

    // helpers

    // removes a message's hash & ID from their arrays
    private void forget(Message message) {
        messageHashes.remove(message.getMessageHash());
        messageIDs.remove(message.getMessageID());
    }

    private static int parseNumber(String value) {

        try {
            return Integer.parseInt(value == null ? "0" : value.trim());
        }

        catch (NumberFormatException exception) {
            return 0;
        }
    }

    // small JSON reader for the flat array of objects that Message.storeMessage writes
    private static List<Map<String, String>> parseJson(String json) {

        List<Map<String, String>> objects = new ArrayList<>();
        int i = 0;
        int n = json.length();

        while (i < n) {

            if (json.charAt(i) != '{') {
                i++;
                continue;
            }

            i++;
            Map<String, String> object = new HashMap<>();

            while (i < n && json.charAt(i) != '}') {

                if (json.charAt(i) != '"') {
                    i++;
                    continue;
                }

                StringBuilder key = new StringBuilder();
                i = readString(json, i, key);

                while (i < n && json.charAt(i) != ':') {
                    i++;
                }

                i++;

                while (i < n && Character.isWhitespace(json.charAt(i))) {
                    i++;
                }

                StringBuilder value = new StringBuilder();

                if (i < n && json.charAt(i) == '"') {
                    i = readString(json, i, value);
                } else {
                    while (i < n && json.charAt(i) != ',' && json.charAt(i) != '}') {
                        value.append(json.charAt(i));
                        i++;
                    }
                }

                object.put(key.toString(), value.toString().trim());
            }

            i++;
            objects.add(object);
        }

        return objects;
    }

    // reads a quoted JSON string starting at its opening quote & returns the index after the closing quote
    private static int readString(String json, int start, StringBuilder out) {

        int i = start + 1;

        while (i < json.length()) {

            char c = json.charAt(i);

            if (c == '\\' && i + 1 < json.length()) {
                char next = json.charAt(i + 1);

                switch (next) {
                    case 'n':
                        out.append('\n');
                        break;
                    case 'r':
                        out.append('\r');
                        break;
                    case 't':
                        out.append('\t');
                        break;
                    default:
                        out.append(next);
                }

                i += 2;
            }

            else if (c == '"') {
                return i + 1;
            }

            else {
                out.append(c);
                i++;
            }
        }

        return i;
    }
}