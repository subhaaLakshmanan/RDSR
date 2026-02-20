package com.example.dsr.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dsr.DTO.PhaseDetails;
import com.example.dsr.model.Employee;
import com.example.dsr.model.Phase;
import com.example.dsr.model.ScheduleLog;
import com.example.dsr.model.Task;
import com.example.dsr.repository.EmployeeRepository;
import com.example.dsr.repository.PhaseRepository;
import com.example.dsr.repository.ProjectRepository;
import com.example.dsr.repository.ScheduleLogRepository;
import com.example.dsr.repository.UserRepository;
import com.example.dsr.responce.MessageResponse;
import com.example.dsr.service.LogService;
import com.example.dsr.service.PhaseService;

@Service
public class PhaseServiceImplementation implements PhaseService{
	
	@Autowired
	PhaseRepository phaseRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	ScheduleLogRepository scheduleLogRepo;
	
	@Autowired
	LogService logService;
	
	@Autowired
	EmployeeRepository employeeRepo;
	
	@Autowired
	ProjectRepository projectRepo;

	@Override
	public ResponseEntity<?> createPhase(Phase reqobj, HttpServletRequest servrequest) {
		try {
            String remoteAddress = servrequest.getRemoteAddr();
			
            ResponseEntity responce = PhaseValidation(reqobj);

			MessageResponse message = (MessageResponse) responce.getBody();
			
		    if (!message.getMessage().equals("Success")) {
				return responce;
			}
			
			long max_id = 0;
			try {
				max_id = phaseRepo.getMaxId() + 1;
			}catch(Exception e) {
				max_id = max_id + 1;
			}
			
			String code = String.format("%06d", max_id);
			reqobj.setCode("PHS" + code);
			
			reqobj.setIsactive(true);
			phaseRepo.save(reqobj);
			
			Phase phase = phaseRepo.getByCode(reqobj.getCode());
			
			Employee employee = employeeRepo.getByEmail(reqobj.getUsername());
			
			ScheduleLog sch_log = new ScheduleLog(employee.getId(),null,null,phase.getId(),null,"Phase Creation","A phase has been created for the project "+projectRepo.getProjectNameById(reqobj.getProjectid())+" by "+employee.getFirstname()+" "+employee.getLastname(),phase.getStatus());
			scheduleLogRepo.save(sch_log);
			
			ScheduleLog sch_log1 = new ScheduleLog(employee.getId(), null,null,phase.getId(),null,"Phase Creation","Status changed, Phase opened by "+employee.getFirstname()+" "+employee.getLastname(),reqobj.getStatus());
			scheduleLogRepo.save(sch_log1);
			
			logService.ActivityLog(reqobj.getUsername(), "Phase Creation", remoteAddress, reqobj.getCode());
			
		}catch(Exception e) {
			System.out.println("Error in createPhase : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
		return ResponseEntity.ok(new MessageResponse("Created successfully!"));
	}
	
	@Override
	public ResponseEntity<?> updatePhase(Phase reqobj, HttpServletRequest servrequest) {
		try {
            String remoteAddress = servrequest.getRemoteAddr();
			
            ResponseEntity responce = PhaseValidation(reqobj);

			MessageResponse message = (MessageResponse) responce.getBody();
			
		    if (!message.getMessage().equals("Success")) {
				return responce;
			}
		    
		    Phase old_phase = phaseRepo.getByCode(reqobj.getCode());
		    
		    if(reqobj.getStatus().equals("Closed")) {
		    	reqobj.setIsactive(false);
		    }else {
		    	reqobj.setIsactive(true);
		    }
		    
		    Employee employee = employeeRepo.getByEmail(reqobj.getUsername());
			String createdby = employee.getFirstname()+" "+employee.getLastname();
			
			String remarks = null;
			
			if(!old_phase.getEnd_date().equals(reqobj.getEnd_date()) || !old_phase.getStart_date().equals(reqobj.getStart_date())){ 
				remarks = "Phase rescheduled by "+createdby;
			}else if(!old_phase.getStatus().equals("Open") && reqobj.getStatus().equals("Open")) {
				remarks = "Status changed, Phase re-opened by "+createdby;
			}else if(!old_phase.getStatus().equals("Closed") && reqobj.getStatus().equals("Closed")) {
				remarks = "Status changed, Phase closed by "+createdby;
			}else if(!old_phase.getStatus().equals(reqobj.getStatus())) {
				remarks = "Status changed, Phase status changed from "+old_phase.getStatus()+" to "+reqobj.getStatus()+" by "+createdby;
			}else if(reqobj.getAssignee() != null) {
				String assignee_name = employeeRepo.getNameById(reqobj.getAssignee());
				
				if(old_phase.getAssignee() == null && reqobj.getAssignee() != null) {
					remarks = "Phase assigned to "+assignee_name+" by "+createdby;
				}else if((!old_phase.getStatus().equals("Open") && reqobj.getStatus().equals("Open") && old_phase.getAssignee() != reqobj.getAssignee())) {
					remarks = "Status changed, Phase re-opened and reassigned to "+assignee_name+" by "+createdby;
				}else if(old_phase.getAssignee() != null && old_phase.getAssignee() != reqobj.getAssignee()) {
					remarks = "Phase reassigned to "+assignee_name+" by "+createdby;
				}else{
					remarks = "Phase data edited by "+createdby;
	            }
			}else{
				remarks = "Phase data edited by "+createdby;
            }
		  
		    phaseRepo.save(reqobj);
			
			Phase new_phase = phaseRepo.getByCode(reqobj.getCode());
			
			ScheduleLog sch_log = new ScheduleLog(employee.getId(),null,null,new_phase.getId(),null,"Edit Phase Details",remarks,new_phase.getStatus());
			scheduleLogRepo.save(sch_log);
			
			logService.ActivityLog(reqobj.getUsername(), "Edit Phase Details", remoteAddress, reqobj.getCode());
			
		}catch(Exception e) {
			System.out.println("Error in updatePhase : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
		return ResponseEntity.ok(new MessageResponse("Updated successfully!"));
	}

	private ResponseEntity PhaseValidation(Phase reqobj) {
		try {
			
			if(!userRepo.existsByEmail(reqobj.getUsername())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized User!"));
			}
			
			if(reqobj.getProjectid() == null || reqobj.getProjectid() == 0) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid project details!"));
			}
			
			reqobj.setPhase_title(reqobj.getPhase_title().trim());
			
			if(reqobj.getPhase_title() == null || reqobj.getPhase_title().isEmpty()){
					return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid Phase title!"));
		    }
			
            if(reqobj.getPhase_type() == null || reqobj.getPhase_type().isEmpty()){
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid Phase type!"));
			}
            
            if(reqobj.getStatus() == null || reqobj.getStatus().trim().equals("")) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid phase status!"));
			}
            
            if (reqobj.getStart_date().isAfter(reqobj.getEnd_date())) {
        	    return ResponseEntity.badRequest().body(new MessageResponse("Error: Start date cannot be after end date"));
        	}
        	
		}catch(Exception e) {
			System.out.println("Error in PhaseValidation : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
		return ResponseEntity.ok(new MessageResponse("Success"));
	}

	@Override
	public ResponseEntity<?> deletePhase(String username, Long phase_id, HttpServletRequest servrequest) {
		try {
            String remoteAddress = servrequest.getRemoteAddr();
			
			if(!userRepo.existsByEmail(username)) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized User!"));
			}
			
			Phase phase = phaseRepo.getById(phase_id);
			phase.setIsdelete(true);
			phaseRepo.save(phase);
			
			Employee employee = employeeRepo.getByEmail(username);
			
			ScheduleLog sch_log = new ScheduleLog(employee.getId(),null,null,phase_id,null,"Delete Phase Schedule",phase.getCode(),phase.getStatus());
			scheduleLogRepo.save(sch_log);
			
            logService.ActivityLog(username, "Delete Phase",remoteAddress,phase.getCode());
			
			return ResponseEntity.ok(new MessageResponse("Deleted Successfully!"));
		}catch(Exception e) {
			System.out.println("Error in deletePhase : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
	}

	

	

}
