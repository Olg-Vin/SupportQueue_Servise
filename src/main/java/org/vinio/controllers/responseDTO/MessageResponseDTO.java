package org.vinio.controllers.responseDTO;

import org.springframework.hateoas.Link;

import javax.xml.crypto.Data;
import java.util.List;

@lombok.Data
public class MessageResponseDTO {
    private Long messageId;
    private Long user;
    private String body;
    private Data createdAt;
    private List<Link> actions;
    private List<Link> links;
}
