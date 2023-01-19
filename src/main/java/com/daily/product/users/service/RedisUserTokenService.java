package com.daily.product.users.service;

import com.daily.product.users.domain.RedisUserToken;
import com.daily.product.users.repository.RedisUserTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class RedisUserTokenService {

    private final RedisUserTokenRepository redisUserTokenRepository;

    public RedisUserTokenService(RedisUserTokenRepository redisUserTokenRepository) {
        this.redisUserTokenRepository = redisUserTokenRepository;
    }

    public void save(String email, String token, int expiration) {
        redisUserTokenRepository.save(RedisUserToken.builder()
            .email(email)
            .token(token)
            .expiration(expiration)
            .build());
    }

    public String get(String email) {
        return redisUserTokenRepository.findById(email)
            .orElseThrow(IllegalArgumentException::new)
            .getToken();
    }

    public void delete(String email) {
        redisUserTokenRepository.deleteById(email);
    }
}
