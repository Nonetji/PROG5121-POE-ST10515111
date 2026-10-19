package com.mycompany.prog5121.poe.st10515111.part1;

import part2.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class MessageTest {

    // spec test data
    private static final String RECIPIENT_1 = "+27718693002";
    private static final String MESSAGE_1 = "Hi Mike, can you join us for dinner tonight?";
    private static final String RECIPIENT_2 = "08575975889";
    private static final String MESSAGE_2 = "Hi Keegan, did you receive the payment?";

    @TempDir
    Path tempDir;

    // every test starts with empty lists / total & writes JSON to a temp folder, not messages.json
    @BeforeEach
    public void setUp() {
        Message.resetState();
        Message.setStorageFile(tempDir.resolve("messages.json"));
    }

    // recipient number

    @Test
    public void testRecipientNumberCorrectlyFormatted() {
        Message message = new Message(0, RECIPIENT_1, MESSAGE_1);

        assertEquals("Cell phone number successfully captured.", message.checkRecipientCell());
    }

    @Test
    public void testRecipientNumberIncorrectlyFormatted() {
        Message message = new Message(1, RECIPIENT_2, MESSAGE_2);

        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
            message.checkRecipientCell());
    }

    @Test
    public void testStaticRecipientCheckMatchesInstanceCheck() {
        assertEquals("Cell phone number successfully captured.", Message.checkRecipientCell(RECIPIENT_1));
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
            Message.checkRecipientCell(RECIPIENT_2));
    }

    // message length

    @Test
    public void testMessageNotMoreThan250Characters() {
        Message message = new Message(0, RECIPIENT_1, MESSAGE_1);

        assertEquals("Message ready to send.", message.validateMessage());
    }

    @Test
    public void testMessageExactly250CharactersIsAccepted() {
        Message message = new Message(0, RECIPIENT_1, "a".repeat(250));

        assertEquals("Message ready to send.", message.validateMessage());
    }

    @Test
    public void testMessageMoreThan250Characters() {
        Message message = new Message(0, RECIPIENT_1, "a".repeat(260));

        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", message.validateMessage());
    }

    // message ID

    @Test
    public void testMessageIDIsTenDigitsAndPassesCheck() {
        Message message = new Message(0, RECIPIENT_1, MESSAGE_1);

        assertTrue(message.getMessageID().matches("\\d{10}"));
        assertTrue(message.checkMessageID());
    }

    @Test
    public void testMessageIDTooLongFailsCheck() {
        Message message = new Message("12345678901", 0, RECIPIENT_1, MESSAGE_1);

        assertFalse(message.checkMessageID());
    }

    @Test
    public void testMessageIDCreated() {
        Message message = new Message(0, RECIPIENT_1, MESSAGE_1);

        assertEquals("Message ID generated: " + message.getMessageID(), message.getMessageIDStatus());
    }

    @Test
    public void testMessageIDsAreUnique() {
        Message first = new Message(0, RECIPIENT_1, MESSAGE_1);
        Message second = new Message(1, RECIPIENT_1, MESSAGE_1);

        assertNotEquals(first.getMessageID(), second.getMessageID());
    }

    // message hash

    @Test
    public void testMessageHashForTestCase1() {
        Message message = new Message("0012345678", 0, RECIPIENT_1, MESSAGE_1);

        assertEquals("00:0:HITONIGHT", message.getMessageHash());
        assertEquals("00:0:HITONIGHT", message.createMessageHash());
    }

    // checks the remainder of the hashes in a loop
    @Test
    public void testMessageHashesInLoop() {
        String[] ids = {"0012345678", "0198765432", "4567890123", "9900000001"};
        String[] messages = {
            MESSAGE_1,
            MESSAGE_2,
            "Yohoooo, I am at your gate.",
            "It is dinner time !"
        };
        String[] expected = {
            "00:0:HITONIGHT",
            "01:1:HIPAYMENT",
            "45:2:YOHOOOOGATE",
            "99:3:ITTIME"
        };

        for (int i = 0; i < messages.length; i++) {
            Message message = new Message(ids[i], i, RECIPIENT_1, messages[i]);

            assertEquals(expected[i], message.getMessageHash(), "Hash for message " + i);
        }
    }

    // SentMessage choices 

    @Test
    public void testSendMessageSelected() {
        Message message = new Message(0, RECIPIENT_1, MESSAGE_1);

        assertEquals("Message successfully sent.", message.SentMessage("1"));
    }

    @Test
    public void testDisregardMessageSelected() {
        Message message = new Message(1, RECIPIENT_2, MESSAGE_2);

        // test data message 2 has an invalid number, but disregarding must still work
        assertEquals("Press 0 to delete the message.", message.SentMessage("2"));
        assertTrue(message.isDisregarded());
        assertEquals(1, Message.getDisregardedMessages().size());
    }

    @Test
    public void testPressZeroDeletesDisregardedMessage() {
        Message message = new Message(1, RECIPIENT_2, MESSAGE_2);

        message.SentMessage("2");

        assertEquals("Message deleted.", message.SentMessage("0"));
        assertTrue(message.isDeleted());
    }

    @Test
    public void testPressZeroWithoutDisregardingIsInvalid() {
        Message message = new Message(0, RECIPIENT_1, MESSAGE_1);

        assertEquals("Invalid option. Please choose 1, 2, or 3.", message.SentMessage("0"));
        assertFalse(message.isDeleted());
    }

    @Test
    public void testStoreMessageSelected() {
        Message message = new Message(0, RECIPIENT_1, MESSAGE_1);

        assertEquals("Message successfully stored.", message.SentMessage("3"));
    }

    @Test
    public void testStoreMessageWritesJsonFile() throws IOException {
        Message message = new Message("0012345678", 0, RECIPIENT_1, MESSAGE_1);

        message.SentMessage("3");

        String json = new String(Files.readAllBytes(tempDir.resolve("messages.json")), StandardCharsets.UTF_8);

        assertTrue(json.contains("\"messageID\":\"0012345678\""));
        assertTrue(json.contains("\"recipient\":\"" + RECIPIENT_1 + "\""));
        assertTrue(json.contains("\"messageHash\":\"00:0:HITONIGHT\""));
    }

    @Test
    public void testSendWithInvalidRecipientIsBlocked() {
        Message message = new Message(1, RECIPIENT_2, MESSAGE_2);

        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", message.SentMessage("1"));
        assertEquals(0, Message.getTotalMessagesSent());
    }

    @Test
    public void testSendTooLongMessageIsBlocked() {
        Message message = new Message(0, RECIPIENT_1, "a".repeat(260));

        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", message.SentMessage("1"));
        assertEquals(0, Message.getTotalMessagesSent());
    }

    // totals and printing

    // spec test data: message 1 is sent, message 2 is discarded, so the total is 1
    @Test
    public void testReturnTotalMessagesSent() {
        Message first = new Message(0, RECIPIENT_1, MESSAGE_1);
        Message second = new Message(1, RECIPIENT_2, MESSAGE_2);

        first.SentMessage("1");
        second.SentMessage("2");

        assertEquals(1, first.returnTotalMessages());
        assertEquals(1, Message.getTotalMessagesSent());
    }

    @Test
    public void testPrintMessagesListsSentMessagesInOrder() {
        Message message = new Message("0012345678", 0, RECIPIENT_1, MESSAGE_1);

        message.SentMessage("1");

        String expected = "Message ID: 0012345678" + System.lineSeparator() + "Message Hash: 00:0:HITONIGHT" + System.lineSeparator() + "Recipient: " + RECIPIENT_1 + System.lineSeparator() + "Message: " + MESSAGE_1;

        assertEquals(expected, Message.printMessages());
    }

    @Test
    public void testPrintMessagesWhenNothingSent() {
        assertEquals("No messages have been sent yet.", Message.printMessages());
    }
}
 