package org.vinio.Services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vinio.DTOs.MessageDTO;
import org.vinio.DTOs.ReplyDTO;
import org.vinio.entities.MessageEntity;
import org.vinio.entities.ReplyEntity;

@Service
public class ReplyService {
    @Autowired
    private ModelMapper modelMapper;

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
