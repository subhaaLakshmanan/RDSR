package com.example.dsr.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.example.dsr.DTO.ProjectDetails;
import com.example.dsr.model.Project;
import com.example.dsr.request.ProjectRequest;

public interface ProjectService {

	ResponseEntity<?> createProject(ProjectRequest reqobj,HttpServletRequest servrequest);

    ResponseEntity<?> updateProject(Project reqobj, HttpServletRequest servrequest);

	ResponseEntity<?> deleteProject(String username, Long project_id, HttpServletRequest servrequest);

	List<ProjectDetails> collectActiveProjects(Long employee_id);



}
