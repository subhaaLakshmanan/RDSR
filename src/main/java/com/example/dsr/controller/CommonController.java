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
import org.springframework.web.multipart.MultipartFile;

import com.example.dsr.DTO.AttendanceDTO;
import com.example.dsr.DTO.TaskLogDetails;
import com.example.dsr.model.AppType;
import com.example.dsr.model.Comments;
import com.example.dsr.model.Shift;
import com.example.dsr.repository.AppTypeRepository;
import com.example.dsr.repository.ShiftRepository;
import com.example.dsr.repository.StatusMasterRepository;
import com.example.dsr.service.CommentsService;
import com.example.dsr.service.DashboardService;
import com.example.dsr.service.TaskService;

@CrossOrigin(origins = "*", maxAge = 3600)  // Allowing all origins is risky. Consider restricting to trusted domains in production.
@RestController
@RequestMapping("/api/v1/common")
public class CommonController {
	
	@Autowired
	ShiftRepository shiftRepo;
	
	@Autowired
	AppTypeRepository AppTypeRepo;
	
	@Autowired
	StatusMasterRepository statusMasterRepo;
	
	@Autowired
	DashboardService dashboardService;
	
	@Autowired
	CommentsService commentsService;
	
	@Autowired
	TaskService taskService;
	
	@GetMapping("/getallshifts")
	public List<Shift> getAllShift(){
		return shiftRepo.getActiveList();
	}
	
	@GetMapping("/getapptypes")
	public List<AppType> getAllAppTypes(){
		return AppTypeRepo.getAllActiveAppTypes();
	}
	
	@GetMapping("/getstatuslist")
	public List<String> getStatusList(){
		return statusMasterRepo.getStatusList();
	}
	
	/** This method collects the dashboard details by employee role and project */
    @GetMapping("/dashboarddetails")
	public Map getDashBoardDetails(@RequestParam Long employee_id,@RequestParam Long project_id) {
		return dashboardService.getDashBoardDetails(employee_id,project_id);
	}
    
    @GetMapping("/gettasklog")
    public List<TaskLogDetails> getTaskLogById(@RequestParam String type,@RequestParam Long id,String tag){
    	return taskService.getTaskLogById(type,id,tag);
    }
    
    @PostMapping("/createcomments")
    public ResponseEntity<?> createComments(@RequestBody Comments reqobj,HttpServletRequest servrequest){
    	return commentsService.createComments(reqobj,servrequest);
    }
    
    @PostMapping("/updatecomments")
    public ResponseEntity<?> updateComments(@RequestBody Comments reqobj,HttpServletRequest servrequest){
    	return commentsService.updateComments(reqobj,servrequest);
    }
    
    @GetMapping("/getcommentsbyid")
    public List<Comments> getCommentsById(@RequestParam String type,@RequestParam Long id){
    	return commentsService.getCommentsById(type,id);
    }
    
    @DeleteMapping("/deletecomment")
    public ResponseEntity<?> deleteComments(@RequestParam Long id,@RequestParam String username,HttpServletRequest servrequest){
    	return commentsService.deleteComments(id,username,servrequest);
    }
    
    @GetMapping("/loadattendance")
    public ResponseEntity<?> loadAttendance(@RequestParam MultipartFile file){
    	return commentsService.loadAttendance(file);
    }

    
    @GetMapping("/attendancedetails")
    public List<AttendanceDTO> getAttendance(
    		@RequestParam Long id,@RequestParam String fromdate,@RequestParam String todate) {
        return commentsService.getAttendance(id,fromdate,todate);
               
    }
    
}
