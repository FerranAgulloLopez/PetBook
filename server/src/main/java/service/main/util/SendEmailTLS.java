package service.main.util;

import org.json.JSONObject;
import service.main.exception.InternalErrorException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class SendEmailTLS {

    private static String pathConfigFile = "../config_files/mail_info.json";
    private static String username;
    private static String password;

    private static final Properties prop = new Properties();

    public SendEmailTLS() throws InternalErrorException {
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        loadMailInfo();
    }

    public void sendEmail(String mail, String subject, String content) throws InternalErrorException {

        try {
            Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(mail)
            );
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);

        } catch (Exception e) {
            throw new InternalErrorException("Error while sending a new email");
        }
    }

    private void loadMailInfo() throws InternalErrorException {
        try(FileReader fileReader = new FileReader(pathConfigFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            String data = "";

            while((line = bufferedReader.readLine()) != null) {
                data = data.concat(line);
            }

            JSONObject jsonObject = new JSONObject(data);
            username = jsonObject.getString("username");
            password = jsonObject.getString("password");

            if (username == null || password == null) throw new InternalErrorException("");

        } catch (Exception e) {
            throw new InternalErrorException("Error while loading email configuration file");
        }

    }

}