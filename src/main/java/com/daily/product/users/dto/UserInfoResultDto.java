package com.daily.product.users.dto;

import com.daily.product.users.domain.User;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class UserInfoResultDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String loginFailLock;
    private int loginFailCount;
    private LocalDateTime lastLoginTime;
    private String useYn;

    public UserInfoResultDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.loginFailLock = user.getLoginFailLock();
        this.loginFailCount = user.getLoginFailCount();
        this.lastLoginTime = user.getLastLoginTime();
        this.useYn = user.getUseYn();
    }
}
