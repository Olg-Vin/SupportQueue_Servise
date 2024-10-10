package org.vinio.controllers.responseDTO;

import lombok.Data;
import org.springframework.hateoas.Link;

import java.util.Date;
import java.util.List;

@Data
public class ReplyQLDto {
    private Long replyId;
    private Long message;
    private String body;
    private Date sentAt;
    private String status;
    private List<Link> actions;
    private List<Link> links;
}
