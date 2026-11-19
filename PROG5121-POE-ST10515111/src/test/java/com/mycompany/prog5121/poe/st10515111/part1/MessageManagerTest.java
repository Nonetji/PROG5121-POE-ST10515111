package com.mycompany.prog5121.poe.st10515111.part1;

import part2.Message;
import part3.MessageManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MessageManagerTest {

    // spec test data (message 4 uses the developer number as its ID and recipient)
    private static final String RECIPIENT_1 = "+27834557896";
    private static final String TEXT_1 = "Did you get the cake?";

    private static final String RECIPIENT_2 = "+27838884567";
    private static final String TEXT_2 = "Where are you? You are late! I have asked you to be on time.";

    private static final String RECIPIENT_3 = "+27834484567";
    private static final String TEXT_3 = "Yohoooo, I am at your gate.";

    private static final String ID_4 = "0838884567";
    private static final String TEXT_4 = "It is dinner time !";

    private static final String RECIPIENT_5 = "+27838884567";
    private static final String TEXT_5 = "Ok, I am leaving without you.";

    @TempDir
    Path tempDir;

    private MessageManager manager;
    private Message message2;

    // every test starts clean, writes JSON to a temp folder and fills the arrays from the spec test data
    @BeforeEach
    public void setUp() {
        Message.resetState();
        Message.setStorageFile(tempDir.resolve("messages.json"));

        manager = new MessageManager("Developer");

        // message 1: sent
        Message message1 = new Message(0, RECIPIENT_1, TEXT_1);
        message1.SentMessage("1");
        manager.addSent(message1);

        // message 2 and 5: stored (written to the JSON file, then read back into the array)
        message2 = new Message(1, RECIPIENT_2, TEXT_2);
        message2.SentMessage("3");

        // message 3: disregarded
        Message message3 = new Message(2, RECIPIENT_3, TEXT_3);
        message3.SentMessage("2");
        manager.addDisregarded(message3);

        // message 4: sent (its developer number is not a +27 number, so it goes straight into the array)
        Message message4 = new Message(ID_4, 3, ID_4, TEXT_4);
        manager.addSent(message4);

        Message message5 = new Message(4, RECIPIENT_5, TEXT_5);
        message5.SentMessage("3");

        manager.loadStoredMessages();
    }

    // sent array

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        assertEquals(List.of(TEXT_1, TEXT_4), manager.getSentMessageTexts());
    }

    // the other arrays

    @Test
    public void testDisregardedMessagesArrayPopulated() {
        assertEquals(1, manager.getDisregardedMessages().size());
        assertEquals(TEXT_3, manager.getDisregardedMessages().get(0).getMessageText());
    }

    @Test
    public void testStoredMessagesArrayReadFromJsonFile() {
        assertEquals(2, manager.getStoredMessages().size());
        assertEquals(TEXT_2, manager.getStoredMessages().get(0).getMessageText());
        assertEquals(RECIPIENT_2, manager.getStoredMessages().get(0).getRecipient());
        assertEquals(TEXT_5, manager.getStoredMessages().get(1).getMessageText());
    }

    @Test
    public void testLoadingTwiceDoesNotDuplicateStoredMessages() {
        manager.loadStoredMessages();

        assertEquals(2, manager.getStoredMessages().size());
    }

    @Test
    public void testMessageHashAndIDArraysPopulated() {
        assertEquals(5, manager.getMessageHashes().size());
        assertEquals(5, manager.getMessageIDs().size());
        assertTrue(manager.getMessageHashes().contains(message2.getMessageHash()));
        assertTrue(manager.getMessageIDs().contains(ID_4));
    }

    // menu options

    @Test
    public void testDisplaySenderAndRecipientOfStoredMessages() {
        String expected = "Sender: Developer | Recipient: " + RECIPIENT_2 + System.lineSeparator()
            + "Sender: Developer | Recipient: " + RECIPIENT_5;

        assertEquals(expected, manager.displaySenderAndRecipient());
    }

    @Test
    public void testDisplayTheLongestMessage() {
        assertEquals(TEXT_2, manager.longestStoredMessage());
    }

    @Test
    public void testLongestMessageIsNullWhenNothingStored() {
        assertNull(new MessageManager("Developer").longestStoredMessage());
    }

    @Test
    public void testSearchForMessageID() {
        assertEquals(TEXT_4, manager.findByMessageID(ID_4).getMessageText());
        assertEquals("Recipient: " + ID_4 + System.lineSeparator() + "Message: " + TEXT_4, manager.searchByMessageID(ID_4));
    }

    @Test
    public void testSearchForUnknownMessageID() {
        assertNull(manager.findByMessageID("9999999999"));
        assertEquals("No message found with ID 9999999999.", manager.searchByMessageID("9999999999"));
    }

    @Test
    public void testSearchAllMessagesForARecipient() {
        assertEquals(List.of(TEXT_2, TEXT_5), manager.messagesForRecipient("+27838884567"));
    }

    @Test
    public void testSearchForRecipientWithNoMessages() {
        assertTrue(manager.messagesForRecipient("+27000000000").isEmpty());
    }

    // delete

    @Test
    public void testDeleteMessageUsingMessageHash() throws IOException {
        String result = manager.deleteByHash(message2.getMessageHash());

        assertEquals("Message: \"" + TEXT_2 + "\" successfully deleted.", result);
        assertEquals(1, manager.getStoredMessages().size());
        assertFalse(manager.getMessageHashes().contains(message2.getMessageHash()));

        // the JSON file no longer holds the deleted message but still holds message 5
        String json = new String(Files.readAllBytes(tempDir.resolve("messages.json")), StandardCharsets.UTF_8);

        assertFalse(json.contains("Where are you?"));
        assertTrue(json.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeletedMessageStaysDeletedAfterReloadingTheFile() {
        manager.deleteByHash(message2.getMessageHash());
        manager.loadStoredMessages();

        assertEquals(1, manager.getStoredMessages().size());
        assertEquals(TEXT_5, manager.getStoredMessages().get(0).getMessageText());
    }

    @Test
    public void testDeleteWithUnknownHash() {
        assertEquals("No stored message found with hash 99:9:NOPE.", manager.deleteByHash("99:9:NOPE"));
        assertEquals(2, manager.getStoredMessages().size());
    }

    // reports

    @Test
    public void testDisplayReportOfSentMessages() {
        String report = manager.reportSentMessages();

        for (Message message : manager.getSentMessages()) {
            assertTrue(report.contains("Message Hash: " + message.getMessageHash()));
            assertTrue(report.contains("Recipient: " + message.getRecipient()));
            assertTrue(report.contains("Message: " + message.getMessageText()));
        }

        assertFalse(report.contains(TEXT_2));
    }

    @Test
    public void testDisplayReportOfStoredMessages() {
        String report = manager.reportStoredMessages();

        assertTrue(report.contains("Message Hash: " + message2.getMessageHash()));
        assertTrue(report.contains("Recipient: " + RECIPIENT_2));
        assertTrue(report.contains("Message: " + TEXT_2));
        assertTrue(report.contains("Message: " + TEXT_5));
    }

    @Test
    public void testReportsWhenNothingIsHeld() {
        MessageManager empty = new MessageManager("Developer");

        assertEquals("No messages have been sent yet.", empty.reportSentMessages());
        assertEquals("There are no stored messages.", empty.reportStoredMessages());
    }
}