package org.vinio.controllers.RabbitMQ;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.vinio.DTOs.MessageDTO;
import org.vinio.DTOs.UserDTO;
import org.vinio.Services.MessageService;

@Component
public class Listener {
    static final String queueName = "AuditQueue";
    @Autowired
    private MessageService messageService;

    @Bean
    public Queue myQueue() {
        return new Queue(queueName, false);
    }

    @RabbitListener(queues = queueName)
    public void listen(String message) {
        System.out.println("Message read from " + queueName + ":\n" + message);
        ObjectMapper objectMapper = new ObjectMapper();
        MessageDTO dto;
        try {
            dto = objectMapper.readValue(message, MessageDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        MessageDTO messageFromDB = messageService.getMessage(dto.getMessageId());
        System.out.println(messageFromDB);
        messageFromDB.setCategory(dto.getCategory());
        messageFromDB.setPriority(dto.getPriority());
        messageFromDB.setSubject(dto.getSubject());
        System.out.println(messageFromDB);
        messageService.saveMessage(messageFromDB);
    }
}
