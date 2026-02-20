package com.example.dsr.request;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class ProjectRequest {

	private Long id;
	private String project_title;
	private String description;
	private Long deptid;
	private LocalDate start_date;
	private LocalDate end_date;
	private Long assigned_manager;
	private String client;
	private boolean isactive;
	private boolean isclose;
	private boolean ispublic;
	private List<Long> employee_list;
	private String username;
	
	public ProjectRequest(Long id, String project_title, Long deptid, LocalDate start_date, LocalDate end_date,
			Long assigned_manager, String client, boolean ispublic, List<Long> employee_list, String username) {
		super();
		this.id = id;
		this.project_title = project_title;
		this.deptid = deptid;
		this.start_date = start_date;
		this.end_date = end_date;
		this.assigned_manager = assigned_manager;
		this.client = client;
		this.ispublic = ispublic;
		this.employee_list = employee_list;
		this.username = username;
	}

	public ProjectRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
