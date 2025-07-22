/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

/**
 *
 * @author Admin
 */
public class EmailUtils {

    public static void sendVerificationEmail(String toEmail, String fullName, String verifyCode) throws MessagingException {
        final String fromEmail = "duytester13724@gmail.com";
        final String password = "vehh dqgo nqmx ohvg";
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(props,
                new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        message.setSubject("Library Account Verification");

        String content = "Dear " + fullName + ",<br>"
                + "Please verify your account by clicking the link below:<br>"
                + "<h3><a href='http://localhost:8080/Final_Project/VerifyAccountServlet?code=" + verifyCode + "'>VERIFY</a></h3>";

        message.setContent(content, "text/html");
        System.out.println("Sending mail to: " + toEmail);
        Transport.send(message);
        System.out.println("Email sent successfully!");
    }
    public static void sendEmail(String to, String subject, String content) throws MessagingException {
        final String fromEmail = "duytester13724@gmail.com";
        final String password = "vehh dqgo nqmx ohvg";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(content);

        Transport.send(message);
    }
}
