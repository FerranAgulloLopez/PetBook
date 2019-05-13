package service.main.util;

import org.json.JSONObject;
import service.main.exception.InternalServerErrorException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

public class SendEmailTLS {

    private static String pathConfigFile = "../config_files/mail_info.json";
    private static String username;
    private static String name;
    private static String password;

    private static final Properties prop = new Properties();

    public SendEmailTLS() throws InternalServerErrorException {
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        loadMailInfo();
    }

    public void sendEmail(String mail, String subject, String content) throws InternalServerErrorException {

        try {
            Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(name));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(mail)
            );
            message.setSubject(subject);
            message.setContent(content, "text/html; charset=utf-8");
            Transport.send(message);

        } catch (Exception e) {
            throw new InternalServerErrorException("Error while sending a new email");
        }
    }

    private void loadMailInfo() throws InternalServerErrorException {
        try(FileReader fileReader = new FileReader(pathConfigFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            String data = "";

            while((line = bufferedReader.readLine()) != null) {
                data = data.concat(line);
            }

            JSONObject jsonObject = new JSONObject(data);
            username = jsonObject.getString("username");
            name = jsonObject.getString("name");
            password = jsonObject.getString("password");

            if (username == null || password == null || name == null) throw new InternalServerErrorException("");

        } catch (Exception e) {
            throw new InternalServerErrorException("Error while loading email configuration file");
        }

    }

}