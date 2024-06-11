package com.profitsoft.mail_sender.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Document(indexName = "email_messages")
@Data
public class Message {
    @Id
    private String id;
    private String subject;
    private String content;
    private String recipient;
    private String status;
    private String errorMessage;
    private int retryCount;
    @Field(type = FieldType.Date)
    private Date date;

    public Message(String subject, String content, String recipient) {
        this.subject = subject;
        this.content = content;
        this.recipient = recipient;
        this.date = new Date();
    }
}
