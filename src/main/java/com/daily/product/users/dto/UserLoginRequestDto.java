package com.daily.product.users.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserLoginRequestDto {
    private String email;
    private String password;
}
