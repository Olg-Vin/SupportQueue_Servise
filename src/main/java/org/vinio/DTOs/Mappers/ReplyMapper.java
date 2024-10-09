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

/*
* @Component
public class ReplyMapper {

    @Autowired
    private MessageRepository messageRepository;

    // Преобразование ReplyEntity в ReplyDTO
    public ReplyDTO convertToDto(ReplyEntity replyEntity) {
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setReplyId(replyEntity.getReplyId());
        replyDTO.setMessage(replyEntity.getMessage().getMessageId());  // Извлекаем ID сообщения
        replyDTO.setBody(replyEntity.getBody());
        replyDTO.setSentAt(replyEntity.getSentAt());
        replyDTO.setStatus(replyEntity.getStatus());  // Добавляем статус

        return replyDTO;
    }

    // Преобразование ReplyDTO в ReplyEntity
    public ReplyEntity convertToEntity(ReplyDTO replyDTO) {
        ReplyEntity replyEntity = new ReplyEntity();

        // Находим MessageEntity по ID
        MessageEntity messageEntity = messageRepository.findById(replyDTO.getMessage())
                .orElseThrow(() -> new RuntimeException("Message with id " + replyDTO.getMessage() + " not found"));

        replyEntity.setMessage(messageEntity);  // Устанавливаем связь с MessageEntity
        replyEntity.setBody(replyDTO.getBody());
        replyEntity.setSentAt(replyDTO.getSentAt());
        replyEntity.setStatus(replyDTO.getStatus());  // Устанавливаем статус

        return replyEntity;
    }
}
*/
