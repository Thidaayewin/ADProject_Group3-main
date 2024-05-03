package com.team3.weather.service;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {
    private JavaMailSender mailSender;

    @Autowired
    EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendHtmlEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        message.setFrom(new InternetAddress("emailjavatestiss@gmail.com"));
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject(subject);

        String htmlContent = "<h1>This is a test Spring Boot email</h1>" +
                "<p>It can contain <strong>HTML</strong> content.</p>";

        message.setContent(htmlContent, "text/html; charset=utf-8");

        mailSender.send(message);
    }

    public void sendAccountCreationEmail(String recipientEmail, String recipientName, String pageURL)
            throws IOException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        // message.setFrom(new InternetAddress("sender@example.com"));
        message.setRecipients(MimeMessage.RecipientType.TO, recipientEmail);
        message.setSubject("Complete Weather Forecasting Account Creation");

        // Read the HTML template into a String variable
        ClassPathResource resource = new ClassPathResource("email-templates/CompleteRegistration.html");
        String htmlTemplate = new String(Files.readAllBytes(resource.getFile().toPath()));

        // Replace placeholders in the HTML template
        htmlTemplate = htmlTemplate.replace("${name}", recipientName);
        htmlTemplate = htmlTemplate.replace("${buttonlink}", pageURL);

        // Set the email's content to be the HTML template
        message.setContent(htmlTemplate, "text/html; charset=utf-8");

        mailSender.send(message);
    }

    public void sendSuccessfulEnrollmentEmail(String recipientEmail, String recipientName, String courseName)
            throws IOException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        // message.setFrom(new InternetAddress("sender@example.com"));
        message.setRecipients(MimeMessage.RecipientType.TO, recipientEmail);
        message.setSubject("You have been enrolled in " + courseName);

        // Read the HTML template into a String variable
        ClassPathResource resource = new ClassPathResource("email-templates/SuccessfulEnrollment.html");
        String htmlTemplate = new String(Files.readAllBytes(resource.getFile().toPath()));

        // Replace placeholders in the HTML template
        htmlTemplate = htmlTemplate.replace("${name}", recipientName);
        htmlTemplate = htmlTemplate.replace("${courseName}", courseName);

        // Set the email's content to be the HTML template
        message.setContent(htmlTemplate, "text/html; charset=utf-8");

        mailSender.send(message);
    }

    public void sendThresholdCrossedEmail(String recipientEmail, String recipientName, String dataDriftDetails, String conceptDriftDetails)
            throws IOException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        // message.setFrom(new InternetAddress("sender@example.com"));
        message.setRecipients(MimeMessage.RecipientType.TO, recipientEmail);
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDateTime = currentDateTime.format(formatter);
        message.setSubject("Drift Threshold Crossed at "+ formattedDateTime);

        // Read the HTML template into a String variable
        ClassPathResource resource = new ClassPathResource("email-templates/ThresholdAlert.html");
        String htmlTemplate = new String(Files.readAllBytes(resource.getFile().toPath()));

        // Replace placeholders in the HTML template
        htmlTemplate = htmlTemplate.replace("${name}", recipientName);
        htmlTemplate = htmlTemplate.replace("${dataDrift}", dataDriftDetails);
        htmlTemplate = htmlTemplate.replace("${conceptDrift}", conceptDriftDetails);

        // Set the email's content to be the HTML template
        message.setContent(htmlTemplate, "text/html; charset=utf-8");

        mailSender.send(message);
    }

}
