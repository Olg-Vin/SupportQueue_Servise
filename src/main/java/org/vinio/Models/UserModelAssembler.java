package org.vinio.Models;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import org.vinio.DTOs.UserDTO;
import org.vinio.controllers.v1.UserController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler extends RepresentationModelAssemblerSupport<UserDTO, UserModel> {

    public UserModelAssembler() {
        super(UserController.class, UserModel.class);
    }

    @Override
    public UserModel toModel(UserDTO entity) {
        UserModel userModel = createModelWithId(entity.getUserId(), entity);
        userModel.add(linkTo(methodOn(UserController.class).deleteUser(entity.getUserId())).withRel("delete").withType("DELETE")
                .andAffordance(afford(methodOn(UserController.class).updateUser(entity.getUserId(), null))));
        userModel.userId = entity.getUserId();
        userModel.name = entity.getName();
        userModel.contactInfo = entity.getContactInfo();
        return userModel;
    }
}