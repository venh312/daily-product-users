package com.daily.product.users.dto;

import com.daily.product.users.domain.User;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
public class UserSaveRequestDto {
    private String name;
    private String email;
    private String password;
    private String address;
    private String addressDetail;
    private String loginFailLock;
    private int loginFailCount;
    private LocalDateTime lastLoginTime;
    private LocalDateTime registerTime;
    private String useYn;

    public User toEntity() {
        return User.builder()
            .name(name)
            .email(email)
            .password(password)
            .address(address)
            .addressDetail(addressDetail)
            .loginFailLock("N")
            .loginFailCount(0)
            .lastLoginTime(lastLoginTime)
            .registerTime(LocalDateTime.now())
            .useYn("Y")
            .build();
    }
}
