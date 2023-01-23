package com.daily.product.users.dto;

import com.daily.product.users.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserSaveRequestDto {
    private String type;
    private String name;
    private String email;
    private String password;
    private String address;
    private String addressDetail;
    private String loginFailLock;
    private int loginFailCount;
    private String useYn;

    public User toEntity() {
        return User.builder()
            .type(type)
            .name(name)
            .email(email)
            .password(password)
            .address(address)
            .addressDetail(addressDetail)
            .loginFailLock("N")
            .loginFailCount(0)
            .useYn("Y")
            .build();
    }
}
