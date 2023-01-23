package com.daily.product.users.repository;

import com.daily.product.users.domain.RedisUserToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RedisUserTokenRepository extends CrudRepository<RedisUserToken, Long> {
    Optional<RedisUserToken> findByToken(String token);
    void deleteByToken(String token);
}
