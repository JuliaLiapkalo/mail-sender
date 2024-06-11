package com.profitsoft.mail_sender.listener;

import com.profitsoft.mail_sender.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageListener {

    private final MessageService messageService;

    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void listen(String message) {
        messageService.sendEmail(message);
    }

}
