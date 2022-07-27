package com.babyboy.social.utils;

import com.babyboy.social.domain.EmailForm;
import com.babyboy.social.domain.UserReport;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenders {

    private final Logger log = LoggerFactory.getLogger(EmailSenders.class);

    private final JavaMailSender emailSender;

    public EmailSenders(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(EmailForm emailForm) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            if (emailForm.getContent() != null) helper.setText(emailForm.getContent(), true); // Use this or above line.
            if (emailForm.getSubject() != null) helper.setSubject(emailForm.getSubject());
            if (emailForm.getCc() != null) helper.setCc(emailForm.getCc().split(","));
            if (emailForm.getRecipients() != null) {
                log.info("Send mail to: " + emailForm.getRecipients() + " and cc:" + emailForm.getCc());
                helper.setTo(emailForm.getRecipients());
                emailSender.send(mimeMessage);
            }
        } catch (Exception exception) {
            log.error("Error when send mail to: " + emailForm.getRecipients() + " and cc:" + emailForm.getCc());
        }
    }

    public void sendEmailUserReport(UserReport userReport, String recipient) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            if (userReport.getContent() != null) helper.setText(userReport.getContent(), true); // Use this or above line.

            helper.setSubject("User report post with id: " + userReport.getPostId());
            if (recipient != null) {
                log.info("Send mail to: " + recipient);
                helper.setTo(recipient);
                emailSender.send(mimeMessage);
            }
        } catch (Exception exception) {
            log.error("Error when send mail to: " + recipient);
        }
    }
}
