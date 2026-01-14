package com.example.taskMS.repository;

import com.example.taskMS.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Crucial for JWT Authentication later
    Optional<User> findByEmail(String email);

    // Crucial for Registration validation
    Boolean existsByEmail(String email);
}