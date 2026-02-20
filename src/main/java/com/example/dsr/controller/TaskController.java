package com.example.dsr.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.dsr.DTO.TaskDetails;
import com.example.dsr.model.Task;
import com.example.dsr.repository.TaskRepository;
import com.example.dsr.service.TaskService;

@CrossOrigin(origins = "*", maxAge = 3600)  // Allowing all origins is risky. Consider restricting to trusted domains in production.
@RestController
@RequestMapping("/api/v1/task")
public class TaskController {
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	TaskRepository taskRepo;
	
	@PostMapping("/create")
	public ResponseEntity<?> createTask(@RequestBody Task reqobj,HttpServletRequest servrequest){
		return taskService.createTask(reqobj,servrequest);
	}
	
	@PostMapping("/update")
	public ResponseEntity<?> updateTask(@RequestBody Task reqobj,HttpServletRequest servrequest){
		return taskService.updateTask(reqobj,servrequest);
	}
	
	@GetMapping("/tasklist")
	public List<TaskDetails> getTaskList(@RequestParam Long project_id,@RequestParam Long employee_id,@RequestParam Long phase_id,@RequestParam String task_type,
			@RequestParam String fromdate,@RequestParam String todate){
		return taskService.getTaskListByEmployee(project_id,employee_id,phase_id,task_type,fromdate,todate);
	}
	
	@GetMapping("/taskoverview")
	public List<TaskDetails> getTaskListForOverview(@RequestParam Long employee_id,@RequestParam Long selected_emp_id,@RequestParam String fromdate,@RequestParam String todate){
		return taskService.getTaskListForOverview(employee_id,selected_emp_id,fromdate,todate);
	}
	
	@GetMapping("/tasklistfortracking")
	public List<TaskDetails> getTaskListForTracking(@RequestParam Long project_id,@RequestParam Long employee_id){
		return taskService.getTaskListForTracking(project_id,employee_id);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> delete(@RequestParam String username,@RequestParam Long task_id,HttpServletRequest servrequest){
		return taskService.deleteTaskById(username,task_id,servrequest);
	}
	
	@GetMapping("/swaptask")
	public ResponseEntity<?> swapTaskBetweenPhases(@RequestParam Long phase_id,@RequestParam List<Long> task_list,@RequestParam String username,HttpServletRequest servrequest){
		return taskService.swapTaskBetweenPhases(phase_id,task_list,username,servrequest);
	}

}
