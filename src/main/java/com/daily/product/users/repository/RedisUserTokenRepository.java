package com.daily.product.users.repository;

import com.daily.product.users.domain.RedisUserToken;
import org.springframework.data.repository.CrudRepository;

public interface RedisUserTokenRepository extends CrudRepository<RedisUserToken, String> {
}
