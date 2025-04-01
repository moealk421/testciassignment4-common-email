package org.apache.commons.mail;

import org.junit.Before;
import org.junit.Test;
import javax.mail.Session;
import java.util.Date;
import static org.junit.Assert.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.Address;


// This EmailTest2 class tests the following: buildMimeMessage, getHostName and getMailSession.
public class EmailTest2 {

    private EmailConcrete email; // Email instance for testing.

    // Set up method runs before each test to create a new instance of EmailConcrete.
    @Before
    public void setUp() {
        email = new EmailConcrete();
    }

    // Test case to check if an exception is thrown when buildMimeMessage is called twice.
    @Test(expected = IllegalStateException.class)
    public void testBuildMimeMessage_TwiceThrowsException() throws EmailException {
        email.setHostName("smtp.example.com");
        email.setFrom("test@example.com");
        email.addTo("recipient@example.com");

        email.buildMimeMessage();

        email.buildMimeMessage();
    }

    // Test case to check if buildMimeMessage throws an exception if there's no "From" address.
    @Test(expected = EmailException.class)
    public void testBuildMimeMessage_NoFromAddress() throws EmailException {
        email.setHostName("smtp.example.com");
        email.addTo("recipient@example.com");

        email.buildMimeMessage();
    }

    // Test case to check if buildMimeMessage throws an exception when there are no recipients.
    @Test(expected = EmailException.class)
    public void testBuildMimeMessage_NoRecipients() throws EmailException {
        email.setHostName("smtp.example.com");
        email.setFrom("test@example.com");

        // This should throw an exception since no recipients are set.
        email.buildMimeMessage();
    }

    // Test case for building a valid email with all the required parameters.
    @Test
    public void testBuildMimeMessage_ValidEmail() throws EmailException {
        email.setHostName("smtp.example.com");
        email.setFrom("test@example.com");
        email.addTo("recipient@example.com");
        email.setSubject("Test Subject");
        email.setMsg("This is a test email.");

        email.buildMimeMessage();

        assertNotNull(email); // Ensure email object isn't null after the MimeMessage is built.
    }

    // Test case for checking buildMimeMessage with CC and BCC.
    @Test
    public void testBuildMimeMessage_ValidWithCCAndBCC() throws EmailException {
        email.setHostName("smtp.example.com");
        email.setFrom("test@example.com");
        email.addTo("recipient@example.com");
        email.addCc("cc@example.com");
        email.addBcc("bcc@example.com");
        email.setSubject("Test Subject");
        email.setMsg("This is a test email.");

        email.buildMimeMessage();

        assertNotNull(email); // Ensure it completes without issues.
    }

    // Test case for checking the subject with a charset.
    @Test
    public void testBuildMimeMessage_SubjectWithCharset() throws EmailException {
        email.setHostName("smtp.example.com");
        email.setFrom("test@example.com");
        email.addTo("recipient@example.com");
        email.setSubject("Test Subject");
        email.setCharset("UTF-8");
        email.setMsg("This is a test email.");

        // Build the MimeMessage.
        email.buildMimeMessage();

        assertNotNull(email); // Ensure email is built successfully.
    }

    // Test case for empty "To" list.
    @Test(expected = EmailException.class)
    public void testBuildMimeMessage_EmptyToList() throws EmailException {
        email.setHostName("smtp.example.com");
        email.setFrom("test@example.com");
       
        email.buildMimeMessage();
    }

    // Test case for buildMimeMessage when no subject is provided.
    @Test
    public void testBuildMimeMessage_NoSubject() throws EmailException {
        email.setHostName("smtp.example.com");
        email.setFrom("test@example.com");
        email.addTo("recipient@example.com");
        email.setMsg("Test email body");

        // No subject set
        email.buildMimeMessage();

        assertNotNull(email); // MimeMessage should be built successfully.
    }

    // Test case for buildMimeMessage with both text and HTML content.
    @Test
    public void testBuildMimeMessage_TextAndHtmlContent() throws EmailException {
        email.setHostName("smtp.example.com");
        email.setFrom("test@example.com");
        email.addTo("recipient@example.com");
        email.setSubject("Test Subject");
        email.setMsg("This is a test email in text format.");
        email.setContent("<html><body><p>This is a test email in HTML format.</p></body></html>", "text/html");

        email.buildMimeMessage();

        assertNotNull(email); // MimeMessage should be built successfully with both text and HTML content.
    }

    // Test case for buildMimeMessage with a custom Date object.
    @Test
    public void testBuildMimeMessage_CustomDate() throws EmailException {
        email.setHostName("smtp.example.com");
        email.setFrom("test@example.com");
        email.addTo("recipient@example.com");

        Date customDate = new Date(1633036800000L); // Example timestamp
        email.setSentDate(customDate);

        email.buildMimeMessage();

        assertEquals(customDate, email.getSentDate());  // Ensure the custom sent date is used in the message.
    }

    // Test case for buildMimeMessage with a missing host name and missing "From" address.
    @Test(expected = EmailException.class)
    public void testBuildMimeMessage_MissingFromAndHostName() throws EmailException {
        email.addTo("recipient@example.com");

        // Missing both "From" and "Host Name"
        email.buildMimeMessage();  // Should throw EmailException
    }


    @Test
    public void testBuildMimeMessage_ReplyTo() throws EmailException, MessagingException {
        email.setHostName("smtp.example.com");
        email.setFrom("test@example.com");
        email.addTo("recipient@example.com");
        email.addReplyTo("reply@example.com");

        email.buildMimeMessage();
        MimeMessage mimeMessage = email.getMimeMessage();
        
        // Verify that the Reply-To is set correctly
        Address[] replyTo = mimeMessage.getReplyTo();
        assertEquals(1, replyTo.length);
        assertEquals("reply@example.com", ((InternetAddress) replyTo[0]).getAddress());
    }


    // Testing the getHostName() method to retrieve the hostname from session.
    @Test
    public void testGetHostName_WithSession() {
        email.setHostName("default.example.com");
        Session session = Session.getInstance(System.getProperties());
        session.getProperties().setProperty(Email.MAIL_HOST, "session.example.com");

        // Set the session and check if the session's host is being used.
        email.setMailSession(session);
        assertEquals("session.example.com", email.getHostName());
    }
    
    @Test
    public void testBuildMimeMessage_AddHeaders() throws EmailException, MessagingException {
        email.setHostName("smtp.example.com");
        email.setFrom("test@example.com");
        email.addTo("recipient@example.com");

        // Set headers for the email
        email.addHeader("X-Custom-Header", "HeaderValue1");
        email.addHeader("X-Another-Header", "HeaderValue2");

        // Build the MimeMessage
        email.buildMimeMessage();

        MimeMessage mimeMessage = email.getMimeMessage();
        
        // Verify that the headers are added correctly
        assertNotNull(mimeMessage.getHeader("X-Custom-Header"));
        assertEquals("HeaderValue1", mimeMessage.getHeader("X-Custom-Header")[0]);
        
        assertNotNull(mimeMessage.getHeader("X-Another-Header"));
        assertEquals("HeaderValue2", mimeMessage.getHeader("X-Another-Header")[0]);
    }


    // Test case for retrieving the host name directly when session is set.
    @Test
    public void testGetHostName_WithHostNameSet() {
        email.setHostName("configured.example.com");

        // Ensure the configured host name is returned.
        assertEquals("configured.example.com", email.getHostName());
    }

    // Test case for when no session or host name is set, should return null.
    @Test
    public void testGetHostName_NoSessionOrHostName() {
        assertNull(email.getHostName()); // No session or host name set.
    }

    // Testing getMailSession() to retrieve the session.
    @Test
    public void testGetMailSession_DefaultSession() throws EmailException {
        Session session = email.getMailSession();
        assertNotNull(session); // Ensure a session is returned.
    }

    // Test case to retrieve the session when a host name is set.
    @Test
    public void testGetMailSession_WithHostName() throws EmailException {
        email.setHostName("smtp.example.com");
        Session session = email.getMailSession();

        // Ensure the session is correct.
        assertNotNull(session);
        assertEquals("smtp.example.com", session.getProperty(Email.MAIL_HOST));
    }

    // Test case to ensure getMailSession throws an exception when no host name is set.
    @Test(expected = EmailException.class)
    public void testGetMailSession_NoHostNameThrowsException() throws EmailException {
        email.getMailSession(); // Should throw an exception since no host is set.
    }

    // Test case to check if SSL is configured correctly.
    @Test
    public void testGetMailSession_WithSSL() throws EmailException {
        email.setHostName("smtp.example.com");
        email.setSSLOnConnect(true);

        // Check if SSL is configured in the session.
        Session session = email.getMailSession();
        assertNotNull(session);
        assertEquals("javax.net.ssl.SSLSocketFactory", session.getProperty(Email.MAIL_SMTP_SOCKET_FACTORY_CLASS));
    }
}