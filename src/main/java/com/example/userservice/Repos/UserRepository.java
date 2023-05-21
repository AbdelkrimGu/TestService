package com.example.userservice.Repos;

import com.example.userservice.user.Role;
import com.example.userservice.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Indexed;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);

  Long countByRole(Role role);


  @Query("SELECT u FROM User u WHERE u.confirmationToken = :token")
  User findByConfirmationToken(@Param("token") String token);


}
