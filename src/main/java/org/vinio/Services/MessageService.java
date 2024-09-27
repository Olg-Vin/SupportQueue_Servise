package org.vinio.Services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vinio.DTOs.MessageDTO;
import org.vinio.entities.MessageEntity;
import org.vinio.repositories.MessageRepository;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private ModelMapper modelMapper;

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
