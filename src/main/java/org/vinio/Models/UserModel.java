package org.vinio.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@Builder
public class UserModel extends RepresentationModel<UserModel> {
    public Long userId;
    public String name;
    public String contactInfo;

    protected UserModel() {
    }
}