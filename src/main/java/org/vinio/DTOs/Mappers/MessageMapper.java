package org.vinio.DTOs.Mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vinio.DTOs.MessageDTO;
import org.vinio.controllers.GraphQL.inputs.MessageInputDTO;
import org.vinio.controllers.responseDTO.MessageResponseDTO;
import org.vinio.dtos.request.AddMessageRequest;
import org.vinio.entities.MessageEntity;

/**
 * Класс-мапер для сущности message
 */
@Component
public class MessageMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public MessageMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public MessageDTO convertToDto(MessageEntity messageEntity) {
        MessageDTO messageDTO = modelMapper.map(messageEntity, MessageDTO.class);
        messageDTO.setUserId(messageEntity.getUser().getUserId());
        return messageDTO;
    }

    public MessageDTO convertToDto(MessageInputDTO messageInputDTO) {
        return modelMapper.map(messageInputDTO, MessageDTO.class);
    }

    public MessageDTO convertToDto(AddMessageRequest addMessageRequest) {
        MessageDTO messageDTO = modelMapper.map(addMessageRequest, MessageDTO.class);
        messageDTO.setMessageId(null);
        return messageDTO;
    }

    public MessageEntity convertToEntity(MessageDTO messageDTO) {
        return modelMapper.map(messageDTO, MessageEntity.class);
    }

    public MessageResponseDTO convertToResponse(MessageDTO messageDTO) {
        return modelMapper.map(messageDTO, MessageResponseDTO.class);
    }
}
