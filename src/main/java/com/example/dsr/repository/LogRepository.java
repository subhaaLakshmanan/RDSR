package com.example.dsr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.dsr.model.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, Long>{

	@Query(value = "select * from log order by id desc limit 1",nativeQuery = true)
	Log getLastlog();

	
	
}
