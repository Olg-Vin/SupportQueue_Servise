package org.vinio.controllers.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.vinio.DTOs.UserDTO;

import java.util.List;

public class UserResponseDTO {
    @JsonProperty("user")
    private UserDTO user;
    @JsonProperty("actions")
    private List<Link> actions;  // Поле "_actions"

    public UserResponseDTO(UserDTO user, List<Link> actions) {
        this.user = user;
        this.actions = actions;
    }



}
