package org.vinio.controllers.graphQL.v1;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.graphql.data.method.annotation.Argument;
//import org.springframework.graphql.data.method.annotation.MutationMapping;
//import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.vinio.DTOs.Mappers.ReplyMapper;
import org.vinio.DTOs.ReplyDTO;
import org.vinio.Services.ReplyService;
import org.vinio.controllers.responseDTO.ReplyResponseDTO;

@Log4j2
@DgsComponent
public class ReplyQueryResolver {
    private ReplyService replyService;
    private ReplyMapper replyMapper;
    @Autowired
    public ReplyQueryResolver(ReplyService replyService, ReplyMapper replyMapper) {
        this.replyService = replyService;
        this.replyMapper = replyMapper;
    }



    @DgsQuery
    public ReplyResponseDTO getReply(@InputArgument Long id) {
        ReplyDTO reply = replyService.getReplyByMessageId(id);
        return replyMapper.convertToResponse(reply);
    }



    @DgsMutation
    public ReplyResponseDTO createReply(@InputArgument Long messageId, String body) {
        ReplyDTO reply = new ReplyDTO();
        reply.setMessage(messageId);
        reply.setBody(body);
        return replyMapper.convertToResponse(replyService.saveReply(reply));
    }
    @DgsMutation
    public ReplyResponseDTO updateReply(@InputArgument Long id, String body) {
        ReplyDTO reply = replyService.getReply(id);
        reply.setBody(body);
        return replyMapper.convertToResponse(replyService.updateReply(id, reply));
    }
    @DgsMutation
    public boolean deleteReply(@InputArgument Long id) {
        replyService.deleteReply(id);
        return true;
    }
}
