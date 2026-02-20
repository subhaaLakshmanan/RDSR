package com.example.dsr.DTO;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProjectDetails {

//	project class fields
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
	private LocalDateTime created_date;
	private LocalDateTime updated_date;
	
//	extra fields
	private String employees;
	private List<Long> employee_list;
	private String manager_name;
	private String department;
	private int tasks_done;
	private int tasks_pending;
	
	
	public ProjectDetails(Long id, String project_title, String description, Long deptid, LocalDate start_date,
			LocalDate end_date, Long assigned_manager, String client, boolean isactive, boolean isdelete,
			boolean isclose, boolean ispublic, LocalDateTime created_date, LocalDateTime updated_date, String employees,
			String manager_name, String department, int tasks_done, int tasks_pending) {
		super();
		this.id = id;
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
		this.created_date = created_date;
		this.updated_date = updated_date;
		this.employees = employees;
		this.manager_name = manager_name;
		this.department = department;
		this.tasks_done = tasks_done;
		this.tasks_pending = tasks_pending;
	}
	 
	
	
	
	
	
	
	
	
}
