package com.example.dsr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dsr.model.Department;
import com.example.dsr.model.Designation;
import com.example.dsr.model.Shift;
import com.example.dsr.repository.DepartmentRepository;
import com.example.dsr.repository.DesignationRepository;

@CrossOrigin(origins = "*", maxAge = 3600)  // Allowing all origins is risky. Consider restricting to trusted domains in production.
@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {
	
	@Autowired
	DepartmentRepository departmentRepo;
	
	@Autowired
	DesignationRepository designationRepo;

	@GetMapping("/getalldepartments")
	public List<Department> getAllDepartments(){
		return departmentRepo.getActiveList();
	}
	
	@GetMapping("/designationbydepartment")
	public List<Designation> getDesignationByDepartment(@RequestParam Long department_id){
		return designationRepo.getDesignationByDepartment(department_id);
	}
	
	
}
