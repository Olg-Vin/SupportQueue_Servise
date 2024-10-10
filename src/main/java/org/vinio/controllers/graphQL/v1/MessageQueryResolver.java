package org.vinio.controllers.graphQL.v1;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.vinio.DTOs.Mappers.MessageMapper;
import org.vinio.DTOs.MessageDTO;
import org.vinio.Services.MessageService;
import org.vinio.controllers.responseDTO.MessageQLDto;
import org.vinio.controllers.responseDTO.MessageResponseDTO;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@Component
public class MessageQueryResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageMapper messageMapper;

    // Query to get message by reply ID
    public MessageQLDto getMessage(Long id) {
        MessageDTO message = messageService.getMessage(id);
        return messageMapper.convertToQLDto(message, createActions(id), createLinks(id));
    }
    public MessageQLDto getMessageByReplyId(Long id) {
        MessageDTO message = messageService.getMessageByReplyId(id);
        return messageMapper.convertToQLDto(message, createActions(id), createLinks(id));
    }
    public MessageQLDto getMessagesByUserId(Long id) {
        MessageDTO message = messageService.getMessageByReplyId(id);
        return messageMapper.convertToQLDto(message, createActions(id), createLinks(id));
    }

    // Mutation to create a message
    public MessageQLDto createMessage(MessageDTO messageDTO) {
        return messageMapper.convertToQLDto(messageService.saveMessage(messageDTO));
    }

    // Mutation to update a message
    public MessageQLDto updateMessage(Long id, MessageDTO messageDTO) {
        MessageDTO message = messageService.updateMessage(id, messageDTO);
        return messageMapper.convertToQLDto(messageService.updateMessage(id, message));
    }

    // Mutation to delete a message
    public boolean deleteMessage(Long id) {
        messageService.deleteMessage(id);
        return true;
    }

    // HATEOAS Links
    private List<Link> createLinks(Long id) {
        Link selfLink = linkTo(methodOn(MessageQueryResolver.class).getMessage(id)).withSelfRel();
        Link updateLink = linkTo(methodOn(MessageQueryResolver.class).updateMessage(id, null)).withRel("update");
        Link deleteLink = linkTo(methodOn(MessageQueryResolver.class).deleteMessage(id)).withRel("delete");
        return List.of(selfLink, updateLink, deleteLink);
    }

    // HATEOAS Actions
    private List<Link> createActions(Long id) {
        Link replyLink = linkTo(methodOn(ReplyQueryResolver.class).getReply(id)).withRel("reply");
        return List.of(replyLink);
    }
}
