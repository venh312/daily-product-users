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

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
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
                    cookie.setRefreshToken(response, tokenProvider.generateAccessToken(loginRequestDto.getEmail()));
                    cookie.setRefreshToken(response, tokenProvider.generateRefreshToken(loginRequestDto.getEmail()));
                    resultMap.put("code", Login.SUCCESS);
                    resultMap.put("msg", Login.SUCCESS.getValue());
                }
            }
        }
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
