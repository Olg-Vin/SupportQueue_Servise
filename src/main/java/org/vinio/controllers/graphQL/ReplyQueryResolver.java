package org.vinio.controllers.graphQL;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.InputArgument;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.vinio.DTOs.Mappers.ReplyMapper;
import org.vinio.DTOs.ReplyDTO;
import org.vinio.Services.ReplyService;
import org.vinio.controllers.GraphQL.inputs.ReplyInputDTO;
import org.vinio.controllers.GraphQL.resolvers.ReplyQueryResolverInterface;
import org.vinio.dtos.response.ReplyResponse;

@Log4j2
@DgsComponent
public class ReplyQueryResolver implements ReplyQueryResolverInterface {
    private final ReplyService replyService;
    private final ReplyMapper replyMapper;

    @Autowired
    public ReplyQueryResolver(ReplyService replyService, ReplyMapper replyMapper) {
        this.replyService = replyService;
        this.replyMapper = replyMapper;
    }

    @Override
    public ReplyResponse getReply(@InputArgument @NotNull Long id) {
        ReplyDTO reply = replyService.getReplyByMessageId(id);
        return replyMapper.convertToResponse(reply);
    }

    @Override
    public ReplyResponse createReply(@InputArgument(name = "newReply") ReplyInputDTO replyInputDTO) {
        ReplyDTO reply = new ReplyDTO();
        reply.setMessage(replyInputDTO.getMessageId());
        reply.setBody(replyInputDTO.getBody());
        return replyMapper.convertToResponse(replyService.saveReply(reply));
    }

    @Override
    public ReplyResponse updateReply(@InputArgument @NotNull Long id,
                                     @InputArgument(name = "newReply") @NotNull ReplyInputDTO replyInputDTO) {
        ReplyDTO replyDTO = replyMapper.convertToDto(replyInputDTO);
        ReplyDTO reply = replyService.updateReply(id, replyDTO);
        return replyMapper.convertToResponse(reply);
    }

    @Override
    public boolean deleteReply(@InputArgument @NotNull Long id) {
        replyService.deleteReply(id);
        return true;
    }
}
