package com.mycompany.prog5121.poe.st10515111.part1;

import part2.Message;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    // confirms that a correctly formatted cellphone number is accepted
    @Test
    public void testValidRecipientCell() {

        Message message = new Message(1,"+27821234567","Hello QuickChat");

        assertEquals("Cell phone number successfully captured.",message.checkRecipientCell());
    }

    // confirms that an incorrectly formatted cellphone number is rejected
    @Test
    public void testInvalidRecipientCell() {

        Message message = new Message(1,"0821234567","Hello QuickChat");

        assertNotEquals("Cell phone number successfully captured.",message.checkRecipientCell());
    }

    // confirms that the generated message ID has ten characters
    @Test
    public void testMessageIDLength() {

        Message message = new Message(1,"+27821234567","Hello QuickChat");

        assertEquals(10, message.getMessageID().length());
        assertTrue(message.checkMessageID());
    }

    // confirms that a valid message passes length validation
    @Test
    public void testValidMessageLength() {

        Message message = new Message(1,"+27821234567","Hello QuickChat");

        assertEquals("Message ready to send.",message.validateMessage());
    }
}