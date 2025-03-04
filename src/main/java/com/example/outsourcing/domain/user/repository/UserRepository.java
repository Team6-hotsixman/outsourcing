package com.example.outsourcing.domain.user.repository;

import com.example.outsourcing.domain.common.exception.ApplicationException;
import com.example.outsourcing.domain.common.exception.ErrorCode;
import com.example.outsourcing.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User>findByEmail(String email);
    boolean existsByEmail(String email);
    default User findByIdOrElseThrow(Long userId) {
        return findById(userId).orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_USER));
    }
}
