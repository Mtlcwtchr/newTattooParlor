package by.bsuir.tattooparlor.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailSender implements IEmailSender {

    private static final String AUTH = "true";
    private static final String IS_START_TLS_ENABLED = "true";
    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "587";

    private static final String SENDER_ADDRESS = "test.aliaksandra@gmail.com";
    private static final String SENDER_PASSWORD = "fib12358";

    @Autowired
    private EmailSender() { }

    @Override
    public boolean sendNotification(String receiver, String dateFormatted) {
        return sendEmail(receiver, String.format("Try not to forget about your tomorrow's session at %s", dateFormatted));
    }

    @Override
    public boolean sendVerificationEmail(String receiver, int verificationCode){
        return sendEmail(receiver, String.format("Your verification code is %s", verificationCode));
    }

    @Override
    public boolean sendPostVerificationEmail(String receiver){
        return sendEmail(receiver, "Your account has been verified");
    }

    private boolean sendEmail(String receiver, String text) {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.auth", AUTH);
        properties.put("mail.smtp.starttls.enable", IS_START_TLS_ENABLED);
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", PORT);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_ADDRESS, SENDER_PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_ADDRESS));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setSubject("Welcome to C-dur!");
            message.setText(text);
            Transport.send(message);

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
