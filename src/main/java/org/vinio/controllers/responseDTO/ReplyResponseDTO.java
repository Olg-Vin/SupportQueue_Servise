package org.vinio.controllers.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Affordance;
import org.springframework.hateoas.Link;
import org.vinio.DTOs.ReplyDTO;
import org.vinio.entities.ReplyEntity;

import java.util.List;
import java.util.Set;

public class ReplyResponseDTO {
    @JsonProperty("reply")
    private ReplyDTO reply;
    @JsonProperty("actions")
    private List<Link> actions;  // Поле "_actions"

    public ReplyResponseDTO(ReplyDTO reply, List<Link> actions) {
        this.reply = reply;
        this.actions = actions;
    }

}
