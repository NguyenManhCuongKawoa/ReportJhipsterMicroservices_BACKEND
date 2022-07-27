package com.babyboy.social.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;

@Entity
@Table(name = "email_form")
public class EmailForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    private String recipients;

    private String recipientsname;

    private String cc;

    private String content;

    public EmailForm() {}

    public EmailForm(String subject, String recipients, String recipientsname, String cc, ZonedDateTime datetime, String content) {
        this.subject = subject;
        this.recipients = recipients;
        this.recipientsname = recipientsname;
        this.cc = cc;
        this.content = content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public void setRecipientsname(String recipientsname) {
        this.recipientsname = recipientsname;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getRecipients() {
        return recipients;
    }

    public String getRecipientsname() {
        return recipientsname;
    }

    public String getCc() {
        return cc;
    }

    public String getContent() {
        return content;
    }
}
