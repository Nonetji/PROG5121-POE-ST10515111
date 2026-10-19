package part2;
 
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
 
public class Message {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Set<String> USED_IDS = new HashSet<>();
    private static final List<Message> SENT_MESSAGES = new ArrayList<>();
    private static final List<Message> STORED_MESSAGES = new ArrayList<>();
    private static int totalMessagesSent = 0;
 
    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;
 
    private String actionStatus = "Message not processed.";
    
    // initializes message details & generates its ID and hash
    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient == null ? "" : recipient.trim();
        this.messageText = messageText == null ? "" : messageText.trim();
        this.messageID = generateUniqueMessageID();
        this.messageHash = createMessageHash();
    }
    
    // creates unique ten digit message ID for tracking
    private String generateUniqueMessageID() {
        String id;
 
        synchronized (USED_IDS) {
 
            do {
                id = String.format("%010d",RANDOM.nextInt(1_000_000_000));
            }
            
            while (USED_IDS.contains(id));
            USED_IDS.add(id);
            }
 
        return id;
    }
    
    // confirms that message ID contains less than 10 characters
    public boolean checkMessageID() {
 
    return messageID.length() <= 10;
    }
    
    // checks that recipient uses a South African number
    public String checkRecipientCell() {
 
        if (recipient.matches("^\\+27\\d{9}$")) {
 
            return "Cell phone number successfully captured.";
        }
 
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }
    
    // checks message length before user chooses what to do with it
    public String validateMessage() {
 
        if (messageText.length() > 250) {
 
            return "Message exceeds 250 characters by " + (messageText.length() - 250) + "; please reduce the size.";
        }
 
        return "Message ready to send.";
    }
    
    // builds hash from the first two ID digits, message number, and first and last words
    public String createMessageHash() {
        String[] words = messageText.trim().isEmpty()? new String[0] : messageText.trim().split("\\s+");
        String firstWord = words.length == 0 ? "" : words[0];
        String lastWord = words.length == 0 ? "" : words[words.length - 1];
 
        return messageID.substring(0, 2) + ":" + messageNumber + ":" + (firstWord + lastWord).toUpperCase();
    }
    
    // processes selected action & returns required status message
    public String SentMessage(String choice) {
 
        if (!"Message ready to send.".equals(validateMessage())) {
            actionStatus = validateMessage();
 
            return actionStatus;
        }
 
        if (!"Cell phone number successfully captured.".equals(checkRecipientCell())) {
            actionStatus = checkRecipientCell();
 
            return actionStatus;
        }
 
        switch (choice == null ? "" : choice.trim()) {
 
            case "1":
            case "Send":
            case "send":
                SENT_MESSAGES.add(this);
                totalMessagesSent++;
                actionStatus = "Message successfully sent.";
                break;
 
            case "2":
            case "Disregard":
            case "disregard":
                actionStatus = "Press 0 to delete the message.";
                break;
 
            case "3":
            case "Store":
            case "store":
                STORED_MESSAGES.add(this);
                storeMessage();
                actionStatus = "Message successfully stored.";
                break;
 
            default:
                actionStatus = "Invalid option. Please choose 1, 2, or 3.";
        }
 
        return actionStatus;
    }
    
    // appends the message to JSON array in messages.json
    public void storeMessage() {
 
        Path file = Paths.get("messages.json");
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
        System.out.println("The message could not be stored: "+ exception.getMessage());
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
}