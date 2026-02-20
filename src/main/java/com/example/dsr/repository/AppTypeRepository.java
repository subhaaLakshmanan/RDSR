package com.example.dsr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.dsr.model.AppType;

@Repository
public interface AppTypeRepository extends JpaRepository<AppType, Long>{

	@Query(value = "select * from application_type_master where isactive = 1 and isdelete = 0",nativeQuery = true)
	List<AppType> getAllActiveAppTypes();

}
