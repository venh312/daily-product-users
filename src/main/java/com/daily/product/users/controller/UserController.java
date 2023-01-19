package com.daily.product.users.controller;

import com.daily.product.users.dto.UserInfoResultDto;
import com.daily.product.users.dto.UserLoginRequestDto;
import com.daily.product.users.dto.UserSaveRequestDto;
import com.daily.product.users.dto.UserUpdateRequestDto;
import com.daily.product.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RestController
@RequestMapping("/user")
@Tag(name = "USER", description = "USER API 명세서")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "[USER] 로그인 처리", description = "성공 시 쿠키에 accessToken/refreshToken 이 발급된다.")
    @Parameters({
        @Parameter(name="email", description = "이메일", required = true),
        @Parameter(name="password", description = "비밀번호", required = true)
    })
    @PostMapping("/login")
    public ResponseEntity<HashMap<String, Object>> loginAction(HttpServletResponse response, UserLoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(userService.loginAction(response, loginRequestDto));
    }

    @Operation(summary = "[USER] 로그아웃", description = "발급 한 AccessToken/refreshToken 을 클라이언트/서버에서 모두 삭제한다.")
    @Parameters({
        @Parameter(name="email", description = "이메일", required = true),
    })
    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request, String email) {
        userService.logout(request, email);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "[USER] AccessToken 재발급", description = "refreshToken 으로 AccessToken 을 재발급한다.")
    @Parameters({
        @Parameter(name="email", description = "이메일", required = true),
    })
    @PostMapping("/reissue")
    public ResponseEntity<HashMap<String, Object>> reissue(HttpServletRequest request, HttpServletResponse response, String email) {
        return ResponseEntity.ok(userService.reissue(request, response, email));
    }

    @Operation(summary = "[USER] 등록", description = "회원가입")
    @Parameters({
        @Parameter(name="name", description = "이름(닉네임)", required = true),
        @Parameter(name="email", description = "이메일", required = true),
        @Parameter(name="password", description = "비밀번호", required = true),
        @Parameter(name="address", description = "주소", required = true),
        @Parameter(name="addressDetail", description = "상세주소", required = true),
    })
    @PostMapping
    public ResponseEntity<Long> save(UserSaveRequestDto userSaveDto) {
        return ResponseEntity.ok(userService.save(userSaveDto));
    }

    @Operation(summary = "[USER] 이메일 존재 유무", description = "등록된 이메일이 있는지 확인한다.")
    @Parameters({
        @Parameter(name="email", description = "이메일", required = true),
    })
    @GetMapping("/count/{email}")
    public ResponseEntity<Integer> countByEmail(
            @PathVariable String email) {
        return ResponseEntity.ok(userService.countByEmail(email));
    }

    @Operation(summary = "[USER] 회원정보 조회", description = "등록된 회원의 정보를 조회한다.")
    @Parameters({
        @Parameter(name="id", description = "ID", required = true),
    })
    @GetMapping("/my-page/{id}")
    public ResponseEntity<UserInfoResultDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(summary = "[USER] 회원 이름 변경", description = "등록된 회원의 이름을 변경한다.")
    @Parameters({
        @Parameter(name="id", description = "ID", required = true),
        @Parameter(name="name", description = "이름", required = true),
    })
    @PutMapping("/my-page/name")
    public ResponseEntity<Boolean> updateName(UserUpdateRequestDto userUpdateRequestDto) {
        return ResponseEntity.ok(userService.updateName(userUpdateRequestDto));
    }

    @Operation(summary = "[USER] 회원 비밀번호 변경", description = "등록된 회원의 비밀번호를 변경한다.")
    @Parameters({
        @Parameter(name="id", description = "ID", required = true),
        @Parameter(name="password", description = "이름", required = true),
    })
    @PutMapping("/my-page/password")
    public ResponseEntity<Boolean> updatePassword(UserUpdateRequestDto userUpdateRequestDto) {
        return ResponseEntity.ok(userService.updatePassword(userUpdateRequestDto));
    }

    @Operation(summary = "[USER] 회원 주소 변경", description = "등록된 회원의 주소를 변경한다.")
    @Parameters({
        @Parameter(name="id", description = "ID", required = true),
        @Parameter(name="address", description = "주소", required = true),
        @Parameter(name="addressDetail", description = "상세주소", required = true),
    })
    @PutMapping("/my-page/address")
    public ResponseEntity<Boolean> updateAddress(UserUpdateRequestDto userUpdateRequestDto) {
        return ResponseEntity.ok(userService.updateAddress(userUpdateRequestDto));
    }

    @Operation(summary = "[USER] 회원 로그인 잠금 여부 변경", description = "등록된 회원의 로그인 잠금 여부를 변경한다.")
    @Parameters({
        @Parameter(name="id", description = "ID", required = true),
        @Parameter(name="loginFailLock", description = "로그인 잠금 여부", required = true),
    })
    @PutMapping("/my-page/login-lock")
    public ResponseEntity<Boolean> updateLoginFailLock(UserUpdateRequestDto userUpdateRequestDto) {
        return ResponseEntity.ok(userService.updateLoginFailLock(userUpdateRequestDto));
    }

    @Operation(summary = "[USER] 회원 로그인 실패 횟수 변경", description = "등록된 회원의 로그인 실패 횟수를 변경한다.")
    @Parameters({
        @Parameter(name="id", description = "ID", required = true),
        @Parameter(name="loginFailCount", description = "로그인 실패 횟수", required = true),
    })
    @PutMapping("/my-page/login-fail")
    public ResponseEntity<Boolean> updateLoginFailCount(UserUpdateRequestDto userUpdateRequestDto) {
        return ResponseEntity.ok(userService.updateLoginFailCount(userUpdateRequestDto));
    }

    @Operation(summary = "[USER] 회원 사용여부 변경", description = "등록된 회원의 사용여부를 변경한다.")
    @Parameters({
        @Parameter(name="id", description = "ID", required = true),
        @Parameter(name="useYn", description = "이름", required = true),
    })
    @PutMapping("/my-page/use")
    public ResponseEntity<Boolean> updateUseYn(UserUpdateRequestDto userUpdateRequestDto) {
        return ResponseEntity.ok(userService.updateUseYn(userUpdateRequestDto));
    }
}
