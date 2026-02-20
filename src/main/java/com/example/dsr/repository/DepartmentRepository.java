package com.example.dsr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.dsr.model.Department;
import com.example.dsr.model.Designation;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>{

	@Query(value = "select * from department_master where isactive = 1 and isdelete = 0",nativeQuery = true)
	List<Department> getActiveList();

	@Query(value = "select department_name from department_master where id =:deptid",nativeQuery = true)
	String getNameById(Long deptid);


	
	

}
