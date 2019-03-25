package service.main.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@Configurable
public class EmailServiceImpl {

    public void sendSimpleMessage(String to, String subject, String text) {
        //TODO configure a mail server or use an external one
        Properties properties = new Properties();
        properties.setProperty("spring.mail.host","smtp.gmail.com");
        properties.setProperty("spring.mail.port","587");
        properties.setProperty("spring.mail.properties.mail.smtp.auth","true");
        properties.setProperty("spring.mail.properties.mail.smtp.starttls.enable","true");
        properties.setProperty("spring.mail.username","username");
        properties.setProperty("spring.mail.password","password");
        JavaMailSenderImpl emailSender = new JavaMailSenderImpl();
        emailSender.setHost("");
        emailSender.setPort(25);
        emailSender.setProtocol("smtp");
        /*emailSender.setUsername("");
        emailSender.setPassword("");*/
        emailSender.setJavaMailProperties(properties);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }
}
