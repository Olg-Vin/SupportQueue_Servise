package org.vinio.DTOs.Mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vinio.DTOs.ReplyDTO;
import org.vinio.entities.ReplyEntity;

/**
 * Класс-мапер для сущности reply
 * */
@Component
public class ReplyMapper {
    private final ModelMapper modelMapper;
    @Autowired
    public ReplyMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ReplyDTO convertToDto(ReplyEntity replyEntity) {
        ReplyDTO replyDTO = modelMapper.map(replyEntity, ReplyDTO.class);
        // Явно установить userId, чтобы избежать рекурсивной загрузки UserEntity
        replyDTO.setMessage(replyEntity.getMessage().getMessageId());
        return replyDTO;
    }

    public ReplyEntity convertToEntity(ReplyDTO replyDTO) {
        return modelMapper.map(replyDTO, ReplyEntity.class);
    }
}
