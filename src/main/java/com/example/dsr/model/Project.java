package com.example.dsr.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

import com.example.dsr.DTO.EmployeeDetails;
import com.example.dsr.DTO.ProjectDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NamedNativeQuery(name = "Project.collectActiveProjects", query = "select p.*,pm.employee_list as employees,d.department_name as department,concat(ae.firstname,' ',ae.lastname) as manager_name,(select count(id) from task where projectid = p.id and status = 'Closed' and isdelete = 0) as tasks_done,(select count(id) from task where projectid = p.id and status <> 'Closed' and isdelete = 0) as tasks_pending from project_master p left join project_members pm on p.id = pm.projectid left join employee e on e.id =:employee_id left join users u on e.email = u.email left join user_roles ur on u.id = ur.user_id left join roles r on ur.role_id = r.id left join department_master d on p.deptid = d.id left join employee ae on ae.id = p.assigned_manager where p.isdelete = 0 and (r.role = 'ROLE_ADMIN' OR (r.role = 'ROLE_MANAGER' AND (p.ispublic = 1 OR p.deptid = e.deptid or FIND_IN_SET(e.id, pm.employee_list)OR e.id = p.assigned_manager)) OR (r.role = 'ROLE_EMPLOYEE'AND (p.ispublic = 1 OR FIND_IN_SET(e.id, pm.employee_list)OR e.id = p.assigned_manager)))", resultSetMapping = "Mapping.collectActiveProjectsRS")
@SqlResultSetMapping(name = "Mapping.collectActiveProjectsRS", classes = {
@ConstructorResult(targetClass = ProjectDetails.class, columns = { @ColumnResult(name = "id", type = Long.class),
		@ColumnResult(name = "project_title", type = String.class), 
		@ColumnResult(name = "description", type = String.class),
		@ColumnResult(name = "deptid", type = Long.class),
		@ColumnResult(name = "start_date", type = LocalDate.class),
		@ColumnResult(name = "end_date", type = LocalDate.class),
		@ColumnResult(name = "assigned_manager", type = Long.class),
		@ColumnResult(name = "client", type = String.class),
		@ColumnResult(name = "isactive", type = boolean.class),
		@ColumnResult(name = "isdelete", type = boolean.class),
		@ColumnResult(name = "isclose", type = boolean.class),
		@ColumnResult(name = "ispublic", type = boolean.class),
		@ColumnResult(name = "created_date", type = LocalDateTime.class),
		@ColumnResult(name = "updated_date", type = LocalDateTime.class),
		@ColumnResult(name = "employees", type = String.class),
		@ColumnResult(name = "manager_name", type = String.class),
		@ColumnResult(name = "department", type = String.class),
		@ColumnResult(name = "tasks_done", type = Integer.class),
		@ColumnResult(name = "tasks_pending", type = Integer.class)}) })


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "project_master")
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String project_title;
	private String description;
	private Long deptid;
	private LocalDate start_date;
	private LocalDate end_date;
	private Long assigned_manager;
	private String client;
	private boolean isactive;
	private boolean isdelete;
	private boolean isclose;
	private boolean ispublic;
	
	@CreationTimestamp
	private LocalDateTime created_date;
	@UpdateTimestamp
	private LocalDateTime updated_date;
	
	@Transient
	private List<Long> employee_list;
	@Transient
	private String username;
//	@Transient
//	private String manager_name;
//	@Transient
//	private String department;
	
	public Project(String project_title, String description, Long deptid, LocalDate start_date, LocalDate end_date,
			Long assigned_manager, String client, boolean isactive, boolean isdelete, boolean isclose,
			boolean ispublic) {
		super();
		this.project_title = project_title;
		this.description = description;
		this.deptid = deptid;
		this.start_date = start_date;
		this.end_date = end_date;
		this.assigned_manager = assigned_manager;
		this.client = client;
		this.isactive = isactive;
		this.isdelete = isdelete;
		this.isclose = isclose;
		this.ispublic = ispublic;
	}
	
	
	
	
}
