package com.example.dsr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.dsr.model.ProjectMembers;

public interface ProjectMembersRepository extends JpaRepository<ProjectMembers, Long>{

	@Query(value = "select employee_list from project_members where projectid =:projectId",nativeQuery = true)
	String getEmployeeListByProjectId(Long projectId);

	@Query(value = "update project_members set employee_list =:employee_list,updated_date = now() where projectid =:id",nativeQuery = true)
	void updateEmployeeListForProject(String employee_list, Long id);

	

}
