package com.example.dsr.controller;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.event.PublicInvocationEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dsr.DTO.DsrDetails;
import com.example.dsr.model.DSR;
import com.example.dsr.model.SubTask;
import com.example.dsr.repository.SubTaskRepository;
import com.example.dsr.service.SubTaskService;

@CrossOrigin(origins = "*", maxAge = 3600)  // Allowing all origins is risky. Consider restricting to trusted domains in production.
@RestController
@RequestMapping("/api/v1/subtask")
public class SubTaskController {
	
	@Autowired
	SubTaskService subTaskService;
	
	@Autowired
	SubTaskRepository subTaskRepo;
	
	@PostMapping("/create")
	public ResponseEntity<?> CreateSubTask(@RequestBody SubTask reqobj,HttpServletRequest servrequest){
		return subTaskService.CreateSubTask(reqobj,servrequest);
	}
	
	@GetMapping("/getsubtasks")
	public List<SubTask> getSubTaskList(@RequestParam Long taskid){
		return subTaskRepo.getSubTaskListbyTask(taskid);
	}
	
	@PostMapping("/update")
	public ResponseEntity<?> UpdateSubTask(@RequestBody SubTask reqobj,HttpServletRequest servrequest){
		return subTaskService.UpdateSubTask(reqobj,servrequest);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> DeleteSubTask(@RequestParam Long sub_task_id,@RequestParam String username,HttpServletRequest servrequest){
		return subTaskService.DeleteSubTask(sub_task_id,username,servrequest);
	}
	
	@PostMapping("/createdailystatus")
	public ResponseEntity<?> CreateDailyStatus(@RequestBody DSR reqobj,HttpServletRequest servrequest){
		return subTaskService.CreateDailyStatus(reqobj,servrequest);
	}
	
//	@PostMapping("/updatedailystatus")
//	public ResponseEntity<?> UpdateDailyStatus(@RequestBody DSR reqobj){
//		return subTaskService.UpdateDailyStatus(reqobj);
//	}
	
	@GetMapping("/getdsrdetails")
	public List<DSR> getDSRDetailsBySubTask(@RequestParam Long sub_task_id){
		return subTaskService.getDSRDetailsBySubTask(sub_task_id);
	}
	
	@DeleteMapping("/deletedsr")
	public ResponseEntity<?> DeleteDSR(@RequestParam String username,@RequestParam Long dsr_id){
		return subTaskService.DeleteDSR(username,dsr_id);
	}
	
	@GetMapping("/dsroverview")
    public List<DsrDetails> getDsrOverviewDetails(@RequestParam Long employee_id,@RequestParam String from_date,@RequestParam String to_date,@RequestParam Long user_id){
    	return subTaskService.getDsrOverviewDetails(employee_id,from_date,to_date,user_id);
    }
	

}
