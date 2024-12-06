package org.vinio.controllers.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.Link;

import java.util.List;
import org.vinio.dtos.response.UserResponse;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserResponseDTO extends UserResponse {
    @JsonProperty("actions")
    private List<Link> actions;
    @JsonProperty("links")
    private List<Link> links;
}