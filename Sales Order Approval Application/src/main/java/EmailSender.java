import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {
	
    public static void sendEmail(String subject, String text,int i) {
        // SMTP server configuration
        String host = "smtp.gmail.com";
        int port = 587;
        String username = "shobhitgopalakrishnan.nmims@gmail.com";
        String password = "hbtuqrdfxidbmqcd";

        // Email properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // Create a Session object with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a default MimeMessage object
            MimeMessage message = new MimeMessage(session);
            // Set From: header field
            message.setFrom(new InternetAddress(username));
            // Set To: header field
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("jh1355349503@gmail.com"));
            // Set Subject: header field
            message.setSubject(subject);
            // Set message body
            message.setText(text + String.valueOf(i));

            // Send message
            Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public static void managerNotification (int i) {
        String subject = "New Pending Orders for Approval";
        String text = "Orders pending: ";
        sendEmail(subject, text, i);
    }
    
    // testing main method
    /*public static void main(String[] args) {
		EmailSender es = new EmailSender();
		es.sendEmail(10);
	}*/
}
