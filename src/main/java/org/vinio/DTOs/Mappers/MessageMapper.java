package org.vinio.DTOs.Mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.vinio.DTOs.MessageDTO;
import org.vinio.controllers.graphQL.inputs.MessageInputDTO;
import org.vinio.controllers.responseDTO.MessageResponseDTO;
import org.vinio.entities.MessageEntity;
import org.vinio.repositories.MessageRepository;

import java.util.List;

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
    public MessageDTO convertToDto(MessageInputDTO messageInputDTO) {
        return modelMapper.map(messageInputDTO, MessageDTO.class);
    }

// TODO в dto лежат лишь id на сущность, надо явно её привязать
    public MessageEntity convertToEntity(MessageDTO messageDTO) {
        return modelMapper.map(messageDTO, MessageEntity.class);
    }

    public MessageResponseDTO convertToResponse(MessageDTO messageDTO, List<Link> actions, List<Link> links) {
        MessageResponseDTO messageResponseDTO = modelMapper.map(messageDTO, MessageResponseDTO.class);
        messageResponseDTO.setActions(actions);  // Добавляем действия
        messageResponseDTO.setLinks(links);      // Добавляем ссылки
        return messageResponseDTO;
    }

    public MessageResponseDTO convertToResponse(MessageDTO messageDTO) {
        return modelMapper.map(messageDTO, MessageResponseDTO.class);
    }
}
