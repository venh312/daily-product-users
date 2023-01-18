package com.daily.product.users.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class UserUpdateRequestDto {
    private Long id;
    private String name;
    private String password;
    private String address;
    private String addressDetail;
    private String loginFailLock;
    private int loginFailCount;
    private String useYn;
}
