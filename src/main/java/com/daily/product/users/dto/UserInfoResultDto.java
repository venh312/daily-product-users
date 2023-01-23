package com.daily.product.users.dto;

import com.daily.product.users.domain.User;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class UserInfoResultDto {
    private Long id;
    private String type;
    private String name;
    private String email;
    private String loginFailLock;
    private int loginFailCount;
    private LocalDateTime lastLoginTime;
    private String useYn;

    public UserInfoResultDto(User user) {
        this.id = user.getId();
        this.type = user.getType();
        this.name = user.getName();
        this.email = user.getEmail();
        this.loginFailLock = user.getLoginFailLock();
        this.loginFailCount = user.getLoginFailCount();
        this.lastLoginTime = user.getLastLoginTime();
        this.useYn = user.getUseYn();
    }
}
