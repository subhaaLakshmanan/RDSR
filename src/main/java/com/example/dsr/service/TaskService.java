package com.example.dsr.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.example.dsr.DTO.TaskDetails;
import com.example.dsr.DTO.TaskLogDetails;
import com.example.dsr.model.Task;

public interface TaskService {

	ResponseEntity<?> createTask(Task reqobj, HttpServletRequest servrequest);

	List<TaskDetails> getTaskListByEmployee(Long project_id,Long employee_id,Long phase_id,String task_type,String fromdate,String todate);

	ResponseEntity<?> deleteTaskById(String username, Long task_id, HttpServletRequest servrequest);

	ResponseEntity<?> updateTask(Task reqobj, HttpServletRequest servrequest);

	ResponseEntity<?> swapTaskBetweenPhases(Long phase_id, List<Long> task_list,String username, HttpServletRequest servrequest);

	List<TaskLogDetails> getTaskLogById(String type, Long id,String tag);

	List<TaskDetails> getTaskListForTracking(Long project_id, Long employee_id);
	
	List<TaskDetails> getTaskListForOverview(Long employee_id,Long selected_emp_id, String fromdate, String todate);

}
