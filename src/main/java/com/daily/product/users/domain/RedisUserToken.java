package com.daily.product.users.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash("redisUserToken")
public class RedisUserToken {
    @Id
    String email;
    String token;
    @TimeToLive
    Integer expiration;

    @Builder
    public RedisUserToken(String email, String token, Integer expiration) {
        this.email = email;
        this.token = token;
        this.expiration = expiration;
    }
}
