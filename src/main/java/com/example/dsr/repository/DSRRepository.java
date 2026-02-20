package com.example.dsr.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.dsr.DTO.DsrDetails;
import com.example.dsr.model.DSR;

@Repository
public interface DSRRepository extends JpaRepository<DSR, Long>{

	
    @Query(value = "select * from dsr where isdelete = 0 and subtaskid =:sub_task_id order by id desc",nativeQuery = true)
	List<DSR> getActiveDSRBySubTask(Long sub_task_id);

	@Query(value = "select count(id) from dsr where isdelete = 0 and subtaskid =:subtaskid and date =:date",nativeQuery = true)
	int getDateCount(LocalDate date, Long subtaskid);

	@Query(value = "select count(id) from dsr where isdelete = 0 and subtaskid =:subtaskid and date =:date and id <> :id",nativeQuery = true)
	int getDateCountById(LocalDate date, Long subtaskid, Long id);

	@Query(value = "update dsr set isdelete = 1,updateddate = now() where id =:dsr_id",nativeQuery = true)
	void deleteDSRById(Long dsr_id);

	@Query(value = "select sum(worked_hours) from dsr where subtaskid =:subtaskid and isdelete = 0 and if(:id != 0,id <> :id,id != 0)",nativeQuery = true)
	int getTotalWorkingHoursForSubTask(Long subtaskid, Long id);

	@Query(nativeQuery = true)
	List<DsrDetails> getDsrOverviewDetails(Long employee_id, String from_date, String to_date,Long user_id);
	
	@Query(value = "select max(d.date) from dsr d left join sub_task s on d.subtaskid = s.id left join employee e on e.id = s.employeeid left join employee emp on emp.id =:user_id LEFT JOIN users u ON emp.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id where d.isdelete = 0 and if(r.role = 'ROLE_ADMIN',d.isdelete = 0,e.deptid = emp.deptid)",nativeQuery = true)
	String getMaxDateByUserId(Long user_id);

//	@Query(value = "select max(d.date) from dsr d left join sub_task s on d.subtaskid = s.id left join employee e on s.employeeid = e.id where e.deptid =:dept_id",nativeQuery = true)
//	String getMaxDateByDepartmentId(Long dept_id);
	
//	@Query(value = "select sum(worked_hours) from dsr where subtaskid =:subtaskid and isdelete = 0",nativeQuery = true)
//	int getTotalWorkingHoursForSubTask(Long subtaskid);

}
