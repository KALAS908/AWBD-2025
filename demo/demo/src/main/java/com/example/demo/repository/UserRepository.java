package com.example.demo.repository;


import com.example.demo.Models.User;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Hidden
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);

    User findByUserName(String userName);
}
