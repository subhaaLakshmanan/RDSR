package com.example.dsr.serviceImpl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dsr.model.SubTask;
import com.example.dsr.DTO.DsrDetails;
import com.example.dsr.model.DSR;
import com.example.dsr.model.Employee;
import com.example.dsr.model.ScheduleLog;
import com.example.dsr.model.Task;
import com.example.dsr.repository.DSRRepository;
import com.example.dsr.repository.EmployeeRepository;
import com.example.dsr.repository.ScheduleLogRepository;
import com.example.dsr.repository.SubTaskRepository;
import com.example.dsr.repository.TaskRepository;
import com.example.dsr.repository.UserRepository;
import com.example.dsr.responce.MessageResponse;
import com.example.dsr.service.LogService;
import com.example.dsr.service.SubTaskService;

@Service
public class SubTaskServiceImplementation implements SubTaskService{
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	TaskRepository taskRepo;
	
	@Autowired
	SubTaskRepository subTaskRepo;
	
	@Autowired
	EmployeeRepository employeeRepo;
	
	@Autowired
	ScheduleLogRepository scheduleLogRepo;
	
	@Autowired
	LogService logService;
	
	@Autowired
	DSRRepository dSRRepo;

	@Override
	public ResponseEntity<?> CreateSubTask(SubTask reqobj, HttpServletRequest servrequest) {
		try {
            String remoteAddress = servrequest.getRemoteAddr();
			
            ResponseEntity responce = SubTaskValidation(reqobj);

			MessageResponse message = (MessageResponse) responce.getBody();
			
		    if (!message.getMessage().equals("Success")) {
				return responce;
			}
			
			long count = 0;
			try {
				count = subTaskRepo.getCountByTaskId(reqobj.getTaskid()) + 1;
			}catch(Exception e) {
				count = count + 1;
			}
			
			reqobj.setCode(taskRepo.getById(reqobj.getTaskid()).getTaskcode()+count);
			
            reqobj.setIsactive(true);
			
	        subTaskRepo.save(reqobj);
	        
//	        if(reqobj.getCompletion_percentage() != 0) {
//	        	taskRepo.updateCompletionPercentageForTask(reqobj.getTaskid());
//	        }
			
			SubTask subtask = subTaskRepo.getByCode(reqobj.getCode());
			
			String createdby = employeeRepo.getNameById(reqobj.getEmployeeid());
           
			ScheduleLog sch_log = new ScheduleLog(reqobj.getEmployeeid(),null,subtask.getId(),null,null,"Sub-task Creation","Sub-Task created by "+createdby,subtask.getStatus());
			scheduleLogRepo.save(sch_log);
			
			logService.ActivityLog(employeeRepo.getMailById(reqobj.getEmployeeid()), "Sub-task Creation", remoteAddress, subtask.getCode());
			
		}catch(Exception e) {
			System.out.println("Error in CreateSubTask : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
		return ResponseEntity.ok(new MessageResponse("Created successfully!"));
	}

	private ResponseEntity SubTaskValidation(SubTask reqobj) {
		try {
			
			if(reqobj.getTaskid() == null || reqobj.getTaskid() == 0) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid task details!"));
			}
			
			Task task = taskRepo.getById(reqobj.getTaskid());
			
			if(reqobj.getEmployeeid() == null || reqobj.getEmployeeid() == 0) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Select the valid employee details!"));
			}else {
				if(!userRepo.existsByEmail(employeeRepo.getById(reqobj.getEmployeeid()).getEmail())) {
					return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized User!"));
				}
			}
			
			if(reqobj.getStart_date().isBefore(task.getStart_date()) && reqobj.getEnd_date().isAfter(task.getEnd_date())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid start and end date!"));
			}
			
			if (reqobj.getStart_date().isAfter(reqobj.getEnd_date())) {
        	    return ResponseEntity.badRequest().body(new MessageResponse("Error: Start date cannot be after end date"));
        	}
			
			reqobj.setTask(reqobj.getTask().trim());
			if(reqobj.getTask() == null || reqobj.getTask().isEmpty()){
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Enter the valid task details!"));
			}
			
//			reqobj.setDescription(reqobj.getDescription().trim());
//			if(reqobj.getDescription() == null || reqobj.getDescription().isEmpty()){
//				return ResponseEntity.badRequest().body(new MessageResponse("Error: Enter the valid task description!"));
//			}
			
			if(reqobj.getPriority() == null || reqobj.getPriority().isEmpty()){
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Set valid priority!"));
			}
			
			if(reqobj.getStatus() == null || reqobj.getStatus().trim().equals("")) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid task status!"));
			}
			
			int daysBetween = (int) (ChronoUnit.DAYS.between(reqobj.getStart_date(), reqobj.getEnd_date()) + 1);
			
            int hours = 8 * daysBetween;
			String timeStr = String.format("%d:%02d", hours, 0);
			
			reqobj.setEstimated_hours(timeStr);
			
			String task_estimation_time = taskRepo.getById(reqobj.getTaskid()).getEstimated_hours();
			
			int task_estimation_hours = Integer.parseInt(task_estimation_time.split(":")[0]);
			System.out.println("task_estimation_hours : "+task_estimation_hours);
				
			int Total_sub_task_time = 0;
			
			try {
				Total_sub_task_time = reqobj.getId() == null ? subTaskRepo.getTotalSubTaskEstimationTimeByTaskId(reqobj.getTaskid(),(long) 0) : subTaskRepo.getTotalSubTaskEstimationTimeByTaskId(reqobj.getTaskid(),reqobj.getId());
			}catch(Exception e) {
				
			}
			
//			int estimatedHours = Integer.parseInt(reqobj.getEstimated_hours().split(":")[0]);

			if ((Total_sub_task_time + hours) > task_estimation_hours) {
				 return ResponseEntity.badRequest().body(new MessageResponse("Error: Estimated hours exceed total task estimation"));
			}
			
			if(reqobj.getWorked_hours() != null) {
				if(Integer.parseInt(reqobj.getWorked_hours().split(":")[0]) > hours) {
					return ResponseEntity.badRequest().body(new MessageResponse("Error: Worked hours exceed estimated hours"));
				}
			}
		}catch(Exception e) {
			System.out.println("Error in SubTaskValidation : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
		return ResponseEntity.ok(new MessageResponse("Success"));
	}

	@Override
	public ResponseEntity<?> UpdateSubTask(SubTask reqobj, HttpServletRequest servrequest) {
		try {
            String remoteAddress = servrequest.getRemoteAddr();
			
            ResponseEntity responce = SubTaskValidation(reqobj);

			MessageResponse message = (MessageResponse) responce.getBody();
			
		    if (!message.getMessage().equals("Success")) {
				return responce;
			}
		    
		    SubTask old_subtask = subTaskRepo.getByCode(reqobj.getCode());
		    
		    if(reqobj.getStatus().equals("Closed") || reqobj.getStatus().equals("Cancelled")) {
		    	reqobj.setIsactive(false);
		    }else {
		    	reqobj.setIsactive(true);
		    }
		    
            String createdby = employeeRepo.getNameById(reqobj.getEmployeeid());
			
            String remark;
            
            if(!old_subtask.getStatus().equals("Open") && reqobj.getStatus().equals("Open")){
                remark = "Status changed, Task reopened  by "+createdby;
            }else if(!old_subtask.getStatus().equals("Closed") && reqobj.getStatus().equals("Closed")){
                 remark = "Status changed, Task closed by "+createdby;
            }else if(!old_subtask.getEnd_date().equals(reqobj.getEnd_date()) || !old_subtask.getStart_date().equals(reqobj.getStart_date())){
                remark = "Task rescheduled by "+createdby;
            }else if(!old_subtask.getStatus().equals(reqobj.getStatus())) {
            	remark = "Status changed, Task status changed from "+old_subtask.getStatus()+" to "+reqobj.getStatus()+" by "+createdby;
            }else{
                remark = "Task data edited by "+createdby;
            }
			
			subTaskRepo.save(reqobj);
	        
//	        if(reqobj.getCompletion_percentage() != 0) {
//	        	taskRepo.updateCompletionPercentageForTask(reqobj.getTaskid());
//	        }
			
			ScheduleLog sch_log = new ScheduleLog(reqobj.getEmployeeid(),null,reqobj.getId(),null,null,"Edit sub-task details",remark,reqobj.getStatus());
			scheduleLogRepo.save(sch_log);
			
			logService.ActivityLog(employeeRepo.getMailById(reqobj.getEmployeeid()), "Edit sub-task details", remoteAddress, reqobj.getCode());
			
		}catch(Exception e) {
			System.out.println("Error in UpdateSubTask : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
		return ResponseEntity.ok(new MessageResponse("Updated successfully!"));
	}

	@Override
	public ResponseEntity<?> DeleteSubTask(Long sub_task_id, String username, HttpServletRequest servrequest) {
		try {
            String remoteAddress = servrequest.getRemoteAddr();
			
			if(!userRepo.existsByEmail(username)) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized User!"));
			}
			
			SubTask subtask = subTaskRepo.getById(sub_task_id);
			subtask .setIsdelete(true);
			subTaskRepo.save(subtask);
			
			Employee employee = employeeRepo.getByEmail(username);
			
			ScheduleLog sch_log = new ScheduleLog(employee.getId(),null,subtask.getId(),null,null,"Delete Task Schedule",subtask.getCode(),subtask.getStatus());
			scheduleLogRepo.save(sch_log);
			
            logService.ActivityLog(username, "Delete Sub-task details",remoteAddress,subtask.getCode());
			
			return ResponseEntity.ok(new MessageResponse("Deleted Successfully!"));
		}catch(Exception e) {
			System.out.println("Error in deleteTaskById : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
	}

	@Override
	public ResponseEntity<?> CreateDailyStatus(DSR reqobj,HttpServletRequest servrequest) {
		try {
			String remoteAddress = servrequest.getRemoteAddr();
			
			ResponseEntity responce = ValidationForDsr(reqobj);

			MessageResponse message = (MessageResponse) responce.getBody();
				
			if (!message.getMessage().equals("Success")) {
				return responce;
			}
			
			SubTask subtask = subTaskRepo.getById(reqobj.getSubtaskid());
			 
			dSRRepo.save(reqobj);
			
			subTaskRepo.updateCompletionPercentageForSubTask(reqobj.getSubtaskid(),reqobj.getCompletion_percentage());
			
			taskRepo.updateCompletionPercentageForTask(subTaskRepo.getById(reqobj.getSubtaskid()).getTaskid());
			
			logService.ActivityLog(reqobj.getUsername(), "Update DSR For Sub-task",remoteAddress,subtask.getCode());
	    }catch(Exception e) {
			System.out.println("Error in CreateDailyStatus : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
		return ResponseEntity.ok(new MessageResponse("Success!"));
	}
	
//	@Override
//	public ResponseEntity<?> UpdateDailyStatus(DSR reqobj) {
//		try {
//			ResponseEntity responce = ValidationForDsr(reqobj);
//
//			MessageResponse message = (MessageResponse) responce.getBody();
//				
//			if (!message.getMessage().equals("Success")) {
//				return responce;
//			}
//			 
//			dSRRepo.save(reqobj);
//		}catch(Exception e) {
//			System.out.println("Error in UpdateDailyStatus : "+e.getMessage());
//			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
//		}
//		return ResponseEntity.ok(new MessageResponse("Success!"));
//	}
	
	private ResponseEntity ValidationForDsr (DSR reqobj) {
		try {
			
			if(!userRepo.existsByEmail(reqobj.getUsername())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized User!"));
			}
			
			if(reqobj.getSubtaskid() == null || reqobj.getSubtaskid() == 0) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid task details!"));
			}
			
			if(reqobj.getDate() == null) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid date value!"));
			}else {
				
//				if(!reqobj.getDate().equals(LocalDate.now()) || !reqobj.getDate().equals(LocalDate.now().minusDays(1))) {
//					return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid date value!!"));
//				}
				
				SubTask subtask = subTaskRepo.getById(reqobj.getSubtaskid());
				
				if(!reqobj.getDate().isBefore(subtask.getStart_date()) && !reqobj.getDate().isAfter(subtask.getEnd_date())) {
					int date_count = 0;
					try {
						date_count = reqobj.getId() == 0 || reqobj.getId() == null ? dSRRepo.getDateCount(reqobj.getDate(),reqobj.getSubtaskid()): dSRRepo.getDateCountById(reqobj.getDate(),reqobj.getSubtaskid(),reqobj.getId());
	              	}catch(Exception e) {
						
					}
					
					if(date_count > 0) {
						return ResponseEntity.badRequest().body(new MessageResponse("Error: Duplicate date value!"));
					}
				}else {
					return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid date value!"));
				}
			}
			
			reqobj.setComments(reqobj.getComments().trim());
			if(reqobj.getComments() == null || reqobj.getComments().isEmpty()){
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Enter the valid comments!"));
			}
			
			if(reqobj.getWorked_hours() == null) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: The work time details are invalid"));
			}
			
			String sub_task_estimation_time = subTaskRepo.getById(reqobj.getSubtaskid()).getEstimated_hours();
			
			int sub_task_estimation_hours = Integer.parseInt(sub_task_estimation_time.split(":")[0]);
				
			int Total_worked_hours = 0;
			
			try {
				Total_worked_hours = dSRRepo.getTotalWorkingHoursForSubTask(reqobj.getSubtaskid(),reqobj.getId());
			}catch(Exception e) {
				
			}
			
            int today_worked_hours = Integer.parseInt(reqobj.getWorked_hours().split(":")[0]);

			if ((Total_worked_hours + today_worked_hours) > sub_task_estimation_hours) {
				 return ResponseEntity.badRequest().body(new MessageResponse("Error: Estimated hours exceed total task estimation"));
			}
			
			if(reqobj.getCompletion_percentage() == 0) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: The completion percentage is invalid!"));
			}
		}catch(Exception e) {
			System.out.println("Error in ValidationForDsr : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
		return ResponseEntity.ok(new MessageResponse("Success"));
	}

	@Override
	public List<DSR> getDSRDetailsBySubTask(Long sub_task_id) {
		List<DSR> list = new ArrayList<>();
		try{
			list = dSRRepo.getActiveDSRBySubTask(sub_task_id).stream()
					.peek(dsr -> {
				         SubTask sub_task = subTaskRepo.getById(dsr.getSubtaskid());
				         dsr.setTask(sub_task.getTask());
				         dsr.setCompletion_percentage(sub_task.getCompletion_percentage());
			         }).collect(Collectors.toList());
		}catch(Exception e) {
			System.out.println("Error in getDSRDetailsBySubTask : "+e.getMessage());
		}
		return list;
	}

	@Override
	public ResponseEntity<?> DeleteDSR(String username, Long dsr_id) {
		try {
			if(!userRepo.existsByEmail(username)) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized User!"));
			}
			dSRRepo.deleteDSRById(dsr_id);
		}catch(Exception e) {
			System.out.println("Error in DeleteDSR : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
		return ResponseEntity.ok(new MessageResponse("Success"));
	}

	@Override
	public List<DsrDetails> getDsrOverviewDetails(Long employee_id, String from_date, String to_date,Long user_id) {
		List<DsrDetails> list = new ArrayList<>();
		try {
			
//			Long dept_id = employeeRepo.getById(user_id).getDeptid();
			
			try {
				if(from_date.equalsIgnoreCase("null") && to_date.equalsIgnoreCase("null")) {
					String date = dSRRepo.getMaxDateByUserId(user_id);
					
					from_date = date;
					to_date = date;
				}
			}catch(Exception e) {
				String date = dSRRepo.getMaxDateByUserId(user_id);
				
				from_date = date;
				to_date = date;
			}
			
//			if(from_date.equalsIgnoreCase("null") && to_date.equalsIgnoreCase("null")) {
//				String date = dSRRepo.getMaxDateByUserId(user_id);
//				
//				from_date = date;
//				to_date = date;
//			}
			
			list = dSRRepo.getDsrOverviewDetails(employee_id,from_date,to_date,user_id);
		}catch(Exception e) {
				System.out.println("Exception in getDsrOverviewDetails : "+e.getMessage());
		}
		return list;
	}


}
