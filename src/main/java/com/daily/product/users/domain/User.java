package com.daily.product.users.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity(name = "user")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    @Builder
    public User(Long id, String name, String email, String password, String address, String addressDetail, String loginFailLock, int loginFailCount, LocalDateTime lastLoginTime, LocalDateTime registerTime, String useYn) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.addressDetail = addressDetail;
        this.loginFailLock = loginFailLock;
        this.loginFailCount = loginFailCount;
        this.lastLoginTime = lastLoginTime;
        this.registerTime = registerTime;
        this.useYn = useYn;
    }

    public void updateName(String name) {
        this.name = name;
    }
    public void updatePassword(String password) {
        this.password = password;
    }
    public void updateAddress(String address, String addressDetail) {
        this.address = address;
        this.addressDetail = addressDetail;
    }
    public void updateLoginFailLock(String loginFailLock) {
        this.password = loginFailLock;
    }
    public void updateLoginFailCount(int loginFailCount) {
        this.loginFailCount = loginFailCount;
    }
    public void updateUseYn(String useYn) {
        this.password = useYn;
    }
}
