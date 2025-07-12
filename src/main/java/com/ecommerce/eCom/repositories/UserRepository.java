package com.ecommerce.eCom.repositories;

import com.ecommerce.eCom.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User,Long> {

    Optional<User> findByUserName(String username);
    Optional<User> findUserPassword(String username);
    boolean exitsByUserName(String username);
    boolean existsByEmail(@Email String email);
}
