package org.vinio.controllers.graphQL.v1;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.vinio.DTOs.Mappers.ReplyMapper;
import org.vinio.DTOs.ReplyDTO;
import org.vinio.Services.ReplyService;
import org.vinio.controllers.responseDTO.ReplyResponseDTO;

@Log4j2
@Controller
public class ReplyQueryResolver {
    private ReplyService replyService;
    private ReplyMapper replyMapper;
    @Autowired
    public ReplyQueryResolver(ReplyService replyService, ReplyMapper replyMapper) {
        this.replyService = replyService;
        this.replyMapper = replyMapper;
    }



    @QueryMapping
    public ReplyResponseDTO getReply(@Argument Long id) {
        ReplyDTO reply = replyService.getReplyByMessageId(id);
        return replyMapper.convertToResponse(reply);
    }



    @MutationMapping
    public ReplyResponseDTO createReply(@Argument Long messageId, String body) {
        ReplyDTO reply = new ReplyDTO();
        reply.setMessage(messageId);
        reply.setBody(body);
        return replyMapper.convertToResponse(replyService.saveReply(reply));
    }
    @MutationMapping
    public ReplyResponseDTO updateReply(@Argument Long id, String body) {
        ReplyDTO reply = replyService.getReply(id);
        reply.setBody(body);
        return replyMapper.convertToResponse(replyService.updateReply(id, reply));
    }
    @MutationMapping
    public boolean deleteReply(@Argument Long id) {
        replyService.deleteReply(id);
        return true;
    }
}
