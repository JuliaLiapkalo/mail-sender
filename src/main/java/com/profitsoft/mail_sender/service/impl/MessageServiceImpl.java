package com.profitsoft.mail_sender.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.profitsoft.mail_sender.config.EmailConfig;
import com.profitsoft.mail_sender.entity.Message;
import com.profitsoft.mail_sender.enums.Status;
import com.profitsoft.mail_sender.repo.MessageRepository;
import com.profitsoft.mail_sender.service.MessageService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final EmailConfig emailConfig;

    @Value("${java-mail.email-from-send}")
    String emailFromSend;

    public void sendEmail(String message) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String subject = jsonNode.get("subject").asText();
            String content = String.valueOf(jsonNode.get("content"));
            String email = jsonNode.get("email").asText();
            Message emailMessage = new Message(subject, content, email);

            sendMessage(emailMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(Message message) {
        try {
            var mimeMessage = emailConfig.javaMailSender().createMimeMessage();
            var helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(message.getContent(), true);
            helper.setTo(message.getRecipient());
            helper.setSubject(message.getSubject());
            helper.setFrom(emailFromSend);
            emailConfig.javaMailSender().send(mimeMessage);
            message.setStatus(Status.SUCCESS.name());
        } catch (Exception e) {
            message.setStatus(Status.FAILURE.name());
            message.setErrorMessage(e.getClass().getName() + ": " + e.getMessage());
        }
        messageRepository.save(message);
    }

    @Scheduled(fixedRate = 300000)
    public void retryFailedEmails() {
        List<Message> failedEmails = messageRepository.findByStatus("FAILED");
        for (Message emailMessage : failedEmails) {
            emailMessage.setRetryCount(emailMessage.getRetryCount() + 1);
            sendMessage(emailMessage);
        }
    }
}

