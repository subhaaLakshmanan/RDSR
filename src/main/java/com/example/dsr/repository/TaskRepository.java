package com.example.dsr.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.dsr.DTO.TaskDetails;
import com.example.dsr.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

	@Query(value = "select max(id) from task",nativeQuery = true)
	long getMaxId();

	@Query(value = "select * from task where taskcode =:taskcode",nativeQuery = true)
	Task getTaskByTaskcode(String taskcode);

    @Query(nativeQuery = true)
	List<TaskDetails> getTaskListByEmployee(Long project_id,Long employee_id,Long phaseid,String task_type,String fromdate,String todate);

//    @Query(value = "update task t set t.completion_percentage = (SELECT AVG(s.completion_percentage) FROM sub_task s where s.taskid = t.id and s.isdelete = 0),updated_date = now() where t.id =:taskid",nativeQuery = true)
//    @Query(value = "UPDATE task t JOIN sub_task st ON st.taskid = t.id SET t.completion_percentage = (SELECT AVG(s.completion_percentage) FROM sub_task s WHERE s.taskid = t.id AND s.isdelete = 0),t.updated_date = NOW() WHERE st.id =:taskid",nativeQuery = true)
    @Query(value = "UPDATE task t SET t.completion_percentage = (SELECT AVG(s.completion_percentage) FROM sub_task s WHERE s.taskid = t.id AND s.isdelete = 0),t.worked_hours = IFNULL((SELECT SEC_TO_TIME(SUM(TIME_TO_SEC(s.worked_hours))) FROM sub_task s WHERE s.taskid = t.id AND s.isdelete = 0), '00:00:00'),t.updated_date = NOW() where t.id =:taskid",nativeQuery = true)
    void updateCompletionPercentageForTask(Long taskid);

    @Query(value = "update task set phaseid =:phase_id,version = (select version from phase where id =:phase_id),updated_date = now() where id in :task_list",nativeQuery = true)
	void swapTaskBetweenPhases(Long phase_id, List<Long> task_list);

    @Query(value = "SELECT COUNT(CASE WHEN t.task_type = 'Requirement' AND t.isactive = 1 AND t.isdelete = 0 THEN 1 END) AS active_task,COUNT(CASE WHEN t.task_type = 'Requirement' AND t.isactive = 0 AND t.isdelete = 0 THEN 1 END) AS closed_task,COUNT(CASE WHEN t.task_type = 'Bug' AND t.isactive = 1 AND t.isdelete = 0 THEN 1 END) AS active_issue,COUNT(CASE WHEN t.task_type = 'Bug' AND t.isactive = 0 AND t.isdelete = 0 THEN 1 END) AS closed_issue,r.role FROM task t left join employee emp on emp.id =:employee_id LEFT JOIN users u ON emp.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id where if(r.role = 'ROLE_EMPLOYEE',t.assigned_to = emp.id,if(r.role = 'ROLE_MANAGER',FIND_IN_SET(t.projectid,(select group_concat(id) from project_master where if(ispublic,id != 0,deptid = emp.deptid))),t.id != 0)) and (:project_id = 0 OR t.projectid =:project_id)",nativeQuery = true)
    Map<String, String> getTaskCountForDashboardByRole(Long employee_id,Long project_id);

//    @Query(value = "select t.* from task t left join project_master pm on t.projectid = pm.id LEFT JOIN employee emp ON emp.id =:employee_id LEFT JOIN users u ON emp.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id where t.task_type =:tasktype and t.isactive = 1 and t.isdelete = 0 and if(r.role = 'ROLE_ADMIN',t.id != 0,if(r.role = 'ROLE_MANAGER',pm.ispublic = 1 or pm.deptid = emp.deptid,pm.ispublic = 1 or t.assigned_from = emp.id or t.assigned_to = emp.id)) and (:project_id = 0 OR t.projectid =:project_id)",nativeQuery = true)
//    @Query(value = "select t.*,concat(e.firstname,' ',e.lastname) as employee_name from task t left join project_master pm on t.projectid = pm.id left join employee e on e.id = t.assigned_to LEFT JOIN employee emp ON emp.id =:employee_id LEFT JOIN users u ON emp.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id where t.task_type =:tasktype and t.isactive = 1 and t.isdelete = 0 and if(r.role = 'ROLE_ADMIN',t.id != 0,if(r.role = 'ROLE_MANAGER',pm.ispublic = 1 or pm.deptid = emp.deptid,pm.ispublic = 1 or t.assigned_from = emp.id or t.assigned_to = emp.id)) and (:project_id = 0 OR t.projectid =:project_id)",nativeQuery = true)
    @Query(value = "select t.*,concat(e.firstname,' ',e.lastname) as employee_name from task t left join project_master pm on t.projectid = pm.id left join employee e on e.id = t.assigned_to LEFT JOIN employee emp ON emp.id =:employee_id LEFT JOIN users u ON emp.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id where t.task_type =:tasktype and if(r.role = 'ROLE_ADMIN',t.id != 0,if(r.role = 'ROLE_MANAGER',pm.ispublic = 1 or pm.deptid = emp.deptid,pm.ispublic = 1 or t.assigned_from = emp.id or t.assigned_to = emp.id)) and (:project_id = 0 OR t.projectid =:project_id) and if(:status_type = 'open',t.isactive = 1 AND t.isdelete = 0,if(:status_type = 'close',t.isactive = 0 AND t.isdelete = 0,t.id != 0))",nativeQuery = true)
    List<Map<String, String>> getTaskListForDashboard(Long employee_id,String tasktype,Long project_id,String status_type);

    @Query(value = "select t.id,t.taskcode,t.projectid,t.task,t.description,t.task_type,t.end_date,t.status,concat(e.firstname,' ',e.lastname) as employee_name from task t left join project_master pm on t.projectid = pm.id left join employee e on t.assigned_to = e.id LEFT JOIN employee emp ON emp.id =:employee_id LEFT JOIN users u ON emp.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id where if(r.role = 'ROLE_ADMIN',t.id != 0,if(r.role = 'ROLE_MANAGER',pm.ispublic = 1 or pm.deptid = emp.deptid,pm.ispublic = 1 or t.assigned_from = emp.id or t.assigned_to = emp.id)) and t.isactive = 1 and t.isdelete = 0 and t.end_date < date(now()) and (:project_id = 0 OR t.projectid =:project_id)",nativeQuery = true)
    List<Map<String, String>> getOverDueTaskListForDashboard(Long employee_id,Long project_id);

    @Query(value = "select t.id,t.taskcode,t.projectid,t.task,t.description,t.task_type,t.end_date,t.status,concat(e.firstname,' ',e.lastname) as employee_name from task t left join project_master pm on t.projectid = pm.id left join employee e on t.assigned_to = e.id LEFT JOIN employee emp ON emp.id =:employee_id LEFT JOIN users u ON emp.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id where if(r.role = 'ROLE_ADMIN',t.id != 0,if(r.role = 'ROLE_MANAGER',pm.ispublic = 1 or pm.deptid = emp.deptid,pm.ispublic = 1 or t.assigned_from = emp.id or t.assigned_to = emp.id)) and t.isactive = 1 and t.isdelete = 0 and t.end_date = date(now()) and (:project_id = 0 OR t.projectid =:project_id)",nativeQuery = true)
    List<Map<String, String>> getTodayDueTaskListForDashboard(Long employee_id,Long project_id);

    @Query(nativeQuery = true)
	List<TaskDetails> getTaskListForTracking(Long project_id,Long employee_id);
    
    @Query(nativeQuery = true)
    List<TaskDetails> getTaskListForOverview(Long employee_id,Long selected_emp_id, String fromdate, String todate);

    

	

	

}
