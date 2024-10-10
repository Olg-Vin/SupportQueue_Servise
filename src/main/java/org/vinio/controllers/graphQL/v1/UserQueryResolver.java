package org.vinio.controllers.graphQL.v1;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.vinio.DTOs.Mappers.UserMapper;
import org.vinio.DTOs.UserDTO;
import org.vinio.Services.UserService;
import org.vinio.controllers.responseDTO.UserResponseDTO;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@Controller
public class UserQueryResolver {
    private UserService userService;
    private UserMapper userMapper;
    @Autowired
    public UserQueryResolver(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }



    @QueryMapping
    public UserResponseDTO getUser(@Argument("id") Long id) {
        UserDTO user = userService.getUser(id);
        return userMapper.convertToResponse(user, createActions(id), createLinks(id));
    }
    @QueryMapping(name = "getUsers")
    public List<UserResponseDTO> getUsers() {
        List<UserDTO> user = userService.getUsers();
        for (int i = 0; i < user.size(); i++) {
            System.out.println(user.get(i));
        }
        return user.stream().map(u -> userMapper.convertToResponse(u)).toList();
    }




    @MutationMapping
    public UserResponseDTO createUser(String name) {
        UserDTO user = new UserDTO();
        user.setName(name);
        return userMapper.convertToResponse(userService.saveUser(user));
    }
    @MutationMapping
    public UserResponseDTO updateUser(@Argument Long id, String name) {
        UserDTO userDTO = userService.getUser(id);
        if (name != null) userDTO.setName(name);
        return userMapper.convertToResponse(userService.updateUser(id, userDTO));
    }
    @MutationMapping
    public boolean deleteUser(@Argument Long id) {
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
