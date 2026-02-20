package com.example.dsr.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.example.dsr.DTO.TaskDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NamedNativeQuery(name = "Task.getTaskListByEmployee", query = "select t.*,p.project_title,ph.phase_title,concat(e.firstname,' ',e.lastname) as assigned_from_name,concat(e1.firstname,' ',e1.lastname) as assigned_to_name from task t left join project_master p on t.projectid = p.id left join phase ph on t.phaseid = ph.id left join employee e on e.id = t.assigned_from left join employee e1 on e1.id = t.assigned_to left join employee emp on emp.id =:employee_id LEFT JOIN users u ON emp.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id where if(r.role = 'ROLE_EMPLOYEE',t.assigned_to =:employee_id,t.projectid =:project_id) and if(:phaseid = 0,t.projectid =:project_id,t.phaseid =:phaseid) and if(:task_type = 'all',t.projectid =:project_id,t.task_type =:task_type) and t.projectid =:project_id and t.isdelete = 0 and ((date(t.start_date) between date(:fromdate) and date(:todate)) or (date(t.end_date) between date(:fromdate) and date(:todate))) order by t.id desc", resultSetMapping = "Mapping.getTaskListByEmployeeRS")
@SqlResultSetMapping(name = "Mapping.getTaskListByEmployeeRS", classes = {
@ConstructorResult(targetClass = TaskDetails.class, columns = { @ColumnResult(name = "id", type = Long.class),@ColumnResult(name = "taskcode", type = String.class), 
		@ColumnResult(name = "projectid", type = Long.class),@ColumnResult(name = "phaseid", type = Long.class),@ColumnResult(name = "version", type = String.class),
		@ColumnResult(name = "task", type = String.class),@ColumnResult(name = "description", type = String.class),@ColumnResult(name = "task_type", type = String.class),
		@ColumnResult(name = "priority", type = String.class),@ColumnResult(name = "severity", type = String.class),@ColumnResult(name = "assigned_from", type = Long.class),@ColumnResult(name = "assigned_to", type = Long.class),
		@ColumnResult(name = "estimated_hours", type = String.class),@ColumnResult(name = "worked_hours", type = String.class),@ColumnResult(name = "start_date", type = LocalDate.class),
		@ColumnResult(name = "end_date", type = LocalDate.class),@ColumnResult(name = "status", type = String.class),@ColumnResult(name = "completion_percentage", type = String.class),
		@ColumnResult(name = "isactive", type = Boolean.class),@ColumnResult(name = "isdelete", type = Boolean.class),@ColumnResult(name = "created_date", type = LocalDateTime.class),
		@ColumnResult(name = "updated_date", type = LocalDateTime.class),@ColumnResult(name = "remark", type = String.class),@ColumnResult(name = "project_title", type = String.class),
		@ColumnResult(name = "assigned_from_name", type = String.class),@ColumnResult(name = "assigned_to_name", type = String.class),@ColumnResult(name = "phase_title", type = String.class) }) })

@NamedNativeQuery(name = "Task.getTaskListForOverview", query = "select t.*,p.project_title,ph.phase_title,concat(e.firstname,' ',e.lastname) as assigned_from_name,concat(e1.firstname,' ',e1.lastname) as assigned_to_name from task t left join project_master p on t.projectid = p.id left join project_members pm on p.id = pm.projectid left join phase ph on t.phaseid = ph.id left join employee e on e.id = t.assigned_from left join employee e1 on e1.id = t.assigned_to left join employee emp on emp.id =:employee_id LEFT JOIN users u ON emp.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id where if(r.role = 'ROLE_ADMIN',t.id != 0,FIND_IN_SET(emp.id, pm.employee_list)) and t.isdelete = 0 and if(:selected_emp_id != 0,t.assigned_to = :selected_emp_id,t.id != 0) and (date(t.start_date) between date(:fromdate) and date(:todate) or date(t.end_date) between date(:fromdate) and date(:todate)) order by t.id desc", resultSetMapping = "Mapping.getTaskListForOverviewRS")
@SqlResultSetMapping(name = "Mapping.getTaskListForOverviewRS", classes = {
@ConstructorResult(targetClass = TaskDetails.class, columns = { @ColumnResult(name = "id", type = Long.class),@ColumnResult(name = "taskcode", type = String.class), 
		@ColumnResult(name = "projectid", type = Long.class),@ColumnResult(name = "phaseid", type = Long.class),@ColumnResult(name = "version", type = String.class),
		@ColumnResult(name = "task", type = String.class),@ColumnResult(name = "description", type = String.class),@ColumnResult(name = "task_type", type = String.class),
		@ColumnResult(name = "priority", type = String.class),@ColumnResult(name = "severity", type = String.class),@ColumnResult(name = "assigned_from", type = Long.class),@ColumnResult(name = "assigned_to", type = Long.class),
		@ColumnResult(name = "estimated_hours", type = String.class),@ColumnResult(name = "worked_hours", type = String.class),@ColumnResult(name = "start_date", type = LocalDate.class),
		@ColumnResult(name = "end_date", type = LocalDate.class),@ColumnResult(name = "status", type = String.class),@ColumnResult(name = "completion_percentage", type = String.class),
		@ColumnResult(name = "isactive", type = Boolean.class),@ColumnResult(name = "isdelete", type = Boolean.class),@ColumnResult(name = "created_date", type = LocalDateTime.class),
		@ColumnResult(name = "updated_date", type = LocalDateTime.class),@ColumnResult(name = "remark", type = String.class),@ColumnResult(name = "project_title", type = String.class),
		@ColumnResult(name = "assigned_from_name", type = String.class),@ColumnResult(name = "assigned_to_name", type = String.class),@ColumnResult(name = "phase_title", type = String.class) }) })


//@NamedNativeQuery(name = "Task.getTaskListForTracking", query = "select t.*,p.project_title,ph.phase_title,concat(e.firstname,' ',e.lastname) as assigned_from_name,concat(e1.firstname,' ',e1.lastname) as assigned_to_name from task t left join project_master p on t.projectid = p.id left join phase ph on t.phaseid = ph.id left join employee e on e.id = t.assigned_from left join employee e1 on e1.id = t.assigned_to left join department_master dm on dm.id = e1.deptid where t.isdelete = 0 and if(:project_id = 0,t.id != 0,t.projectid =:project_id) and if(dm.department_name = 'SQA',t.status IN ('In-Review','To-be-Tested','In-Testing'),t.status IN ('Open','In-Progress','Delayed','Active','On-Track','Approved','Planning')) and if(:employee_id = 0,t.id != 0,t.assigned_to =:employee_id)", resultSetMapping = "Mapping.getTaskListForTrackingRS")
@NamedNativeQuery(name = "Task.getTaskListForTracking", query = "select t.*,p.project_title,ph.phase_title,concat(e.firstname,' ',e.lastname) as assigned_from_name,concat(e1.firstname,' ',e1.lastname) as assigned_to_name from task t left join project_master p on t.projectid = p.id left join phase ph on t.phaseid = ph.id left join employee e on e.id = t.assigned_from left join employee e1 on e1.id = t.assigned_to left join department_master dm on dm.id = e1.deptid where t.isdelete = 0 and if(:project_id = 0,t.id != 0,t.projectid =:project_id) and if(:employee_id = 0,t.id != 0,t.assigned_to =:employee_id) order by t.id desc", resultSetMapping = "Mapping.getTaskListForTrackingRS")
@SqlResultSetMapping(name = "Mapping.getTaskListForTrackingRS", classes = {
@ConstructorResult(targetClass = TaskDetails.class, columns = { @ColumnResult(name = "id", type = Long.class),@ColumnResult(name = "taskcode", type = String.class), 
		@ColumnResult(name = "projectid", type = Long.class),@ColumnResult(name = "phaseid", type = Long.class),@ColumnResult(name = "version", type = String.class),
		@ColumnResult(name = "task", type = String.class),@ColumnResult(name = "description", type = String.class),@ColumnResult(name = "task_type", type = String.class),
		@ColumnResult(name = "priority", type = String.class),@ColumnResult(name = "severity", type = String.class),@ColumnResult(name = "assigned_from", type = Long.class),@ColumnResult(name = "assigned_to", type = Long.class),
		@ColumnResult(name = "estimated_hours", type = String.class),@ColumnResult(name = "worked_hours", type = String.class),@ColumnResult(name = "start_date", type = LocalDate.class),
		@ColumnResult(name = "end_date", type = LocalDate.class),@ColumnResult(name = "status", type = String.class),@ColumnResult(name = "completion_percentage", type = String.class),
		@ColumnResult(name = "isactive", type = Boolean.class),@ColumnResult(name = "isdelete", type = Boolean.class),@ColumnResult(name = "created_date", type = LocalDateTime.class),
		@ColumnResult(name = "updated_date", type = LocalDateTime.class),@ColumnResult(name = "remark", type = String.class),@ColumnResult(name = "project_title", type = String.class),
		@ColumnResult(name = "assigned_from_name", type = String.class),@ColumnResult(name = "assigned_to_name", type = String.class),@ColumnResult(name = "phase_title", type = String.class) }) })



@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task")
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String taskcode;
	private Long projectid;
	private Long phaseid;
	private String version;
	private String task;
	private String description;
	private String task_type;
	private String priority;
	private String severity;
	private Long assigned_from;
	private Long assigned_to;
	private String estimated_hours;
	private String worked_hours;
	private LocalDate start_date;
	private LocalDate end_date;
	private String status;
	private int completion_percentage;
	private boolean isactive;
	private boolean isdelete;
	
	@CreationTimestamp
	private LocalDateTime created_date;
	@UpdateTimestamp
	private LocalDateTime updated_date;
	
	private String remark;
	
}
