package com.daily.product.users.controller;

import com.daily.product.users.dto.UserInfoResultDto;
import com.daily.product.users.dto.UserSaveRequestDto;
import com.daily.product.users.dto.UserUpdateRequestDto;
import com.daily.product.users.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/save")
    public ResponseEntity<Long> save(UserSaveRequestDto userSaveDto) {
        return ResponseEntity.ok(userService.save(userSaveDto));
    }

    @GetMapping("/count/{email}")
    public ResponseEntity<Integer> countByEmail(
            @PathVariable String email) {
        return ResponseEntity.ok(userService.countByEmail(email));
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<UserInfoResultDto> findById(
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PatchMapping("/update/name")
    public ResponseEntity<Boolean> updateName(UserUpdateRequestDto userUpdateRequestDto) {
        return ResponseEntity.ok(userService.updateName(userUpdateRequestDto));
    }

    @PatchMapping("/update/password")
    public ResponseEntity<Boolean> updatePassword(UserUpdateRequestDto userUpdateRequestDto) {
        return ResponseEntity.ok(userService.updatePassword(userUpdateRequestDto));
    }

    @PatchMapping("/update/address")
    public ResponseEntity<Boolean> updateAddress(UserUpdateRequestDto userUpdateRequestDto) {
        return ResponseEntity.ok(userService.updateAddress(userUpdateRequestDto));
    }

    @PatchMapping("/update/login/lock")
    public ResponseEntity<Boolean> updateLoginFailLock(UserUpdateRequestDto userUpdateRequestDto) {
        return ResponseEntity.ok(userService.updateLoginFailLock(userUpdateRequestDto));
    }

    @PatchMapping("/update/login/fail")
    public ResponseEntity<Boolean> updateLoginFailCount(UserUpdateRequestDto userUpdateRequestDto) {
        return ResponseEntity.ok(userService.updateLoginFailCount(userUpdateRequestDto));
    }

    @PatchMapping("/update/use")
    public ResponseEntity<Boolean> updateUseYn(UserUpdateRequestDto userUpdateRequestDto) {
        return ResponseEntity.ok(userService.updateUseYn(userUpdateRequestDto));
    }
}