package org.vinio.controllers.v1;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vinio.DTOs.Mappers.MessageMapper;
import org.vinio.DTOs.MessageDTO;
import org.vinio.Services.MessageService;
import org.vinio.entities.MessageEntity;
import org.vinio.repositories.MessageRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageMapper messageMapper;

    //    Возвращаем entity model с ссылочками
    @GetMapping("/getMessage/{id}")
    public ResponseEntity<EntityModel<MessageDTO>> getMessageByReplyId (@PathVariable("id") Long id) {
        MessageDTO messageDTO = messageService.getMessageByReplyId(id);
        Link selfLink = linkTo(methodOn(MessageController.class).getMessagesByUserId(messageDTO.getMessageId())).withSelfRel();
        Link deleteLink = linkTo(methodOn(MessageController.class).deleteMessage(messageDTO.getMessageId())).withRel("delete");
        Link updateLink = linkTo(methodOn(MessageController.class).updateMessage(messageDTO.getMessageId(), null)).withRel("update");
        Link replay = linkTo(methodOn(ReplyController.class).getReplyByMessageId(messageDTO.getMessageId())).withRel("replay");
        EntityModel<MessageDTO> entityModel = EntityModel.of(messageDTO, selfLink, deleteLink, updateLink, replay);
        return new ResponseEntity<>(entityModel, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CollectionModel<EntityModel<MessageDTO>>> getMessagesByUserId(@PathVariable("userId") Long userId) {
        List<MessageEntity> messages = messageRepository.findAll()
                .stream().filter(mes -> mes.getUser().getUserId().equals(userId)).toList();
// TODO переехать на messageResponseDTO
        List<EntityModel<MessageDTO>> messageDtos = messages.stream()
                .map(messageEntity -> {
                    MessageDTO messageDto = messageMapper.convertToDto(messageEntity);
                    Link selfLink = linkTo(methodOn(MessageController.class).getMessagesByUserId(messageDto.getMessageId())).withSelfRel();
                    Link deleteLink = linkTo(methodOn(MessageController.class).deleteMessage(messageDto.getMessageId())).withRel("delete");
                    Link updateLink = linkTo(methodOn(MessageController.class).updateMessage(messageDto.getMessageId(), null)).withRel("update");
                    Link replay = linkTo(methodOn(ReplyController.class).getReplyByMessageId(messageDto.getMessageId())).withRel("replay");
                    return EntityModel.of(messageDto, selfLink, deleteLink, updateLink, replay);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(messageDtos));
    }

    @PostMapping("/create")
    public ResponseEntity<MessageDTO> createMessage(@RequestBody MessageDTO messageDTO) {
        MessageDTO message = messageService.saveMessage(messageDTO);
        log.info("[endpoint] save new message with id " + message.getMessageId());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // TODO Обновление сообщения В СЛУЧАЕ ЕСЛИ СТАТУС СООТВЕТСТВУЕТ "ОЖИДАЕТ"
    @PutMapping("/update/{id}")
    public ResponseEntity<MessageDTO> updateMessage(@PathVariable("id") Long id, @RequestBody MessageDTO updatedMessage) {
        log.info("[endpoint] update message with id " + id);
        MessageDTO messageDTO = messageService.updateMessage(id, updatedMessage);
        return new ResponseEntity<>(messageDTO, HttpStatus.OK);
    }

    // TODO Удаление сообщения ИЗМЕНИТЬ НА СТАТУС "ОТОЗВАНО"
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable("id") Long id) {
        log.info("[endpoint] delete message with id " + id);
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
