package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageResponseUnitTest {

    private MessageResponse messageResponse;

    @BeforeEach
    public void setUp() {
        // Arrange: Initialize MessageResponse with a sample message
        messageResponse = new MessageResponse("Initial Message");
    }

    @Test
    public void testMessageResponseConstructor() {
        // Assert: Check if the constructor correctly sets the message
        assertEquals("Initial Message", messageResponse.getMessage());
    }

    @Test
    public void testSetMessage() {
        // Act: Update the message using the setter
        messageResponse.setMessage("Updated Message");

        // Assert: Check if the message was updated correctly
        assertEquals("Updated Message", messageResponse.getMessage());
    }

    @Test
    public void testGetMessage() {
        // Act: Retrieve the message
        String message = messageResponse.getMessage();

        // Assert: Verify that the getter returns the correct message
        assertEquals("Initial Message", message);
    }
}
