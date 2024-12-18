package org.vinio.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vinio.DTOs.Mappers.MessageMapper;
import org.vinio.DTOs.MessageDTO;
import org.vinio.Services.MessageService;
import org.vinio.controllers.RabbitMQ.Sender;
import org.vinio.controllers.Rest.MessageApi;
import org.vinio.controllers.responseDTO.MessageResponseDTO;
import org.vinio.dtos.request.AddMessageRequest;
import org.vinio.dtos.response.MessageResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Контроллер для управления сущностью сообщения (Message).
 * Предоставляет эндпоинты для выполнения CRUD-операций с поддержкой HAL.
 *
 * @see MessageService
 * @see MessageMapper
 */
@Log4j2
@RestController
@RequestMapping("/messages")
public class MessageController implements MessageApi {

    private final MessageService messageService;
    private final ModelMapper modelMapper;
    private final MessageMapper messageMapper;
    private final Sender sender;

    @Value("${gRPCQueueName}")
    private String gRPCQueueName;

    @Autowired
    public MessageController(MessageService messageService, ModelMapper modelMapper, MessageMapper messageMapper, Sender sender) {
        this.messageService = messageService;
        this.modelMapper = modelMapper;
        this.messageMapper = messageMapper;
        this.sender = sender;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getMessageById(@PathVariable("id") Long id) {
        MessageDTO messageDTO = messageService.getMessageByReplyId(id);
        return ResponseEntity.ok(createMessageEntityModel(messageDTO));
    }

    @Override
    @GetMapping("/user/{userId}")
    public ResponseEntity<CollectionModel<MessageResponse>> getMessagesByUserId(@PathVariable("userId") Long userId) {
        List<MessageDTO> messages = messageService.getMessageByUserId(userId);
        List<MessageResponse> messageModels = messages.stream()
                .map(this::createMessageEntityModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(messageModels)
                        .add(linkTo(methodOn(MessageController.class)
                                .getMessagesByUserId(userId))
                                .withSelfRel())
        );
    }

    @Override
    @GetMapping("/reply/{replyId}")
    public ResponseEntity<MessageResponse> getMessageByReplyId(@PathVariable("replyId") Long replyId) {
        MessageDTO messageDTO = messageService.getMessageByReplyId(replyId);
        return ResponseEntity.ok(createMessageEntityModel(messageDTO));
    }

    @Override
    @PostMapping
    public ResponseEntity<MessageResponse> createMessage(@RequestBody AddMessageRequest message) {
        System.out.println();
        System.out.println("1/ add new message: " + message);
        MessageDTO messageDTO = messageMapper.convertToDto(message);
        System.out.println("\n2/ map new message to dto: " + messageDTO);

        MessageDTO createdMessage = messageService.saveMessage(messageDTO);
        log.info("[endpoint] Created message with id: {}", createdMessage.getMessageId());
        sender.sendMessage(gRPCQueueName, toJsonString(createdMessage));
        return ResponseEntity.status(HttpStatus.CREATED).body(createMessageEntityModel(createdMessage));
    }

    private String toJsonString(MessageDTO message){
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonString;
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateMessage(@PathVariable("id") Long id, @RequestBody AddMessageRequest updatedMessage) {
        log.info("[endpoint] Updating message with id: {}", id);
        MessageDTO messageDTO = messageService.updateMessage(id, modelMapper.map(updatedMessage, MessageDTO.class));
        return ResponseEntity.ok(createMessageEntityModel(messageDTO));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable("id") Long id) {
        log.info("[endpoint] Deleting message with id: {}", id);
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Создает EntityModel для MessageDTO с гипермедиа ссылками.
     *
     * @param messageDTO DTO сообщения.
     * @return EntityModel с добавленными ссылками.
     */
    private MessageResponseDTO createMessageEntityModel(MessageDTO messageDTO) {
        Link selfLink = linkTo(methodOn(MessageController.class).getMessageById(messageDTO.getMessageId())).withSelfRel();
        Link deleteLink = linkTo(methodOn(MessageController.class).deleteMessage(messageDTO.getMessageId())).withRel("delete");

        Link updateLink = linkTo(methodOn(MessageController.class).updateMessage(messageDTO.getMessageId(), null)).withRel("update");
        Link replayLink = linkTo(methodOn(ReplyController.class).getReplyByMessageId(messageDTO.getMessageId())).withRel("replay");

        MessageResponseDTO model = modelMapper.map(messageDTO, MessageResponseDTO.class);
        model.add(selfLink, replayLink);
        model.addActions(deleteLink, updateLink);

        return model;
    }
}
