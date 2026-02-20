package com.example.dsr.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.dsr.model.ERole;
import com.example.dsr.model.Role;



@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	Optional<Role> findByRole(ERole name);
	
	Boolean existsByRole(ERole name);
}
