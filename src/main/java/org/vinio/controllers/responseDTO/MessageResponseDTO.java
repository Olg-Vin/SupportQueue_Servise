package org.vinio.controllers.responseDTO;

import org.springframework.hateoas.Affordance;
import org.springframework.hateoas.Link;
import org.vinio.entities.MessageEntity;

import java.util.List;
import java.util.Set;

public class MessageResponseDTO {
    private MessageEntity message;
    private List<Link> actions;  // Поле "_actions"

    public MessageResponseDTO(MessageEntity message, List<Link> actions) {
        this.message = message;
        this.actions = actions;
    }

}
