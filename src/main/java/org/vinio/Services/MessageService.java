package org.vinio.Services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vinio.DTOs.Mappers.MessageMapper;
import org.vinio.DTOs.MessageDTO;
import org.vinio.entities.MessageEntity;
import org.vinio.repositories.MessageRepository;

import java.util.List;

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

    public MessageDTO saveMessage(MessageDTO messageDTO) {
        log.info("Save new message");
        MessageEntity message = messageRepository.save(messageMapper.convertToEntity(messageDTO));
        return messageMapper.convertToDto(message);
    }

    public MessageDTO getMessage(Long id) {
        log.info("Get message with id: " + id);
        return messageMapper.convertToDto(messageRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Message with id " + id + " not found");
                    return new RuntimeException("Message with id " + id + " not found");
                }));
    }

    public MessageDTO updateMessage(Long id, MessageDTO messageDTO) {
        log.info("[service] Update message with id " + id);
        MessageEntity existingMessage = messageRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Message with id " + id + " not found");
                    return new RuntimeException("Message with id " + id + " not found");
                });
        existingMessage.setBody(messageDTO.getBody());
        existingMessage.setCategory(messageDTO.getCategory());
        existingMessage.setSubject(messageDTO.getSubject());
        existingMessage.setStatus(messageDTO.getStatus());
        MessageEntity updatedMessage = messageRepository.save(existingMessage);
        return messageMapper.convertToDto(updatedMessage);
    }

    public void deleteMessage(Long id) {
        log.info("[service] Delete message with id " + id);
        MessageEntity existingMessage = messageRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Message with id " + id + " not found");
                    return new RuntimeException("Message with id " + id + " not found");
                });
        messageRepository.delete(existingMessage);
        log.info("Message with id " + id + " successfully deleted");
    }

    public MessageDTO getMessageByReplyId(Long replyId) {
        MessageEntity message = messageRepository.findByReply_ReplyId(replyId);
        log.info("[service] get message for reply with id " + replyId);
        return messageMapper.convertToDto(message);
    }
    public List<MessageDTO> getMessageByUserId(Long replyId) {
        List<MessageEntity> messages = messageRepository.findByUser_UserId(replyId);
        log.info("[service] get message for reply with id " + replyId);
        return messages.stream().map(messageMapper::convertToDto).toList();
    }
}
