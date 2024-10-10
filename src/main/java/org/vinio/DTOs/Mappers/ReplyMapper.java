package org.vinio.DTOs.Mappers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.vinio.DTOs.ReplyDTO;
import org.vinio.controllers.responseDTO.ReplyResponseDTO;
import org.vinio.entities.ReplyEntity;

import java.util.List;

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
    // TODO в dto лежат лишь id на сущность, надо явно её привязать
    public ReplyEntity convertToEntity(ReplyDTO replyDTO) {
        return modelMapper.map(replyDTO, ReplyEntity.class);
    }

    public ReplyResponseDTO convertToResponse(ReplyDTO replyDTO, List<Link> actions, List<Link> links) {
        ReplyResponseDTO replyResponseDTO = modelMapper.map(replyDTO, ReplyResponseDTO.class);
        replyResponseDTO.setActions(actions);  // Добавляем действия
        replyResponseDTO.setLinks(links);      // Добавляем ссылки
        return replyResponseDTO;
    }
    public ReplyResponseDTO convertToResponse(ReplyDTO replyDTO) {
        return modelMapper.map(replyDTO, ReplyResponseDTO.class);
    }
}
