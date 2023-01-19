package com.daily.product.users.service;

import com.daily.product.users.common.UserCookie;
import com.daily.product.users.domain.User;
import com.daily.product.users.dto.UserInfoResultDto;
import com.daily.product.users.dto.UserLoginRequestDto;
import com.daily.product.users.dto.UserSaveRequestDto;
import com.daily.product.users.dto.UserUpdateRequestDto;
import com.daily.product.users.jwt.TokenProvider;
import com.daily.product.users.msg.Login;
import com.daily.product.users.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RedisUserTokenService redisUserTokenService;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, TokenProvider tokenProvider, RedisUserTokenService redisUserTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.redisUserTokenService = redisUserTokenService;
    }

    public HashMap<String, Object> loginAction(HttpServletResponse response, UserLoginRequestDto loginRequestDto) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", Login.EMPTY);
        resultMap.put("msg", Login.EMPTY.getValue());
        Optional<User> info = userRepository.findByEmail(loginRequestDto.getEmail());
        if (info.isPresent()) {
            User user = info.get();
            if ("N".equals(user.getUseYn())) {
                resultMap.put("code", Login.WITHDRAWAL);
                resultMap.put("msg", Login.WITHDRAWAL.getValue());
            } else if ("".equals(user.getLoginFailLock())) {
                resultMap.put("code", Login.LOCK);
                resultMap.put("msg", Login.LOCK.getValue());
            } else {
                if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
                    resultMap.put("code", Login.NOT_MATCH_PASSWORD);
                    resultMap.put("msg", Login.NOT_MATCH_PASSWORD.getValue());
                } else {
                    UserCookie cookie = new UserCookie();
                    HashMap<String, String> tokenMap = tokenProvider.generateRefreshToken(loginRequestDto.getEmail());
                    String token = tokenMap.get("token");
                    int expiration = Integer.parseInt(tokenMap.get("expiration"));
                    cookie.setAccessToken(response, tokenProvider.generateAccessToken(loginRequestDto.getEmail()));
                    cookie.setRefreshToken(response, token);
                    redisUserTokenService.save(loginRequestDto.getEmail(), token, expiration);
                    resultMap.put("code", Login.SUCCESS);
                    resultMap.put("msg", Login.SUCCESS.getValue());
                }
            }
        }
        return resultMap;
    }

    public void logout(HttpServletRequest request, String email) {
        UserCookie cookie = new UserCookie();
        if (cookie.getRefreshToken(request.getCookies()).equals(redisUserTokenService.get(email)))
            redisUserTokenService.delete(email);
    }

    public HashMap<String, Object> reissue(HttpServletRequest request, HttpServletResponse response, String email) {
        HashMap<String, Object> resultMap = new HashMap<>();

        String redisToken = redisUserTokenService.get(email);
        if (!StringUtils.hasText(redisToken))
            throw new RuntimeException("==> [Reissue Expires] logged out user. ");

        UserCookie cookie = new UserCookie();
        String cookieRefreshToken = cookie.getRefreshToken(request.getCookies());

        if (!cookieRefreshToken.equals(redisToken))
            throw new RuntimeException("==> [Reissue] The information in the token does not match.");

        HashMap<String, String> tokenMap = tokenProvider.generateRefreshToken(email);
        String token = tokenMap.get("token");
        int expiration = Integer.parseInt(tokenMap.get("expiration"));
        cookie.setAccessToken(response, tokenProvider.generateAccessToken(email));
        cookie.setRefreshToken(response, token);
        redisUserTokenService.save(email, token, expiration);

        resultMap.put("code", Login.REISSUE);
        resultMap.put("msg", Login.REISSUE.getValue());

        return resultMap;
    }

    public Long save(UserSaveRequestDto userSaveDto) {
        userSaveDto.setPassword(passwordEncoder.encode(userSaveDto.getPassword()));
        return userRepository.save(userSaveDto.toEntity()).getId();
    }

    public int countByEmail(String email) {
        return userRepository.countByEmail(email);
    }

    public UserInfoResultDto findById(Long id) {
        return userRepository.findById(id).map(UserInfoResultDto::new).orElse(null);
    }

    @Transactional
    public boolean updateName(UserUpdateRequestDto userUpdateRequestDto) {
        Optional<User> info = userRepository.findById(userUpdateRequestDto.getId());
        if (info.isPresent()) {
            info.get().updateName(userUpdateRequestDto.getName());
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updatePassword(UserUpdateRequestDto userUpdateRequestDto) {
        Optional<User> info = userRepository.findById(userUpdateRequestDto.getId());
        if (info.isPresent()) {
            info.get().updatePassword(userUpdateRequestDto.getPassword());
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateAddress(UserUpdateRequestDto userUpdateRequestDto) {
        Optional<User> info = userRepository.findById(userUpdateRequestDto.getId());
        if (info.isPresent()) {
            info.get().updateAddress(userUpdateRequestDto.getAddress(), userUpdateRequestDto.getAddressDetail());
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateLoginFailLock(UserUpdateRequestDto userUpdateRequestDto) {
        Optional<User> info = userRepository.findById(userUpdateRequestDto.getId());
        if (info.isPresent()) {
            info.get().updateLoginFailLock(userUpdateRequestDto.getLoginFailLock());
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateLoginFailCount(UserUpdateRequestDto userUpdateRequestDto) {
        Optional<User> info = userRepository.findById(userUpdateRequestDto.getId());
        if (info.isPresent()) {
            info.get().updateLoginFailCount(userUpdateRequestDto.getLoginFailCount());
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateUseYn(UserUpdateRequestDto userUpdateRequestDto) {
        Optional<User> info = userRepository.findById(userUpdateRequestDto.getId());
        if (info.isPresent()) {
            info.get().updateUseYn(userUpdateRequestDto.getUseYn());
            return true;
        }
        return false;
    }
}
