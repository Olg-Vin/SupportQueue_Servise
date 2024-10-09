package org.vinio.controllers.responseDTO;

import org.springframework.hateoas.Link;
import org.vinio.DTOs.MessageDTO;

import java.util.List;

public class MessageResponseDTO {
    private MessageDTO message;
    private List<Link> actions;  // Поле "_actions"
    private List<Link> links;  // Поле "_actions"

    public MessageResponseDTO(MessageDTO message, List<Link> actions, List<Link> links) {
        this.message = message;
        this.actions = actions;
        this.links = links;
    }

    public MessageResponseDTO(MessageDTO message) {
        this.message = message;
    }
}
