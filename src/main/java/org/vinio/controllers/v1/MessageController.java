package org.vinio.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vinio.DTOs.Mappers.MessageMapper;
import org.vinio.DTOs.MessageDTO;
import org.vinio.ExceptionsHandler.ResourceNotFoundException;
import org.vinio.Services.MessageService;
import org.vinio.controllers.responseDTO.MessageResponseDTO;
import org.vinio.entities.MessageEntity;
import org.vinio.repositories.MessageRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageMapper messageMapper;

    @PostMapping
    public MessageEntity createMessage(@RequestBody MessageEntity message) {
        return messageRepository.save(message);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CollectionModel<EntityModel<MessageDTO>>> getMessagesByUserId(@PathVariable("userId") Long userId) {
        List<MessageEntity> messages = messageRepository.findAll()
                .stream().filter(mes -> mes.getUser().getUserId().equals(userId)).toList();

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

    // TODO Обновление сообщения В СЛУЧАЕ ЕСЛИ СТАТУС СООТВЕТСТВУЕТ
    @PutMapping("/{id}")
    public MessageEntity updateMessage(@PathVariable("id") Long id, @RequestBody MessageEntity updatedMessage) {
        MessageEntity message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));
        updatedMessage.setMessageId(message.getMessageId());
        return messageRepository.save(updatedMessage);
    }

    // TODO Удаление сообщения ИЗМЕНИТЬ НА СТАТУС "ОТОЗВАНО"
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable("id") Long id) {
        messageRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
