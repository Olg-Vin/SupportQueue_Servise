package org.vinio.controllers.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.vinio.DTOs.UserDTO;

import java.util.List;

public class UserResponseDTO {
    @JsonProperty("user")
    private UserDTO user;
    @JsonProperty("actions")
    private List<Link> actions;
    @JsonProperty("links")
    private List<Link> links;

    public UserResponseDTO(UserDTO user, List<Link> actions, List<Link> links) {
        this.user = user;
        this.actions = actions;
        this.links = links;
    }

    public UserResponseDTO(UserDTO user) {
        this.user = user;
    }
}
