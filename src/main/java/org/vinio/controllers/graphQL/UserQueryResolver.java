package org.vinio.controllers.graphQL;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.InputArgument;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.vinio.DTOs.Mappers.UserMapper;
import org.vinio.DTOs.UserDTO;
import org.vinio.Services.UserService;
import org.vinio.controllers.GraphQL.inputs.UserInputDTO;
import org.vinio.controllers.GraphQL.resolvers.UserQueryResolverInterface;
import org.vinio.dtos.response.UserResponse;

import java.util.List;

@Log4j2
@Validated
@DgsComponent
public class UserQueryResolver implements UserQueryResolverInterface {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserQueryResolver(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse getUser(@InputArgument @NotNull Long id) {
        log.info("Fetching user with id: {}", id);
        UserDTO user = userService.getUser(id);
        if (user == null) {
            log.error("User not found for id: {}", id);
            throw new IllegalArgumentException("User not found");
        }
        return userMapper.convertToResponse(user);
    }

    @Override
    public List<UserResponse> getUsers() {
        log.info("Fetching all users");
        List<UserDTO> users = userService.getAllUsers();
        return users.stream()
                .map(userDTO -> (UserResponse) userMapper.convertToResponse(userDTO))
                .toList();
    }

    @Override
    public UserResponse createUser(@InputArgument(name = "newUser") UserInputDTO userInputDTO) {
        log.info("Creating user with name: {} and email: {}", userInputDTO.getName(), userInputDTO.getEmail());
        UserDTO user = new UserDTO();
        user.setName(userInputDTO.getName());
        user.setEmail(userInputDTO.getEmail());
        UserDTO savedUser = userService.saveUser(user);
        return userMapper.convertToResponse(savedUser);
    }

    @Override
    public UserResponse updateUser(@InputArgument @NotNull Long id,
                                   @InputArgument(name = "update") @NotNull UserInputDTO userInputDTO) {
        log.info("Updating user with id: {}", id);
        UserDTO userDTO = userService.getUser(id);
        if (userDTO == null) {
            log.error("User not found for id: {}", id);
            throw new IllegalArgumentException("User not found");
        }
        if (userInputDTO.getName() != null) userDTO.setName(userInputDTO.getName());
        if (userInputDTO.getEmail() != null) userDTO.setEmail(userInputDTO.getEmail());
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return userMapper.convertToResponse(updatedUser);
    }

    @Override
    public boolean deleteUser(@InputArgument @NotNull Long id) {
        log.info("Deleting user with id: {}", id);
        userService.deleteUser(id);
        return true;
    }
}
