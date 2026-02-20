package com.example.dsr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.dsr.model.Designation;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long>{

	@Query(value = "select * from position_master where dept_id =:department_id and isactive = 1 and isdelete = 0",nativeQuery = true)
	List<Designation> getDesignationByDepartment(Long department_id);

	@Query(value = "select position from position_master where id =:positionid",nativeQuery = true)
	String getPositionNameById(Long positionid);

}
