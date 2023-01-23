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
    Long id;
    String token;
    @TimeToLive
    Integer expiration;

    @Builder
    public RedisUserToken(Long id, String token, Integer expiration) {
        this.id = id;
        this.token = token;
        this.expiration = expiration;
    }
}
