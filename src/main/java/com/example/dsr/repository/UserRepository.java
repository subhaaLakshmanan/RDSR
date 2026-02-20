package com.example.dsr.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.dsr.model.User;



public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String username);
	
    boolean existsByEmail(String email);

	User getByEmail(String email);

	
	
	
	
}