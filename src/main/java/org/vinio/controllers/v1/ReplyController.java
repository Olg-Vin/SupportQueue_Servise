package org.vinio.controllers.v1;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vinio.DTOs.Mappers.ReplyMapper;
import org.vinio.DTOs.ReplyDTO;
import org.vinio.Services.ReplyService;
import org.vinio.controllers.responseDTO.ReplyResponseDTO;
import org.vinio.repositories.ReplyRepository;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@RequestMapping("/replies")
public class ReplyController {
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private ReplyMapper replyMapper;

    @GetMapping("/getReply/{id}")
    public ResponseEntity<EntityModel<ReplyResponseDTO>> getReplyByMessageId(@PathVariable("id") Long id) {
        ReplyDTO reply = replyService.getReplyByMessageId(id);
        ReplyResponseDTO replyResponseDTO = new ReplyResponseDTO(reply, createActions(id), createLinks(id));
        return new ResponseEntity<>(EntityModel.of(replyResponseDTO), HttpStatus.OK);
    }
    private List<Link> createLinks (Long id) {
        Class<ReplyController> controllerClass = ReplyController.class;

        Link selfLink = linkTo(methodOn(controllerClass).getReplyByMessageId(id)).withSelfRel();
        Link updateLink = linkTo(methodOn(controllerClass).updateReply(id, null)).withRel("update");
        Link deleteLink = linkTo(methodOn(controllerClass).deleteReply(id)).withRel("delete");

        return List.of(selfLink, updateLink, deleteLink);
    }

    private List<Link> createActions (Long id) {
        Link messageLink = linkTo(methodOn(MessageController.class).getMessageByReplyId(id)).withRel("message");
        return List.of(messageLink);
    }

    @PostMapping
    public ResponseEntity<ReplyDTO> createReply(@RequestBody ReplyDTO reply) {
        ReplyDTO replyDTO = replyService.saveReply(reply);
        log.info("[endpoint] create new reply with id " + replyDTO.getReplyId());
        return new ResponseEntity<>(replyDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReplyDTO> updateReply(@PathVariable("id") Long id, @RequestBody ReplyDTO updatedReply) {
        log.info("[endpoint] update replay with id " + id);
        ReplyDTO reply = replyService.updateReply(id, updatedReply);
        return new ResponseEntity<>(reply, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReply(@PathVariable("id") Long id) {
        log.info("[endpoint] delete reply with id " + id);
        replyService.deleteReply(id);
        return ResponseEntity.noContent().build();
    }
}
