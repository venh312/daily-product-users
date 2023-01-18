package com.daily.product.users.controller;

import com.daily.product.users.dto.UserInfoResultDto;
import com.daily.product.users.dto.UserSaveRequestDto;
import com.daily.product.users.service.UserService;
import com.netflix.discovery.converters.Auto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void save() {
        UserSaveRequestDto userSaveRequestDto = new UserSaveRequestDto();
        userSaveRequestDto.setName("테스트");
        userSaveRequestDto.setEmail("conf312@naver.com");
        userSaveRequestDto.setPassword("테스트");
        userSaveRequestDto.setAddress("테스트");
        userSaveRequestDto.setAddressDetail("테스트");
        Assert.notNull(userService.save(userSaveRequestDto), "회원가입 실패");
    }

    @Test
    void verifyPassword() {
        UserInfoResultDto info = userService.findById(4L);
        Assert.isTrue(bCryptPasswordEncoder.matches("테스트", info.getPassword()), "비밀번호가 같지 않습니다.");
    }
}