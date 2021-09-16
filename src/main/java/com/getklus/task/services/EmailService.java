package com.getklus.task.services;

import com.getklus.task.models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender sender;


    public void sendEmail(Users user, String msg, String title) throws Exception{
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(user.getEmail());
        helper.setText(msg);
        helper.setSubject(title);

        sender.send(message);
    }
}
