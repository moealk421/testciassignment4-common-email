package org.apache.commons.mail;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;

// This EmailTest3 class tests the following: getSentDate, getSocketConnectionTimeout, and setFrom.
public class EmailTest3 {

    private EmailConcrete email;

    @Before
    public void setUp() {
        email = new EmailConcrete();
    }

    // Test when getSentDate is called with no date set.
    @Test
    public void testGetSentDate_DefaultValue() {
        Date beforeCall = new Date();
        Date result = email.getSentDate();
        Date afterCall = new Date();

        assertNotNull(result); // The result should be a valid date.
        assertTrue(result.equals(beforeCall) || result.after(beforeCall)); // Date should be current or after the beforeCall time.
        assertTrue(result.before(afterCall) || result.equals(afterCall)); // Date should be before or equal to afterCall.
    }

    // Test when a custom date is set for getSentDate.
    @Test
    public void testGetSentDate_WithCustomDate() {
        Date customDate = new Date(1672531200000L); 
        email.setSentDate(customDate);

        Date result = email.getSentDate();

        assertNotNull(result); // Result should not be null.
        assertEquals(customDate, result); // It should match the custom date set.
        assertNotSame(customDate, result); // Ensure they are different objects (not the same reference).
    }

    // Test for socket connection timeout with a custom value.
    @Test
    public void testGetSocketConnectionTimeout_CustomValue() {
        email.setSocketConnectionTimeout(5000);

        assertEquals(5000, email.getSocketConnectionTimeout()); // Check that the timeout is correctly set.
    }

    // Test for setting a valid "from" email address.
    @Test
    public void testSetFrom_ValidEmail() throws EmailException {
        email.setFrom("test@example.com");

        assertNotNull(email); // No exception should be thrown.
    }

    // Test for setting an invalid "from" email address (should throw an exception).
    @Test(expected = EmailException.class)
    public void testSetFrom_InvalidEmail() throws EmailException {
        email.setFrom("invalid-email"); // Invalid email, should throw EmailException.
    }
}
