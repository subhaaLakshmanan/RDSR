package com.example.dsr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.dsr.model.Shift;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long>{

	@Query(value = "select * from shift_master where isactive = 1 and isdelete = 0",nativeQuery = true)
	List<Shift> getActiveList();

}
