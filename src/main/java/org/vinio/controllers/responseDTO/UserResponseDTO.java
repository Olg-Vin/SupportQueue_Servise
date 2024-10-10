package org.vinio.controllers.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.hateoas.Link;

import java.util.List;

@Data
public class UserResponseDTO {
    @JsonProperty("id")
    private Long userId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("actions")
    private List<Link> actions;
    @JsonProperty("links")
    private List<Link> links;
}
