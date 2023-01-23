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

    public void save(Long id, String token, int expiration) {
        redisUserTokenRepository.save(RedisUserToken.builder()
            .id(id)
            .token(token)
            .expiration(expiration)
            .build());
    }

    public String getToken(Long id) {
        return redisUserTokenRepository.findById(id)
            .orElseThrow(IllegalArgumentException::new)
            .getToken();
    }

    public Long getId(String token) {
        return redisUserTokenRepository.findByToken(token)
            .orElseThrow(IllegalArgumentException::new)
            .getId();
    }

    public void delete(Long id) {
        redisUserTokenRepository.deleteById(id);
    }
}
