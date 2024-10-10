package org.vinio.controllers.responseDTO;

import lombok.Data;
import org.springframework.hateoas.Link;

import java.util.Date;
import java.util.List;

@Data
public class MessageQLDto {
    private Long messageId;
    private Long user;
    private String category;
    private String subject;
    private String body;
    private Date createdAt;
    private String status;
    private List<Link> actions;
    private List<Link> links;
}
