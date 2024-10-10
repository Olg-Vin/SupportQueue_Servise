package org.vinio.controllers.graphQL.v1;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Controller;
import org.vinio.DTOs.Mappers.MessageMapper;
import org.vinio.DTOs.MessageDTO;
import org.vinio.Services.MessageService;
import org.vinio.controllers.graphQL.inputs.MessageInputDTO;
import org.vinio.controllers.responseDTO.MessageResponseDTO;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@Controller
public class MessageQueryResolver {
    private MessageService messageService;
    private MessageMapper messageMapper;
    @Autowired
    public MessageQueryResolver(MessageService messageService, MessageMapper messageMapper) {
        this.messageService = messageService;
        this.messageMapper = messageMapper;
    }


    @QueryMapping
    public MessageResponseDTO getMessage(@Argument Long id) {
        MessageDTO message = messageService.getMessage(id);
        return messageMapper.convertToResponse(message);
    }



    @MutationMapping
    public MessageResponseDTO createMessage(@Argument("message") MessageInputDTO messageInputDTO) {
        MessageDTO messageDTO = messageMapper.convertToDto(messageInputDTO);
        return messageMapper.convertToResponse(messageService.saveMessage(messageDTO));
    }
    @MutationMapping
    public MessageResponseDTO updateMessage(@Argument Long id, MessageInputDTO messageInputDTO) {
        MessageDTO messageDTO = messageMapper.convertToDto(messageInputDTO);
        MessageDTO message = messageService.updateMessage(id, messageDTO);
        return messageMapper.convertToResponse(messageService.updateMessage(id, message));
    }
    @MutationMapping
    public boolean deleteMessage(@Argument Long id) {
        messageService.deleteMessage(id);
        return true;
    }
}
