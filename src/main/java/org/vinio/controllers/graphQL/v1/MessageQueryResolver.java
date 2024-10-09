package org.vinio.controllers.graphQL.v1;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.vinio.DTOs.MessageDTO;
import org.vinio.Services.MessageService;
import org.vinio.controllers.responseDTO.MessageResponseDTO;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@Component
public class MessageQueryResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

    @Autowired
    private MessageService messageService;

    // Query to get message by reply ID
    public MessageResponseDTO getMessage(Long id) {
        MessageDTO message = messageService.getMessage(id);
        return new MessageResponseDTO(message, createActions(id), createLinks(id));
    }
    public MessageResponseDTO getMessageByReplyId(Long id) {
        MessageDTO message = messageService.getMessageByReplyId(id);
        return new MessageResponseDTO(message, createActions(id), createLinks(id));
    }
    public MessageResponseDTO getMessagesByUserId(Long id) {
        MessageDTO message = messageService.getMessageByReplyId(id);
        return new MessageResponseDTO(message, createActions(id), createLinks(id));
    }

    // Mutation to create a message
    public MessageResponseDTO createMessage(MessageDTO messageDTO) {
        return new MessageResponseDTO(messageService.saveMessage(messageDTO));
    }

    // Mutation to update a message
    public MessageResponseDTO updateMessage(Long id, MessageDTO messageDTO) {
        MessageDTO message = messageService.updateMessage(id, messageDTO);
        return new MessageResponseDTO(messageService.updateMessage(id, message));
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
