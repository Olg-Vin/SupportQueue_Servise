package org.vinio.controllers.responseDTO;

import lombok.Data;
import org.springframework.hateoas.Link;

import java.util.List;

@Data
public class UserQLDto {
    private Long userId;
    private String name;
    private List<Link> actions;
    private List<Link> links;
}
