package com.daily.product.users.repository;

import com.daily.product.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    int countByEmail(String email);
}
