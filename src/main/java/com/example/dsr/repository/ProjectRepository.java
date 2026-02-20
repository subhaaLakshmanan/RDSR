package com.example.dsr.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.dsr.DTO.ProjectDetails;
import com.example.dsr.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>{

	@Query(value = "select * from project_master where project_title =:project_title and isdelete = 0",nativeQuery = true)
	Project getByProjectTitle(String project_title);

	@Query(value = "select count(id) from project_master where project_title =:project_title and isdelete = 0",nativeQuery = true)
	int getCountByProjectName(String project_title);
	
	@Query(value = "select count(id) from project_master where project_title =:project_title and isdelete = 0 and id <> :id",nativeQuery = true)
	int getCountByProjectNameAndId(String project_title, Long id);

	@Query(value = "select project_title from project_master where id =:projectid",nativeQuery = true)
	String getProjectNameById(Long projectid);

	@Query(nativeQuery = true)
	List<ProjectDetails> collectActiveProjects(Long employee_id);

	@Query(value = "SELECT COUNT(CASE WHEN p.isactive = 1 AND p.isdelete = 0 AND p.isclose = 0 THEN 1 END) AS active_project,COUNT(CASE WHEN p.isactive = 1 AND p.isdelete = 0 AND p.isclose = 1 THEN 1 END) AS completed_project,COUNT(CASE WHEN p.isdelete = 1 THEN 1 END) AS dropped_projects FROM project_master p LEFT JOIN project_members pm ON p.id = pm.projectid LEFT JOIN employee emp ON emp.id =:employee_id LEFT JOIN users u ON emp.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id WHERE (r.role = 'ROLE_ADMIN' OR (r.role = 'ROLE_MANAGER' AND (p.ispublic = 1 OR p.deptid = emp.deptid)) OR (r.role = 'ROLE_EMPLOYEE'AND (p.ispublic = 1 OR FIND_IN_SET(emp.id, pm.employee_list)OR emp.id = p.assigned_manager)))",nativeQuery = true)
	Map<String, String> getProjectCountForDashboardByRole(Long employee_id);

	@Query(value = "SELECT COUNT(CASE WHEN p.isactive = 1 AND p.isdelete = 0 AND p.isclose = 0 THEN 1 END) AS active_project,COUNT(CASE WHEN p.isactive = 1 AND p.isdelete = 0 AND p.isclose = 1 THEN 1 END) AS completed_project,COUNT(CASE WHEN p.isdelete = 1 THEN 1 END) AS dropped_projects FROM project_master p where p.ispublic = 1",nativeQuery = true)
	Map<String, String> getProjectCountForPublicProjects();

	@Query(value = "SELECT COUNT(CASE WHEN p.isactive = 1 AND p.isdelete = 0 AND p.isclose = 0 THEN 1 END) AS active_project,COUNT(CASE WHEN p.isactive = 1 AND p.isdelete = 0 AND p.isclose = 1 THEN 1 END) AS completed_project,COUNT(CASE WHEN p.isdelete = 1 THEN 1 END) AS dropped_projects FROM project_master p where p.deptid =:department_id and p.ispublic = 0",nativeQuery = true)
	Map<String, String> getProjectCountByDepartment(Long department_id);

	

}
