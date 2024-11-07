package org.vinio.controllers.graphQL.v1;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.vinio.DTOs.Mappers.MessageMapper;
import org.vinio.DTOs.MessageDTO;
import org.vinio.Services.MessageService;
import org.vinio.controllers.graphQL.inputs.MessageInputDTO;
import org.vinio.controllers.responseDTO.MessageResponseDTO;

@Log4j2
@DgsComponent
public class MessageQueryResolver {
    private MessageService messageService;
    private MessageMapper messageMapper;
    @Autowired
    public MessageQueryResolver(MessageService messageService, MessageMapper messageMapper) {
        this.messageService = messageService;
        this.messageMapper = messageMapper;
    }


    @DgsQuery
    public MessageResponseDTO getMessage(@InputArgument Long id) {
        MessageDTO message = messageService.getMessage(id);
        return messageMapper.convertToResponse(message);
    }



    @DgsMutation
    public MessageResponseDTO createMessage(@InputArgument(name = "message") MessageInputDTO messageInputDTO) {
        MessageDTO messageDTO = messageMapper.convertToDto(messageInputDTO);
        return messageMapper.convertToResponse(messageService.saveMessage(messageDTO));
    }
    @DgsMutation
    public MessageResponseDTO updateMessage(@InputArgument Long id, @InputArgument(name = "message") MessageInputDTO messageInputDTO) {
        MessageDTO messageDTO = messageMapper.convertToDto(messageInputDTO);
        MessageDTO message = messageService.updateMessage(id, messageDTO);
        return messageMapper.convertToResponse(messageService.updateMessage(id, message));
    }
    @DgsMutation
    public boolean deleteMessage(@InputArgument Long id) {
        messageService.deleteMessage(id);
        return true;
    }
}
