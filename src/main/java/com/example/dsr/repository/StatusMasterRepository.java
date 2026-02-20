package com.example.dsr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.dsr.model.StatusMaster;

public interface StatusMasterRepository extends JpaRepository<StatusMaster, Long>{

	@Query(value = "select status from status_master where isactive = 1",nativeQuery = true)
	List<String> getStatusList();

}
