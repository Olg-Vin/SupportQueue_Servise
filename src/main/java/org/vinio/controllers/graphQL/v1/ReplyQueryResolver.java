package org.vinio.controllers.graphQL.v1;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.vinio.DTOs.Mappers.ReplyMapper;
import org.vinio.DTOs.ReplyDTO;
import org.vinio.Services.ReplyService;
import org.vinio.controllers.responseDTO.ReplyResponseDTO;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@Component
public class ReplyQueryResolver {

    @Autowired
    private ReplyService replyService;
    @Autowired
    private ReplyMapper replyMapper;

    // Query to get reply by message ID
    public ReplyResponseDTO getReply(Long id) {
        ReplyDTO reply = replyService.getReplyByMessageId(id);
        return replyMapper.convertToResponse(reply, createActions(id), createLinks(id));
    }

    // Mutation to create a reply
    public ReplyResponseDTO createReply(Long messageId, String body) {
        ReplyDTO reply = new ReplyDTO();
        reply.setMessage(messageId);
        reply.setBody(body);
        return replyMapper.convertToResponse(replyService.saveReply(reply));
    }

    // Mutation to update a reply
    public ReplyResponseDTO updateReply(Long id, String body) {
        ReplyDTO reply = replyService.getReply(id);
        reply.setBody(body);
        return replyMapper.convertToResponse(replyService.updateReply(id, reply));
    }

    // Mutation to delete a reply
    public boolean deleteReply(Long id) {
        replyService.deleteReply(id);
        return true;
    }

    // HATEOAS Links
    private List<Link> createLinks(Long id) {
        Link selfLink = linkTo(methodOn(ReplyQueryResolver.class).getReply(id)).withSelfRel();
        Link updateLink = linkTo(methodOn(ReplyQueryResolver.class).updateReply(id, null)).withRel("update");
        Link deleteLink = linkTo(methodOn(ReplyQueryResolver.class).deleteReply(id)).withRel("delete");
        return List.of(selfLink, updateLink, deleteLink);
    }

    // HATEOAS Actions
    private List<Link> createActions(Long id) {
        Link messageLink = linkTo(methodOn(MessageQueryResolver.class).getMessageByReplyId(id)).withRel("message");
        return List.of(messageLink);
    }
}
