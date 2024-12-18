package org.vinio.controllers.graphQL;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.InputArgument;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.vinio.DTOs.Mappers.MessageMapper;
import org.vinio.DTOs.MessageDTO;
import org.vinio.Services.MessageService;
import org.vinio.controllers.GraphQL.inputs.MessageInputDTO;
import org.vinio.controllers.GraphQL.resolvers.MessageQueryResolverInterface;
import org.vinio.dtos.response.MessageResponse;

@Log4j2
@DgsComponent
public class MessageQueryResolver implements MessageQueryResolverInterface {
    private final MessageService messageService;
    private final MessageMapper messageMapper;

    @Autowired
    public MessageQueryResolver(MessageService messageService, MessageMapper messageMapper) {
        this.messageService = messageService;
        this.messageMapper = messageMapper;
    }

    @Override
    public MessageResponse getMessage(@InputArgument @NotNull Long id) {
        MessageDTO message = messageService.getMessage(id);
        return messageMapper.convertToResponse(message);
    }

    @Override
    public MessageResponse createMessage(@InputArgument(name = "newMessage") @NotNull MessageInputDTO messageInputDTO) {
        MessageDTO messageDTO = messageMapper.convertToDto(messageInputDTO);
        return messageMapper.convertToResponse(messageService.saveMessage(messageDTO));
    }

    @Override
    public MessageResponse updateMessage(@InputArgument @NotNull Long id,
                                         @InputArgument(name = "updateMessage") @NotNull MessageInputDTO messageInputDTO) {
        MessageDTO messageDTO = messageMapper.convertToDto(messageInputDTO);
        MessageDTO message = messageService.updateMessage(id, messageDTO);
        return messageMapper.convertToResponse(messageService.updateMessage(id, message));
    }

    @Override
    public boolean deleteMessage(@InputArgument @NotNull Long id) {
        messageService.deleteMessage(id);
        return true;
    }
}
