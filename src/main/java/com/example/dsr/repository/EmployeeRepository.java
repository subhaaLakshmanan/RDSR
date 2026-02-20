package com.example.dsr.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.dsr.DTO.EmployeeDetails;
import com.example.dsr.model.Employee;
import com.example.dsr.model.Project;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	Employee getByEmail(String email);

//	@Query(value = "SELECT e.*,d.department_name,p.position,s.shift_type,REPLACE(e.image_url,'/var/www/html',(SELECT link_url FROM server_details WHERE isactive = 1 ORDER BY id DESC LIMIT 1)) AS profile_image_url FROM employee e LEFT JOIN department_master d ON e.deptid = d.id LEFT JOIN position_master p ON p.id = e.positionid LEFT JOIN shift_master s ON s.id = e.shiftid WHERE e.isdelete = 0",nativeQuery = true)
//	@Query(value = "SELECT e.*,d.department_name,p.position,s.shift_type,r.role,REPLACE(e.image_url,'/var/www/html',(SELECT link_url FROM server_details WHERE isactive = 1 ORDER BY id DESC LIMIT 1)) AS profile_image_url FROM employee e LEFT JOIN department_master d ON e.deptid = d.id LEFT JOIN position_master p ON p.id = e.positionid LEFT JOIN shift_master s ON s.id = e.shiftid left join users u on e.email = u.email left join user_roles ur on u.id = ur.user_id left join roles r on ur.role_id = r.id WHERE e.isdelete = 0",nativeQuery = true)
//	@Query(value = "SELECT e.*,DATE_FORMAT(e.createddate, '%Y-%m-%dT%H:%i:%s') AS created_date,DATE_FORMAT(e.updateddate, '%Y-%m-%dT%H:%i:%s') AS updated_date,d.department_name,p.position,s.shift_type,r.role,REPLACE(e.image_url, '/var/www/html', (SELECT link_url FROM server_details WHERE isactive = 1 ORDER BY id DESC LIMIT 1)) AS profile_image_url FROM employee e LEFT JOIN department_master d ON e.deptid = d.id LEFT JOIN position_master p ON p.id = e.positionid LEFT JOIN shift_master s ON s.id = e.shiftid LEFT JOIN users u ON e.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id WHERE e.isdelete = 0",nativeQuery = true)
	@Query(nativeQuery = true)
	List<EmployeeDetails> getEmployeeDetails();


	@Query(value = "select count(id) from employee where email =:email and isdelete = 0",nativeQuery = true)
	int getMailIdCount(String email);
	
	@Query(value = "select count(id) from employee where email =:email and id <> :id and isdelete = 0",nativeQuery = true)
	int getMailIdCountByEmployeeId(Long id, String email);

	@Query(value = "select count(id) from employee where mobile =:mobile and isdelete = 0",nativeQuery = true)
	int getMobileNoCount(String mobile);
	
	@Query(value = "select count(id) from employee where mobile =:mobile and id <> :id and isdelete = 0",nativeQuery = true)
	int getMobileNoCountByEmployeeId(Long id, String mobile);

	@Query(value = "select count(id) from employee where attendanceid =:attendanceid and isdelete = 0",nativeQuery = true)
	int getAttendanceIdCount(Long attendanceid);
	
	@Query(value = "select count(id) from employee where attendanceid =:attendanceid and id <> :id and isdelete = 0",nativeQuery = true)
	int getAttendanceIdCountByEmployeeId(Long id, Long attendanceid);

	@Query(value = "select e.id,concat(e.firstname,' ',e.lastname) as employee_name from employee e left join users u on e.email = u.email left join user_roles ur on u.id = ur.user_id left join roles r on ur.role_id = r.id where r.role = 'ROLE_MANAGER' and isactive = 1 and isdelete = 0",nativeQuery = true)
	List<Map<String, String>> getManagerList();

	@Query(value = "select e.id,concat(e.firstname,' ',e.lastname) as employee_name,pm.position from employee e left join users u on e.email = u.email left join user_roles ur on u.id = ur.user_id left join roles r on ur.role_id = r.id left join position_master pm on e.positionid = pm.id where r.role <> 'ROLE_ADMIN' and e.isactive = 1 and e.isdelete = 0",nativeQuery = true)
	List<Map<String, String>> getEmployeeList();

	@Query(value = "select concat(firstname,' ',lastname) as employee_name from employee where id =:id",nativeQuery = true)
	String getNameById(Long id);
	
	@Query(value = "select email from employee where id =:id",nativeQuery = true)
	String getMailById(Long id);

	@Query(nativeQuery = true)
	List<EmployeeDetails> getProjectMemberDetails(Long project_id);

	@Query(value = "select e.id,concat(e.firstname,' ',e.lastname) as employee_name from employee e left join users u on e.email = u.email left join user_roles ur on u.id = ur.user_id left join roles r on ur.role_id = r.id left join project_master p on p.id = :project_id where e.isactive = 1 and e.isdelete = 0 and if(p.ispublic = 1,r.role <> 'ROLE_ADMIN',p.deptid = e.deptid)",nativeQuery = true)
	List<Map<String, String>> getEmployeeListByProject(Long project_id);

	@Query(value = "select e.id,concat(e.firstname,' ',e.lastname) as employee_name from employee e left join project_master p on p.id =:project_id left join project_members pm on p.id = pm.projectid where e.isactive = 1 and e.isdelete = 0 and FIND_IN_SET(e.id, pm.employee_list)",nativeQuery = true)
	List<Map<String, String>> getAssignedEmployeeListByProject(Long project_id);

//	@Query(value = "SELECT COUNT(CASE WHEN e.isactive = 1 AND e.isdelete = 0 THEN 1 END) AS active_employees,COUNT(CASE WHEN e.isactive = 0 AND e.isdelete = 0 THEN 1 END) AS deactive_employees FROM employee e LEFT JOIN employee emp ON emp.id =:employee_id LEFT JOIN users u ON emp.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id where IF(r.role = 'ROLE_ADMIN',e.id != 0,emp.deptid = e.deptid)",nativeQuery = true)
	@Query(value = "SELECT COUNT(CASE WHEN e.isactive = 1 AND e.isdelete = 0 THEN 1 END) AS active_employees,COUNT(CASE WHEN e.isactive = 0 AND e.isdelete = 0 THEN 1 END) AS deactive_employees FROM employee e",nativeQuery = true)
    Map<String, String> getEmployeeCountForDashboardByRole();

	@Query(value = "select email from employee where FIND_IN_SET(id,(select employee_list from project_members where projectid =:project_id))",nativeQuery = true)
	List<String> getEmployeeMailsByProject(Long project_id);

	@Query(value = "select count(reference_no) from employee where reference_no =:reference_no and id <> :id",nativeQuery = true)
	int getCountByReferenceNoAndEmployeeId(Long reference_no, Long id);

	
	

	

}
