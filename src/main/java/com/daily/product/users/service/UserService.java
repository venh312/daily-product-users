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

    public HashMap<String, Object> loginResult(Login type) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", type);
        resultMap.put("msg", type.getValue());
        return resultMap;
    }

    @Transactional
    public HashMap<String, Object> loginAction(HttpServletResponse response, UserLoginRequestDto loginRequestDto) {
        Optional<User> info = userRepository.findByTypeAndEmail(loginRequestDto.getType(), loginRequestDto.getEmail());
        if (info.isPresent()) {
            User user = info.get();
            if ("N".equals(user.getUseYn()))
                return loginResult(Login.WITHDRAWAL);
            if ("Y".equals(user.getLoginFailLock()))
                return loginResult(Login.LOCK);
            if ("HOME".equals(loginRequestDto.getType()) && !passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
                if (user.getLoginFailCount() > 4)
                    user.updateLoginFailLock("Y");
                else
                    user.updateLoginFailCount(user.getLoginFailCount() + 1);
                return loginResult(Login.NOT_MATCH_PASSWORD);
            }

            HashMap<String, String> tokenMap = tokenProvider.generateToken(response, user.getId());
            redisUserTokenService.save(user.getId(), tokenMap.get("refreshToken"), Integer.parseInt(tokenMap.get("expiration")));

            HashMap<String, Object> resultMap = loginResult(Login.SUCCESS);
            resultMap.put("accessToken", tokenMap.get("accessToken"));

            return resultMap;
        }
        return loginResult(Login.EMPTY);
    }

    public void logout(HttpServletRequest request) {
        redisUserTokenService.delete(tokenProvider.getId(new UserCookie().getRefreshToken(request.getCookies())));
    }

    public HashMap<String, Object> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = new UserCookie().getRefreshToken(request.getCookies());

        Long refreshTokenId = tokenProvider.getId(refreshToken);
        if (refreshTokenId == null)
            return loginResult(Login.EXPIRES_REISSUE);

        String redisRefreshToken = redisUserTokenService.getToken(refreshTokenId);
        if (!refreshToken.equals(redisRefreshToken))
            return loginResult(Login.NOT_MATCH_REISSUE);

        HashMap<String, String> tokenMap = tokenProvider.generateToken(response, refreshTokenId);
        redisUserTokenService.save(refreshTokenId, tokenMap.get("refreshToken"), Integer.parseInt(tokenMap.get("expiration")));

        HashMap<String, Object> resultMap = loginResult(Login.REISSUE);
        resultMap.put("accessToken", tokenMap.get("accessToken"));

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
            info.get().updatePassword(passwordEncoder.encode(userUpdateRequestDto.getPassword()));
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
