package com.daily.product.users.service;

import com.daily.product.users.domain.User;
import com.daily.product.users.dto.UserInfoResultDto;
import com.daily.product.users.dto.UserSaveRequestDto;
import com.daily.product.users.dto.UserUpdateRequestDto;
import com.daily.product.users.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
