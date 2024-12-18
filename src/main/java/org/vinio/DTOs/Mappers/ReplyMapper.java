package org.vinio.DTOs.Mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vinio.DTOs.ReplyDTO;
import org.vinio.controllers.GraphQL.inputs.ReplyInputDTO;
import org.vinio.controllers.responseDTO.ReplyResponseDTO;
import org.vinio.entities.ReplyEntity;

/**
 * Класс-мапер для сущности reply
 */
@Component
public class ReplyMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public ReplyMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ReplyDTO convertToDto(ReplyEntity replyEntity) {
        ReplyDTO replyDTO = modelMapper.map(replyEntity, ReplyDTO.class);
        replyDTO.setMessage(replyEntity.getMessage().getMessageId());
        return replyDTO;
    }

    public ReplyDTO convertToDto(ReplyInputDTO replyInputDTO) {
        return modelMapper.map(replyInputDTO, ReplyDTO.class);
    }

    public ReplyEntity convertToEntity(ReplyDTO replyDTO) {
        return modelMapper.map(replyDTO, ReplyEntity.class);
    }

    public ReplyResponseDTO convertToResponse(ReplyDTO replyDTO) {
        return modelMapper.map(replyDTO, ReplyResponseDTO.class);
    }
}
