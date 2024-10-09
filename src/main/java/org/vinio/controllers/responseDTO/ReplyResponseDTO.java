package org.vinio.controllers.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.vinio.DTOs.ReplyDTO;

import java.util.List;

public class ReplyResponseDTO {
    @JsonProperty("reply")
    private ReplyDTO reply;
    @JsonProperty("actions")
    private List<Link> actions;
    @JsonProperty("links")
    private List<Link> links;

    public ReplyResponseDTO(ReplyDTO reply, List<Link> actions, List<Link> links) {
        this.reply = reply;
        this.actions = actions;
        this.links = links;
    }

    public ReplyResponseDTO(ReplyDTO reply) {
        this.reply = reply;
    }
}
