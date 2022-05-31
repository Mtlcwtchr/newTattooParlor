package by.bsuir.tattooparlor.util;


public interface IEmailSender {

    boolean sendNotification(String receiver, String dateFormatted);
    boolean sendVerificationEmail(String receiver, String verificationCode);
    boolean sendPostVerificationEmail(String receiver);

}
