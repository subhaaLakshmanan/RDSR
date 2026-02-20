package com.example.dsr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.dsr.DTO.TaskLogDetails;
import com.example.dsr.model.ScheduleLog;

@Repository
public interface ScheduleLogRepository extends JpaRepository<ScheduleLog, Long>{

	@Query(nativeQuery = true)
	List<TaskLogDetails> getTaskLogById(String type, Long id,String tag);

}
