package org.vinio.controllers.graphQL.v1;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.vinio.DTOs.Mappers.UserMapper;
import org.vinio.DTOs.UserDTO;
import org.vinio.Services.UserService;
import org.vinio.controllers.responseDTO.UserQLDto;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@Component
public class UserQueryResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    // Query to get user by ID
    public UserQLDto getUser(Long id) {
        UserDTO user = userService.getUser(id);
        return userMapper.convertToQLDto(user, createActions(id), createLinks(id));
    }

    // Mutation to create a user
    public UserQLDto createUser(String name) {
        UserDTO user = new UserDTO();
        user.setName(name);
        return userMapper.convertToQLDto(userService.saveUser(user));
    }

    // Mutation to update a user
    public UserQLDto updateUser(Long id, String name) {
        UserDTO userDTO = userService.getUser(id);
        if (name != null) userDTO.setName(name);
        return userMapper.convertToQLDto(userService.updateUser(id, userDTO));
    }

    // Mutation to delete a user
    public boolean deleteUser(Long id) {
        userService.deleteUser(id);
        return true;
    }

    // HATEOAS Links
    private List<Link> createLinks(Long id) {
        Link selfLink = linkTo(methodOn(UserQueryResolver.class).getUser(id)).withSelfRel();
        Link updateLink = linkTo(methodOn(UserQueryResolver.class).updateUser(id, null)).withRel("update");
        Link deleteLink = linkTo(methodOn(UserQueryResolver.class).deleteUser(id)).withRel("delete");
        return List.of(selfLink, updateLink, deleteLink);
    }

    // HATEOAS Actions
    private List<Link> createActions(Long id) {
        Link messageLink = linkTo(methodOn(MessageQueryResolver.class).getMessagesByUserId(id)).withRel("messages");
        return List.of(messageLink);
    }
}
