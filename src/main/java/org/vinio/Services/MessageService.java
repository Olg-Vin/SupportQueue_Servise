package org.vinio.Services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vinio.DTOs.Mappers.MessageMapper;
import org.vinio.DTOs.MessageDTO;
import org.vinio.repositories.MessageRepository;

@Service
@Log4j2
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Autowired
    public MessageService(MessageRepository messageRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    public void saveMessage(MessageDTO messageDTO) {
        log.info("Save new message");
        messageRepository.save(messageMapper.convertToEntity(messageDTO));
    }

    public MessageDTO getMessage(Long id) {
        log.info("Get message with id: " + id);
        return messageMapper.convertToDto(messageRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Message with id " + id + " not found");
                    return new RuntimeException("Message with id " + id + " not found");
                }));
    }

//    TODO изменение некоторых полей сообщения. Удалять и обновлять целиком не требуется
}
