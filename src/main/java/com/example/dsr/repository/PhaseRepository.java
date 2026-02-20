package com.example.dsr.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.dsr.DTO.PhaseDetails;
import com.example.dsr.model.Phase;

@Repository
public interface PhaseRepository extends JpaRepository<Phase, Long>{

	@Query(value = "select max(id) from phase",nativeQuery = true)
	int getMaxId();

	Phase getByCode(String code);

	@Query(nativeQuery = true)
	List<PhaseDetails> getPhaseByProject(Long project_id);

	List<Phase> findByProjectid(Long project_id);

	@Query(value = "select version from phase where id =:phaseid",nativeQuery = true)
	String getVersionById(Long phaseid);

	@Query(value = "SELECT COUNT(CASE WHEN p.isactive = 1 AND p.isdelete = 0 THEN 1 END) AS active_phase,COUNT(CASE WHEN p.isactive = 0 AND p.isdelete = 0 THEN 1 END) AS completed_phase FROM phase p left join project_master pm on p.projectid = pm.id left join project_members pms on pms.projectid = pm.id LEFT JOIN employee emp ON emp.id =:employee_id LEFT JOIN users u ON emp.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id where IF(r.role = 'ROLE_ADMIN',p.id != 0,IF(r.role = 'ROLE_MANAGER',pm.ispublic = 1 or pm.deptid = emp.deptid,FIND_IN_SET(emp.id, pms.employee_list) OR emp.id = pm.assigned_manager)) and (:project_id = 0 OR p.projectid =:project_id)",nativeQuery = true)
	Map<String, String> getPhaseCountForDashboardByRole(Long employee_id,Long project_id);

	@Query(value = "SELECT * FROM phase p left join project_master pm on p.projectid = pm.id left join project_members pms on pms.projectid = pm.id LEFT JOIN employee emp ON emp.id =:employee_id LEFT JOIN users u ON emp.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id where IF(r.role = 'ROLE_ADMIN',p.id != 0,IF(r.role = 'ROLE_MANAGER',pm.ispublic = 1 or pm.deptid = emp.deptid,FIND_IN_SET(emp.id, pms.employee_list) OR emp.id = pm.assigned_manager)) and (:project_id = 0 OR p.projectid =:project_id)",nativeQuery = true)
	List<Phase> getPhaseListForDashboard(Long employee_id,Long project_id);

//	@Query(value = "select p.phase_title,p.phase_type,'Phase' as type,p.end_date from phase p left join project_master pm on p.projectid = pm.id left join project_members pms on pms.projectid = pm.id LEFT JOIN employee emp ON emp.id =:employee_id LEFT JOIN users u ON emp.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id where IF(r.role = 'ROLE_ADMIN',p.id != 0,IF(r.role = 'ROLE_MANAGER',pm.ispublic = 1 or pm.deptid = emp.deptid,FIND_IN_SET(emp.id, pms.employee_list) OR emp.id = pm.assigned_manager)) and p.isactive = 1 and p.isdelete = 0 and p.end_date < date(now())",nativeQuery = true)
//	List<Map<String, String>> getOverDuePhaseListForDashboard(Long employee_id);
//
//	@Query(value = "select p.phase_title,p.phase_type,'Phase' as type,p.end_date from phase p left join project_master pm on p.projectid = pm.id left join project_members pms on pms.projectid = pm.id LEFT JOIN employee emp ON emp.id =:employee_id LEFT JOIN users u ON emp.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id where IF(r.role = 'ROLE_ADMIN',p.id != 0,IF(r.role = 'ROLE_MANAGER',pm.ispublic = 1 or pm.deptid = emp.deptid,FIND_IN_SET(emp.id, pms.employee_list) OR emp.id = pm.assigned_manager)) and p.isactive = 1 and p.isdelete = 0 and p.end_date = date(now())",nativeQuery = true)
//	List<Map<String, String>> getTodayDuePhaseListForDashboard(Long employee_id);

}
