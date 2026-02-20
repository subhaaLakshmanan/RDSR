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

import com.example.dsr.DTO.ProjectDetails;
import com.example.dsr.model.Project;
import com.example.dsr.repository.ProjectRepository;
import com.example.dsr.request.ProjectRequest;
import com.example.dsr.service.ProjectService;

@CrossOrigin(origins = "*", maxAge = 3600)  // Allowing all origins is risky. Consider restricting to trusted domains in production.
@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	ProjectRepository projectRepo;
	
	@PostMapping("/create")
	public ResponseEntity<?> createProject(@RequestBody ProjectRequest reqobj,HttpServletRequest servrequest){
		return projectService.createProject(reqobj,servrequest);
	}
	
	@GetMapping("/all")
	public List<ProjectDetails> getAllActiveProjects(@RequestParam Long employee_id){
		return projectService.collectActiveProjects(employee_id);
	}
	
	@PostMapping("/update")
	public ResponseEntity<?> updateProject(@RequestBody Project reqobj,HttpServletRequest servrequest){
		return projectService.updateProject(reqobj,servrequest);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteProject(@RequestParam String username,@RequestParam Long project_id,HttpServletRequest servrequest){
		return projectService.deleteProject(username,project_id,servrequest);
	}
	
	
	

	
	

}
