package com.oceanview.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.util.Properties;

public final class MailUtil {
    private static final Properties cfg = new Properties();

    static {
        try (InputStream in = MailUtil.class.getClassLoader().getResourceAsStream("mail.properties")) {
            if (in == null) throw new RuntimeException("mail.properties not found in src/main/resources");
            cfg.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load mail.properties: " + e.getMessage(), e);
        }
    }

    private MailUtil(){}

    public static void send(String to, String subject, String htmlBody) throws Exception {
        if (to == null || to.trim().isEmpty()) throw new IllegalArgumentException("Recipient email required");

        final String username = cfg.getProperty("mail.username");
        final String password = cfg.getProperty("mail.password");
        final String from = cfg.getProperty("mail.from");

        Properties props = new Properties();
        props.put("mail.smtp.host", cfg.getProperty("mail.smtp.host"));
        props.put("mail.smtp.port", cfg.getProperty("mail.smtp.port"));
        props.put("mail.smtp.auth", cfg.getProperty("mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", cfg.getProperty("mail.smtp.starttls.enable"));

        Session session = Session.getInstance(props, new Authenticator() {
            @Override protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(htmlBody, "text/html; charset=UTF-8");

        Transport.send(message);
    }

    // Optional: fire-and-forget (so UI doesn’t feel slow)
    public static void sendAsync(String to, String subject, String htmlBody) {
        new Thread(() -> {
            try { send(to, subject, htmlBody); }
            catch (Exception e) { e.printStackTrace(); }
        }).start();
    }
}