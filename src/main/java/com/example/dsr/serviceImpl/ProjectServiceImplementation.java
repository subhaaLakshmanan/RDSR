package com.example.dsr.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.dsr.DTO.ProjectDetails;
import com.example.dsr.model.Project;
import com.example.dsr.model.ProjectMembers;
import com.example.dsr.repository.DepartmentRepository;
import com.example.dsr.repository.EmployeeRepository;
import com.example.dsr.repository.ProjectMembersRepository;
import com.example.dsr.repository.ProjectRepository;
import com.example.dsr.repository.UserRepository;
import com.example.dsr.request.ProjectRequest;
import com.example.dsr.responce.MessageResponse;
import com.example.dsr.service.LogService;
import com.example.dsr.service.ProjectService;

@Service
public class ProjectServiceImplementation implements ProjectService{

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	ProjectRepository projectRepo;
	
	@Autowired
	ProjectMembersRepository projectMembersRepo;
	
	@Autowired
	DepartmentRepository departmentRepo;
	
	@Autowired
	EmployeeRepository employeeRepo;
	
	@Autowired
	LogService logService;
	
	@Override
	public ResponseEntity<?> createProject(ProjectRequest reqobj,HttpServletRequest servrequest) {
		try {
            String remoteAddress = servrequest.getRemoteAddr();
            
            reqobj.setId((long) 0);
            ResponseEntity responce = projectValidation(reqobj);

			MessageResponse message = (MessageResponse) responce.getBody();
			
		    // If address validation fails → return error response
			if (!message.getMessage().equals("Success")) {
				return responce;
			}
			
			Project project = new Project(reqobj.getProject_title(), reqobj.getDescription(), reqobj.getDeptid(), reqobj.getStart_date(), reqobj.getEnd_date(),
					reqobj.getAssigned_manager(),reqobj.getClient(),true, false,false,reqobj.isIspublic());
			
			projectRepo.save(project);
			
			project = projectRepo.getByProjectTitle(reqobj.getProject_title());
			
			String employee_list = reqobj.isIspublic() ? "":reqobj.getEmployee_list().stream().map(String::valueOf).collect(Collectors.joining(","));
			
            ProjectMembers obj = new ProjectMembers(project.getId(),employee_list);
			projectMembersRepo.save(obj);
			
			logService.ActivityLog(reqobj.getUsername(),"Project Creation",remoteAddress,reqobj.getProject_title());
			
		}catch(Exception e) {
			System.out.println("Error in project create : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
		return ResponseEntity.ok(new MessageResponse("Created successfully!"));
	}
	
	@Override
	public ResponseEntity<?> updateProject(Project reqobj, HttpServletRequest servrequest) {
		try {
            String remoteAddress = servrequest.getRemoteAddr();
            
            ProjectRequest obj = new ProjectRequest(reqobj.getId(), reqobj.getProject_title(),reqobj.getDeptid(), reqobj.getStart_date(), reqobj.getEnd_date(),
            		reqobj.getAssigned_manager(),reqobj.getClient(), reqobj.isIspublic(), reqobj.getEmployee_list(), reqobj.getUsername());
            
            ResponseEntity responce = projectValidation(obj);

			MessageResponse message = (MessageResponse) responce.getBody();
			
		    // If address validation fails → return error response
			if (!message.getMessage().equals("Success")) {
				return responce;
			}
			 
			projectRepo.save(reqobj);
			 
            String employee_list = reqobj.getEmployee_list().stream().map(String::valueOf).collect(Collectors.joining(","));
            
            projectMembersRepo.updateEmployeeListForProject(employee_list,reqobj.getId());
            
            logService.ActivityLog(reqobj.getUsername(),"Edit Project Details",remoteAddress,reqobj.getProject_title());
			 
		}catch(Exception e) {
			System.out.println("Error in project update : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
		return ResponseEntity.ok(new MessageResponse("Edited successfully!"));
	}
	
	public ResponseEntity<?> projectValidation (ProjectRequest reqobj) {
        try {
        	if(!userRepo.existsByEmail(reqobj.getUsername())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized user!"));
			}
        	
        	reqobj.setProject_title(reqobj.getProject_title().trim());
        	
        	if(reqobj.getProject_title() == null || reqobj.getProject_title().isEmpty()){
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Enter the valid project title!"));
			}else {
				int project_count = reqobj.getId() == 0 ? projectRepo.getCountByProjectName(reqobj.getProject_title()): projectRepo.getCountByProjectNameAndId(reqobj.getProject_title(),reqobj.getId());
				if(project_count > 0) {
					return ResponseEntity.badRequest().body(new MessageResponse("Error: Project already exists!"));
				}
			}
        	
        	if (reqobj.getStart_date().isAfter(reqobj.getEnd_date())) {
        	    return ResponseEntity.badRequest().body(new MessageResponse("Error: Start date cannot be after end date"));
        	}
        	
        	if(reqobj.getAssigned_manager() == null || reqobj.getAssigned_manager() == 0) {
        		return ResponseEntity.badRequest().body(new MessageResponse("Error: Select the valid manager name!"));
        	}
        	
        	if(reqobj.getClient() == null || reqobj.getClient().isEmpty()){
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Enter the valid client name!"));
			}
        	
        	if(!reqobj.isIspublic()) {
        		if(reqobj.getDeptid() == null || reqobj.getDeptid() == 0) {
            		return ResponseEntity.badRequest().body(new MessageResponse("Error: Select the valid department name!"));
            	}else {
            		if(reqobj.getEmployee_list().isEmpty()) {
            			return ResponseEntity.badRequest().body(new MessageResponse("Error: Select an employee for the project!"));
            		}
            	}
        	}
        }catch(Exception e) {
			System.out.println("Error in project validation : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
        return ResponseEntity.ok(new MessageResponse("Success"));
	}
	
//	@Override
//	public List<Project> getAllActiveProjects() {
//		List<Project> project_list = new ArrayList<>();
//		try {
//			List<Project> list = projectRepo.findAll();
//			
//			project_list =  list.stream().filter(project -> !project.isIsdelete())
//					.peek(project -> {
//						project.setDepartment(project.getDeptid() != null ? departmentRepo.getNameById(project.getDeptid()) : "");
//						project.setManager_name(employeeRepo.getNameById(project.getAssigned_manager()));
//	                    String empStr = projectMembersRepo.getEmployeeListByProjectId(project.getId());  
//	                    
//	                    if (empStr != null && !empStr.isEmpty()) {
//	                        List<Long> empList = Arrays.stream(empStr.split(","))
//	                                .map(Long::valueOf)
//	                                .collect(Collectors.toList());
//	                        project.setEmployee_list(empList);    
//	                    } else {
//	                    	project.setEmployee_list(new ArrayList<>()); 
//	                    }
//	                }).collect(Collectors.toList());
//		}catch(Exception e) {
//			System.out.println("Error in getAllActiveProjects : "+e.getMessage());
//		}
//		return project_list;
//	}
	
	@Override
	public List<ProjectDetails> collectActiveProjects(Long employee_id) {
		List<ProjectDetails> project_list = new ArrayList<>();
		try {
			List<ProjectDetails> list = projectRepo.collectActiveProjects(employee_id);
			
			project_list =  list.stream()
					.peek(project -> {
						
	                    String empStr = project.getEmployees();  
	                    
	                    if (empStr != null && !empStr.isEmpty()) {
	                        List<Long> empList = Arrays.stream(empStr.split(","))
	                        		.map(Long::valueOf)
	                                .collect(Collectors.toList());
	                        project.setEmployee_list(empList);  
	                    } else {
	                    	project.setEmployee_list(new ArrayList<>()); 
	                    }
	                }).collect(Collectors.toList());
		}catch(Exception e) {
			System.out.println("Error in collectActiveProjects : "+e.getMessage());
		}
		return project_list;
	}

	@Override
	public ResponseEntity<?> deleteProject(String username, Long project_id, HttpServletRequest servrequest) {
		try {
            String remoteAddress = servrequest.getRemoteAddr();
			
			if(!userRepo.existsByEmail(username)) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized User!"));
			}
			
			Project project = projectRepo.getById(project_id);
			project.setIsdelete(true);
			projectRepo.save(project);

			logService.ActivityLog(username, "Delete Project",remoteAddress,project.getProject_title());
			
			return ResponseEntity.ok(new MessageResponse("Deleted Successfully!"));
		}catch(Exception e) {
			System.out.println("Error in delete Project : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
	}

	



}
