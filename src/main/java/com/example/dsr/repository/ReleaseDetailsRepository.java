package com.example.dsr.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.dsr.DTO.ReleaseDTO;
import com.example.dsr.model.ReleaseDetails;

public interface ReleaseDetailsRepository extends JpaRepository<ReleaseDetails, Long>{

	@Query(value = "select max(id) from release_details",nativeQuery = true)
	int getMaxId();

	ReleaseDetails getByCode(String code);

	@Query(value = "select * from release_details where projectid =:project_id and isdelete = 0",nativeQuery = true)
	List<ReleaseDetails> getAllActiveReleaseByProject(Long project_id);
	
	@Query(value = "SELECT COUNT(CASE WHEN re.release_type = 'Internal' AND re.isactive = 1 AND re.isdelete = 0 THEN 1 END) AS total_release,COUNT(CASE WHEN re.release_type = 'Internal' AND re.isactive = 1 AND re.isdelete = 0 AND re.status = 'Completed' THEN 1 END) AS passed_release,COUNT(CASE WHEN re.release_type = 'Internal' AND re.isactive = 1 AND re.isdelete = 0 AND re.status = 'Rejected' THEN 1 END) AS failed_release from release_details re left join project_master p on p.id = re.projectid left join project_members pm on pm.projectid = re.projectid left join employee emp on emp.id =:employee_id LEFT JOIN users u ON emp.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id  where if(r.role = 'ROLE_ADMIN',re.isdelete = 0,FIND_IN_SET(emp.id, pm.employee_list) or p.ispublic = 1 and re.isdelete = 0) and MONTH(re.released_date) = MONTH(CURDATE()) AND YEAR(re.released_date) = YEAR(CURDATE())",nativeQuery = true)
	Map<String, String> getReleaseCountForDashboardByRole(Long employee_id);

	@Query(nativeQuery = true)
	List<ReleaseDTO> getReleaseDetailsForDashboardByType(Long employee_id, String type);
	
	@Query(nativeQuery = true)
	List<ReleaseDTO> getReleaseDetailsForOverview(Long employee_id,Long projectid, String fromdate, String todate);

	@Query(value = "select re.*,p.project_title as project_name,concat(e.firstname,' ',e.lastname) as assignee_name from release_details re left join project_master p on re.projectid = p.id left join project_members pm on pm.projectid = p.id left join employee e on e.id = re.assigned_to left join employee emp on emp.id =:employee_id LEFT JOIN users u ON emp.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id where if(r.role = 'ROLE_ADMIN',re.isdelete = 0,FIND_IN_SET(emp.id, pm.employee_list) or p.ispublic = 1 and re.isdelete = 0) and MONTH(re.released_date) = MONTH(CURDATE()) AND YEAR(re.released_date) = YEAR(CURDATE()) and re.release_type = 'Internal'",nativeQuery = true)
	List<Map<String, String>> totalReleaseForThisMonth(Long employee_id);

	@Query(value = "select re.*,p.project_title as project_name,concat(e.firstname,' ',e.lastname) as assignee_name from release_details re left join project_master p on re.projectid = p.id left join project_members pm on pm.projectid = p.id left join employee e on e.id = re.assigned_to left join employee emp on emp.id =:employee_id LEFT JOIN users u ON emp.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id where if(r.role = 'ROLE_ADMIN',re.isdelete = 0,FIND_IN_SET(emp.id, pm.employee_list) or p.ispublic = 1 and re.isdelete = 0) and MONTH(re.released_date) = MONTH(CURDATE()) AND YEAR(re.released_date) = YEAR(CURDATE()) and re.release_type = 'Internal' and re.status = 'Completed'",nativeQuery = true)
	List<Map<String, String>> passedReleaseForThisMonth(Long employee_id);

	
	

}
