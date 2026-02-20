package com.example.dsr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.dsr.model.ServerDetails;

@Repository
public interface ServerDetailsRepository extends JpaRepository<ServerDetails, Long>{

	@Query(value = "select * from server_details where isactive = 1",nativeQuery = true)
	ServerDetails getServerDetails();

}
