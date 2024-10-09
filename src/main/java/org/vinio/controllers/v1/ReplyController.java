package org.vinio.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vinio.DTOs.Mappers.ReplyMapper;
import org.vinio.DTOs.ReplyDTO;
import org.vinio.ExceptionsHandler.ResourceNotFoundException;
import org.vinio.Services.ReplyService;
import org.vinio.controllers.responseDTO.ReplyResponseDTO;
import org.vinio.entities.ReplyEntity;
import org.vinio.repositories.ReplyRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/replies")
public class ReplyController {

    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private ReplyMapper replyMapper;

    @GetMapping("/message/{id}")
    public EntityModel<ReplyResponseDTO> getReplyByMessageId(@PathVariable("id") Long id) {
        ReplyEntity reply = replyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reply not found"));
        Class<ReplyController> controllerClass = ReplyController.class;

        Link selfLink = linkTo(methodOn(controllerClass).getReplyByMessageId(reply.getReplyId())).withSelfRel();
        Link updateLink = linkTo(methodOn(controllerClass).updateReply(reply.getReplyId(), null)).withRel("update");
        Link deleteLink = linkTo(methodOn(controllerClass).deleteReply(reply.getReplyId())).withRel("delete");

        ReplyDTO replyDTO = replyMapper.convertToDto(reply);
//        ReplyResponseDTO replyResponseDTO = new ReplyResponseDTO(replyDTO, List.of(updateLink, deleteLink));

//        return EntityModel.of(replyResponseDTO, selfLink);
        return null;
    }
    private List<Link> createLinks (Long id) {
        Class<UserController> controllerClass = UserController.class;

        Link selfLink = linkTo(methodOn(controllerClass).getUserById(id)).withSelfRel();
        Link updateLink = linkTo(methodOn(controllerClass).updateUser(id, null)).withRel("update");
        Link deleteLink = linkTo(methodOn(controllerClass).deleteUser(id)).withRel("delete");

        return List.of(selfLink, updateLink, deleteLink);
    }
    private List<Link> createActions (Long id) {
        Link messageLink = linkTo(methodOn(MessageController.class).getMessagesByUserId(id)).withRel("messages");
        return List.of(messageLink);
    }

    @PostMapping
    public ReplyEntity createReply(@RequestBody ReplyEntity reply) {
        return replyRepository.save(reply);
    }

    @PutMapping("/{id}")
    public ReplyEntity updateReply(@PathVariable("id") Long id, @RequestBody ReplyEntity updatedReply) {
        ReplyEntity reply = replyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reply not found"));

        updatedReply.setReplyId(reply.getReplyId());
        return replyRepository.save(updatedReply);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReply(@PathVariable("id") Long id) {
        replyRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
