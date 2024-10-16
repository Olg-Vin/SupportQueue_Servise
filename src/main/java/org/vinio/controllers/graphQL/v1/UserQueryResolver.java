package org.vinio.controllers.graphQL.v1;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.graphql.data.method.annotation.Argument;
//import org.springframework.graphql.data.method.annotation.MutationMapping;
//import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.vinio.DTOs.Mappers.UserMapper;
import org.vinio.DTOs.UserDTO;
import org.vinio.Services.UserService;
import org.vinio.controllers.responseDTO.UserResponseDTO;

import java.util.List;

@Log4j2
//@Controller
@DgsComponent
public class UserQueryResolver {
    private UserService userService;
    private UserMapper userMapper;
    @Autowired
    public UserQueryResolver(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }



//    @QueryMapping
    @DgsQuery
    public UserResponseDTO getUser(@InputArgument Long id) {
        UserDTO user = userService.getUser(id);
        return userMapper.convertToResponse(user);
    }
//    @QueryMapping(name = "getUsers")
    @DgsQuery
    public List<UserResponseDTO> getUsers() {
        List<UserDTO> user = userService.getUsers();
        for (int i = 0; i < user.size(); i++) {
            System.out.println(user.get(i));
        }
        return user.stream().map(u -> userMapper.convertToResponse(u)).toList();
    }




//    @MutationMapping
    @DgsMutation
    public UserResponseDTO createUser(@InputArgument String name) {
        UserDTO user = new UserDTO();
        user.setName(name);
        return userMapper.convertToResponse(userService.saveUser(user));
    }
//    @MutationMapping
    @DgsMutation
    public UserResponseDTO updateUser(@InputArgument Long id, @InputArgument String name) {
        UserDTO userDTO = userService.getUser(id);
        if (name != null) userDTO.setName(name);
        return userMapper.convertToResponse(userService.updateUser(id, userDTO));
    }
//    @MutationMapping
    @DgsMutation
    public boolean deleteUser(@InputArgument Long id) {
        userService.deleteUser(id);
        return true;
    }
}
