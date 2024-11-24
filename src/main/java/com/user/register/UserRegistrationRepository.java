package com.user.register;

import com.user.register.UserRegistrationmodel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRegistrationRepository extends JpaRepository<UserRegistrationmodel, String> {
    Optional<UserRegistrationmodel> findByEmail(String email);
    List<UserRegistrationmodel> findAll();
    void deleteByEmail(String email);
    }

