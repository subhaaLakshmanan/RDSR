package com.example.dsr.serviceImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.dsr.DTO.TaskDetails;
import com.example.dsr.DTO.TaskLogDetails;
import com.example.dsr.model.Employee;
import com.example.dsr.model.Phase;
import com.example.dsr.model.ScheduleLog;
import com.example.dsr.model.Task;
import com.example.dsr.repository.EmployeeRepository;
import com.example.dsr.repository.PhaseRepository;
import com.example.dsr.repository.ScheduleLogRepository;
import com.example.dsr.repository.SubTaskRepository;
import com.example.dsr.repository.TaskRepository;
import com.example.dsr.repository.UserRepository;
import com.example.dsr.responce.MessageResponse;
import com.example.dsr.service.LogService;
import com.example.dsr.service.TaskService;

@Service
public class TaskServiceImplementation implements TaskService{
	
	@Autowired
	TaskRepository taskRepo;
	
	@Autowired
	EmployeeRepository employeeRepo;
	
	@Autowired
	ScheduleLogRepository scheduleLogRepo;
	
	@Autowired
	LogService logService;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	PhaseRepository phaseRepo;
	
	@Autowired
	SubTaskRepository subTaskRepo;
	
	

	@Override
	public ResponseEntity<?> createTask(Task reqobj, HttpServletRequest servrequest) {
		try {
            String remoteAddress = servrequest.getRemoteAddr();
			
            ResponseEntity responce = taskValidation(reqobj);

			MessageResponse message = (MessageResponse) responce.getBody();
			
		    if (!message.getMessage().equals("Success")) {
				return responce;
			}
			
			long max_id = 0;
			try {
				max_id = taskRepo.getMaxId() + 1;
			}catch(Exception e) {
				max_id = max_id + 1;
			}
			
			String code = String.format("%06d", max_id);
			reqobj.setTaskcode("TSK" + code);
			
//			int daysBetween = (int) (ChronoUnit.DAYS.between(reqobj.getStart_date(), reqobj.getEnd_date()) + 1);
//			
//            int hours = 8 * daysBetween;
//			String timeStr = String.format("%d:%02d", hours, 0);
//			
//			reqobj.setEstimated_hours(timeStr);
			reqobj.setIsactive(true);
			
	        taskRepo.save(reqobj);
			
			Task task = taskRepo.getTaskByTaskcode(reqobj.getTaskcode());
			
			String createdby = employeeRepo.getNameById(reqobj.getAssigned_from());
            String assignedto = employeeRepo.getNameById(reqobj.getAssigned_to());
            
			ScheduleLog sch_log = new ScheduleLog(reqobj.getAssigned_from(), task.getId(),null,null,null,"Task Creation","Task created by "+createdby+" &  assigned to "+assignedto,task.getStatus());
			scheduleLogRepo.save(sch_log);
			
			ScheduleLog sch_log1 = new ScheduleLog(reqobj.getAssigned_from(), task.getId(),null,null,null,"Task Creation","Status changed, Task opened by "+createdby,task.getStatus());
			scheduleLogRepo.save(sch_log1);
			
			logService.ActivityLog(employeeRepo.getMailById(reqobj.getAssigned_from()), "Task Creation", remoteAddress, task.getTaskcode());
			
		}catch(Exception e) {
			System.out.println("Error in createTask : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
		return ResponseEntity.ok(new MessageResponse("Created successfully!"));
	}
	
	@Override
	public ResponseEntity<?> updateTask(Task reqobj, HttpServletRequest servrequest) {
		try {
            String remoteAddress = servrequest.getRemoteAddr();
			
            ResponseEntity responce = taskValidation(reqobj);

			MessageResponse message = (MessageResponse) responce.getBody();
			
		    if (!message.getMessage().equals("Success")) {
				return responce;
			}
		    
		    Task oldObj = taskRepo.getTaskByTaskcode(reqobj.getTaskcode());
		   
//			int daysBetween = (int) (ChronoUnit.DAYS.between(reqobj.getStart_date(), reqobj.getEnd_date()) + 1);
//			
//            int hours = 8 * daysBetween;
//			String timeStr = String.format("%d:%02d", hours, 0);
//			
//			reqobj.setEstimated_hours(timeStr);
		    
		    if(reqobj.getStatus().equals("Closed") || reqobj.getStatus().equals("Cancelled")) {
		    	reqobj.setIsactive(false);
		    }else {
		    	reqobj.setIsactive(true);
		    }
		    
		    String createdby = employeeRepo.getNameById(reqobj.getAssigned_from());
            String assignedto = employeeRepo.getNameById(reqobj.getAssigned_to());
            
            String remark;
            
            if((!oldObj.getAssigned_to().equals(reqobj.getAssigned_to())) && (!oldObj.getStatus().equals("Open") && reqobj.getStatus().equals("Open"))){
                remark = "Status changed, Task reopened  by "+createdby+" & task reassigned to "+assignedto;
            }else if((oldObj.getAssigned_to().equals(reqobj.getAssigned_to())) && (!oldObj.getStatus().equals("Open") && reqobj.getStatus().equals("Open"))) {
            	remark = "Status changed, Task reopened  by "+createdby;
            }else if(!oldObj.getStatus().equals("Closed") && reqobj.getStatus().equals("Closed")){
                 remark = "Status changed, Task closed by "+createdby;
            }else if(!oldObj.getAssigned_to().equals(reqobj.getAssigned_to())){
                remark = "Schedule data edited by "+createdby+" & task reassigned to "+assignedto;
            }else if(!oldObj.getEnd_date().equals(reqobj.getEnd_date()) || !oldObj.getStart_date().equals(reqobj.getStart_date())){
                remark = "Task rescheduled by "+createdby;
            }else if(!oldObj.getStatus().equals(reqobj.getStatus())) {
            	remark = "Status changed, Task status changed from "+oldObj.getStatus()+" to "+reqobj.getStatus()+" by "+createdby;
            }else{
                remark = "Task data edited by "+createdby;
            }
		    
			taskRepo.save(reqobj);
			 
			Task task = taskRepo.getTaskByTaskcode(reqobj.getTaskcode());
            
            ScheduleLog sch_log = new ScheduleLog(reqobj.getAssigned_from(), task.getId(),null,null,null,"Edit Task Schedule",remark,task.getStatus());
			scheduleLogRepo.save(sch_log);
			
			logService.ActivityLog(employeeRepo.getMailById(reqobj.getAssigned_from()), "Edit Task Schedule", remoteAddress, task.getTaskcode());
			
		}catch(Exception e) {
			System.out.println("Error in updateTask : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
		return ResponseEntity.ok(new MessageResponse("Updated successfully!"));
	}
	
	private ResponseEntity taskValidation(Task reqobj) {
		try {
			if(reqobj.getProjectid() == null || reqobj.getProjectid() == 0) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid project details!"));
			}
			
//			reqobj.setVersion(reqobj.getVersion().trim());
//			if(reqobj.getVersion() == null || reqobj.getVersion().isEmpty()){
//				return ResponseEntity.badRequest().body(new MessageResponse("Error: Enter the valid release version!"));
//			}
			
			reqobj.setTask(reqobj.getTask().trim());
			if(reqobj.getTask() == null || reqobj.getTask().isEmpty()){
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Enter the valid task details!"));
			}
			
//			reqobj.setDescription(reqobj.getDescription().trim());
//			if(reqobj.getDescription() == null || reqobj.getDescription().isEmpty()){
//				return ResponseEntity.badRequest().body(new MessageResponse("Error: Enter the valid task description!"));
//			}
			
			if(reqobj.getTask_type() == null || reqobj.getTask_type().isEmpty()){
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid task type!"));
			}
			
			if(reqobj.getPriority() == null || reqobj.getPriority().isEmpty()){
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Set valid priority!"));
			}
			
			if(reqobj.getStatus() == null || reqobj.getStatus().trim().equals("")) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid task status!"));
			}
			
			if(reqobj.getAssigned_from() == null || reqobj.getAssigned_from() == 0) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Select the valid employee details!"));
			}else {
//				if(!userRepo.existsById(reqobj.getAssigned_from())) {
//					return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized User!"));
//				}
				
				if(!userRepo.existsByEmail(employeeRepo.getById(reqobj.getAssigned_from()).getEmail())) {
					return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized User!"));
				}
			}
			
			if(reqobj.getAssigned_to() == null || reqobj.getAssigned_to() == 0) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Select the valid employee details!"));
			}else {
				if(!userRepo.existsByEmail(employeeRepo.getById(reqobj.getAssigned_to()).getEmail())) {
					return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized User!"));
				}
			}
			
			if (reqobj.getStart_date().isAfter(reqobj.getEnd_date())) {
        	    return ResponseEntity.badRequest().body(new MessageResponse("Error: Start date cannot be after end date"));
        	}
			
//			if(reqobj.getPhaseid() != 0) {
//				reqobj.setVersion(phaseRepo.getVersionById(reqobj.getPhaseid()));
//			}
			
            int daysBetween = (int) (ChronoUnit.DAYS.between(reqobj.getStart_date(), reqobj.getEnd_date()) + 1);
			
            int hours = 8 * daysBetween;
			String timeStr = String.format("%d:%02d", hours, 0);
			
			reqobj.setEstimated_hours(timeStr);
			
		}catch(Exception e) {
			System.out.println("Error in taskValidation : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
		 return ResponseEntity.ok(new MessageResponse("Success"));
	}

	@Override
	public List<TaskDetails> getTaskListByEmployee(Long project_id,Long employee_id,Long phase_id,String task_type,String fromdate,String todate) {
        List<TaskDetails> list = new ArrayList<>();
		try { 
			System.out.println(1);
			 if(fromdate == null || todate == null) {
				fromdate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).toString();
				todate = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)).toString();
             }
             list = taskRepo.getTaskListByEmployee(project_id,employee_id,phase_id,task_type,fromdate,todate);
             System.out.println("size : "+list.size());
		}catch(Exception e) {
			System.out.println("Error in getTaskListByEmployee : "+e.getMessage());
		}
		return list;
	}

	@Override
	public ResponseEntity<?> deleteTaskById(String username, Long task_id, HttpServletRequest servrequest) {
		try {
            String remoteAddress = servrequest.getRemoteAddr();
			
			if(!userRepo.existsByEmail(username)) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized User!"));
			}
			
			Task task = taskRepo.getById(task_id);
			task .setIsdelete(true);
			taskRepo.save(task);
			
			subTaskRepo.updateIsDeleteForSubtask(task_id);
			
			Employee employee = employeeRepo.getByEmail(username);
			
			ScheduleLog sch_log = new ScheduleLog(employee.getId(), task.getId(),null,null,null,"Delete Task Schedule",task.getTaskcode(),task.getStatus());
			scheduleLogRepo.save(sch_log);
			
            logService.ActivityLog(username, "Delete Task",remoteAddress,task.getTaskcode());
			
			return ResponseEntity.ok(new MessageResponse("Deleted Successfully!"));
		}catch(Exception e) {
			System.out.println("Error in deleteTaskById : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
	}

	@Override
	public ResponseEntity<?> swapTaskBetweenPhases(Long phase_id, List<Long> task_list,String username,HttpServletRequest servrequest) {
		try {
			String remoteAddress = servrequest.getRemoteAddr();
			
			if(!userRepo.existsByEmail(username)) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized User!"));
			}
			
			if(task_list.isEmpty()) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid Task Details!"));
			}
			
			if(!phaseRepo.existsById(phase_id)) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid Phase Details!"));
			}
			
		    taskRepo.swapTaskBetweenPhases(phase_id,task_list);
			
			String taskIds = task_list.stream() .map(String::valueOf).collect(Collectors.joining(","));

			logService.ActivityLog(username, "Swap Task",remoteAddress,"Phase_id : "+phase_id+",Task List : "+taskIds);
			
		}catch(Exception e) {
			System.out.println("Error in swapTaskBetweenPhases : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
		return ResponseEntity.ok(new MessageResponse("Success!"));
	}

	@Override
	public List<TaskLogDetails> getTaskLogById(String type, Long id,String tag) {
		List<TaskLogDetails> list = new ArrayList<>();
		try {
			list = scheduleLogRepo.getTaskLogById(type,id,tag);
			
			if(tag.equalsIgnoreCase("status")) {
				TaskLogDetails log = list.get(list.size() - 1);
				
				if(!log.getStatus().equals("Closed")) {
					
					TaskLogDetails new_log = new TaskLogDetails(null, null,null,null, null, null,null, null, "Yet to close",LocalDateTime.now(),null);
					list.add(new_log);
					
				}
			}
		}catch(Exception e) {
			System.out.println("Error in getTaskLogById : "+e.getMessage());
		}
		return list;
	}

	@Override
	public List<TaskDetails> getTaskListForTracking(Long project_id, Long employee_id) {
		List<TaskDetails> list = new ArrayList<>();
		try { 
             list = taskRepo.getTaskListForTracking(project_id,employee_id);
            
		}catch(Exception e) {
			System.out.println("Error in getTaskListForTracking : "+e.getMessage());
		}
		return list;
	}

	@Override
	public List<TaskDetails> getTaskListForOverview(Long employee_id,Long selected_emp_id, String fromdate, String todate) {
		List<TaskDetails> list = new ArrayList<>();
		try { 
			 if(fromdate == null || todate == null) {
				fromdate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).toString();
				todate = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)).toString();
             }
             list = taskRepo.getTaskListForOverview(employee_id,selected_emp_id,fromdate,todate);
		}catch(Exception e) {
			System.out.println("Error in getTaskListForOverview : "+e.getMessage());
		}
		return list;
	}

	

}
