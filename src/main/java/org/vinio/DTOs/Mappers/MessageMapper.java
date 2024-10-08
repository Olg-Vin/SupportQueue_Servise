package org.vinio.DTOs.Mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vinio.DTOs.MessageDTO;
import org.vinio.entities.MessageEntity;
import org.vinio.repositories.MessageRepository;

/**
 * Класс-мапер для сущности message
 * */
@Component
public class MessageMapper {
    private final ModelMapper modelMapper;
    @Autowired
    public MessageMapper(ModelMapper modelMapper, MessageRepository messageRepository) {
        this.modelMapper = modelMapper;
    }

    public MessageDTO convertToDto(MessageEntity messageEntity) {
        MessageDTO messageDTO = modelMapper.map(messageEntity, MessageDTO.class);
        // Явно установить userId, чтобы избежать рекурсивной загрузки UserEntity
        messageDTO.setUser(messageEntity.getUser().getUserId());
        return messageDTO;
    }

    public MessageEntity convertToEntity(MessageDTO messageDTO) {
        return modelMapper.map(messageDTO, MessageEntity.class);
    }
}
