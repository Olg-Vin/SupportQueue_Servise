package org.vinio.controllers.rest;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vinio.DTOs.ReplyDTO;
import org.vinio.Services.ReplyService;
import org.vinio.controllers.Rest.ReplyApi;
import org.vinio.controllers.responseDTO.ReplyResponseDTO;
import org.vinio.dtos.request.AddReplyRequest;
import org.vinio.dtos.response.ReplyResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@RequestMapping("/replies")
public class ReplyController implements ReplyApi {

    private final ReplyService replyService;
    private final ModelMapper modelMapper;

    @Autowired
    public ReplyController(ReplyService replyService, ModelMapper modelMapper) {
        this.replyService = replyService;
        this.modelMapper = modelMapper;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ReplyResponse> getReplyById(@PathVariable Long id) {
        ReplyDTO replyDTO = replyService.getReply(id);
        return ResponseEntity.ok(createReplyEntityModel(replyDTO));
    }

    @Override
    @GetMapping("/message/{messageId}")
    public ResponseEntity<ReplyResponse> getReplyByMessageId(@PathVariable Long messageId) {
        ReplyDTO replyDTO = replyService.getReplyByMessageId(messageId);
        return ResponseEntity.ok(createReplyEntityModel(replyDTO));
    }

    @Override
    @PostMapping
    public ResponseEntity<ReplyResponse> createReply(@RequestBody @Valid AddReplyRequest addReplyRequest) {
        ReplyDTO replyDTO = replyService.saveReply(modelMapper.map(addReplyRequest, ReplyDTO.class));
        log.info("[endpoint] Created reply with id: {}", replyDTO.getReplyId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createReplyEntityModel(replyDTO));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ReplyResponse> updateReply(@PathVariable Long id, @RequestBody @Valid AddReplyRequest updatedReplyRequest) {
        log.info("[endpoint] Updating reply with id: {}", id);
        ReplyDTO updatedReply = replyService.updateReply(id, modelMapper.map(updatedReplyRequest, ReplyDTO.class));
        return ResponseEntity.ok(createReplyEntityModel(updatedReply));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReply(@PathVariable Long id) {
        log.info("[endpoint] Deleting reply with id: {}", id);
        replyService.deleteReply(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Создает EntityModel для ReplyDTO с гипермедиа ссылками.
     *
     * @param replyDTO DTO ответа.
     * @return EntityModel с добавленными ссылками.
     */
    private ReplyResponse createReplyEntityModel(ReplyDTO replyDTO) {
        Link selfLink = linkTo(methodOn(ReplyController.class).getReplyById(replyDTO.getReplyId())).withSelfRel();
        Link deleteLink = linkTo(methodOn(ReplyController.class).deleteReply(replyDTO.getReplyId())).withRel("delete");

        Link updateLink = linkTo(methodOn(ReplyController.class).updateReply(replyDTO.getReplyId(), null)).withRel("update");
        Link messageLink = linkTo(methodOn(MessageController.class).getMessageByReplyId(replyDTO.getReplyId())).withRel("message");

        ReplyResponseDTO model = modelMapper.map(replyDTO, ReplyResponseDTO.class);
        model.add(selfLink, messageLink);
        model.addActions(deleteLink, updateLink);

        return model;
    }
}
