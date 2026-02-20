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

import com.example.dsr.DTO.DsrDetails;
import com.example.dsr.DTO.PhaseDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@NamedNativeQuery(name = "DSR.getDsrOverviewDetails", query = "select d.*,s.task as sub_task,s.start_date,s.end_date,s.completion_percentage,t.task,concat(e.firstname,' ',e.lastname) as employee_name,p.project_title from dsr d left join sub_task s on d.subtaskid = s.id left join task t on t.id = s.taskid left join employee e on s.employeeid = e.id left join project_master p on p.id = t.projectid where d.isdelete = 0 and if(:employee_id = 0,d.id != 0,e.id =:employee_id) and d.date between :from_date and :to_date", resultSetMapping = "Mapping.getDsrOverviewDetailsRS")
@NamedNativeQuery(name = "DSR.getDsrOverviewDetails", query = "select d.*,s.task as sub_task,s.start_date,s.end_date,s.completion_percentage,t.task,concat(e.firstname,' ',e.lastname) as employee_name,p.project_title from dsr d left join sub_task s on d.subtaskid = s.id left join task t on t.id = s.taskid left join employee e on s.employeeid = e.id left join project_master p on p.id = t.projectid left join employee emp on emp.id =:user_id LEFT JOIN users u ON emp.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id where d.isdelete = 0 and if(:employee_id = 0,d.id != 0,e.id =:employee_id) and d.date between :from_date and :to_date and if(r.role = 'ROLE_ADMIN',d.isdelete = 0,e.deptid = emp.deptid)", resultSetMapping = "Mapping.getDsrOverviewDetailsRS")
@SqlResultSetMapping(name = "Mapping.getDsrOverviewDetailsRS", classes = {
@ConstructorResult(targetClass = DsrDetails.class, columns = { @ColumnResult(name = "id", type = Long.class),
		@ColumnResult(name = "subtaskid", type = Long.class), 
		@ColumnResult(name = "comments", type = String.class),
		@ColumnResult(name = "worked_hours", type = String.class),
		@ColumnResult(name = "date", type = LocalDate.class),
		@ColumnResult(name = "isdelete", type = Boolean.class),
		@ColumnResult(name = "createddate", type = LocalDateTime.class),
		@ColumnResult(name = "updateddate", type = LocalDateTime.class),
		@ColumnResult(name = "sub_task", type = String.class),
		@ColumnResult(name = "start_date", type = LocalDate.class),
		@ColumnResult(name = "end_date", type = LocalDate.class),
		@ColumnResult(name = "completion_percentage", type = Integer.class),
		@ColumnResult(name = "task", type = String.class),
		@ColumnResult(name = "employee_name", type = String.class),
		@ColumnResult(name = "project_title", type = String.class)})})

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dsr")
public class DSR {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long subtaskid;
	private String comments;
	private String worked_hours;
	private LocalDate date;
	private boolean isdelete;
	@CreationTimestamp
	private LocalDateTime createddate;
	@UpdateTimestamp
	private LocalDateTime updateddate;
	
	@Transient
	private String username;
	@Transient
	private int completion_percentage;
	@Transient
	private String task;
}
