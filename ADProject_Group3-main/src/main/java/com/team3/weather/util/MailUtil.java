package com.team3.weather.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MailUtil{

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mainUserName;


    /**
     * send email
     */
    public void sendSimpleMail(String[] emails, String messageInfo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Alert reminder");
        message.setFrom('<'+mainUserName+'>');
        message.setTo(emails);
        message.setSentDate(new Date());
        message.setText(messageInfo);
        javaMailSender.send(message);
        System.out.println("send success.");
    }

}
