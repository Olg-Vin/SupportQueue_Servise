package org.vinio.controllers.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.hateoas.Link;

import java.util.List;

@Data
public class ReplyResponseDTO {
    @JsonProperty("replyId")
    private Long replyId;
    @JsonProperty("message")
    private String message;
    @JsonProperty("body")
    private String body;
    @JsonProperty("sentAt")
    private String sentAt;
    @JsonProperty("status")
    private String status;
    @JsonProperty("links")
    private List<Link> links;
    @JsonProperty("actions")
    private List<Link> actions;
}

