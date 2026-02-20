package com.example.dsr.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.dsr.DTO.EmployeeDetails;
import com.example.dsr.model.Employee;
import com.example.dsr.repository.EmployeeRepository;
import com.example.dsr.request.UserCreateRequest;
import com.example.dsr.service.UserService;


@CrossOrigin(origins = "*", maxAge = 3600)  // Allowing all origins is risky. Consider restricting to trusted domains in production.
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	@Autowired
	EmployeeRepository employeeRepo;
	
	@Autowired
	UserService userService;
	
	/** Api for user creation **/
	@PostMapping("/create")
	public ResponseEntity<?> createUser(@RequestBody UserCreateRequest reqobj,HttpServletRequest servrequest){
		return userService.createUser(reqobj,servrequest);
	}
	
	/** Api for user details edit option **/
	@PostMapping("/update")
	public ResponseEntity<?> updateUser(@RequestBody Employee reqobj,HttpServletRequest servrequest){
		return userService.updateUser(reqobj,servrequest);
	}
	
	/** Api for collect user details for manager and employee roles **/
	@GetMapping("/all")
	public List<EmployeeDetails> getAllEmployee(){
		return employeeRepo.getEmployeeDetails();
	}
	
	/** Api for delete user details **/
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteUser(@RequestParam Long employee_id,@RequestParam String username,HttpServletRequest servrequest){
		return userService.deleteUser(employee_id,username,servrequest);
	}
	
	/** Api for collect employee name and id only for manager roles **/
	@GetMapping("/getmanagerlist")
	public List<Map<String,String>> getManagerList(){
		return employeeRepo.getManagerList();
	}
	
	/** Api for collect employee name and id for manager & employee roles **/
	@GetMapping("/getemployeelist")
	public List<Map<String,String>> getEmployeeList(){
		return employeeRepo.getEmployeeList();
	}
	
	/** collect project members details based on the project id **/
	@GetMapping("/getprojectmembers")
	public List<EmployeeDetails> getProjectMemberDetails(@RequestParam Long project_id){
		return employeeRepo.getProjectMemberDetails(project_id);
	}
	
	/** api for employee name drop down in project module **/
	/** It includes the assigned employee list for the project, project details, and details of all employees except those with the admin role. **/
	@GetMapping("/getemployeelistbyproject")
	public Map getEmployeeListByProject(@RequestParam Long project_id){
		return userService.getEmployeeListByProject(project_id);
	}
	
//	@GetMapping("/adduserbyxlfile")
//	public String addUserByXLFile(@RequestParam @ModelAttribute MultipartFile file,HttpServletRequest servrequest) {
//		return userService.addUserByXLFile(file,servrequest);
//	}
	
	@PostMapping("/releaveEmployee")
	public ResponseEntity<?> updatereleaveemployee(@RequestParam Long employeeid,@RequestParam String username,@RequestParam String reason,@RequestParam String releave_date,HttpServletRequest servrequest){
		return userService.updatereleaveemployee(employeeid,username,reason,releave_date,servrequest);
	}
	
}
