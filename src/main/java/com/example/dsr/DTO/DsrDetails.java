package com.example.dsr.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DsrDetails {

	private Long id;
	private Long subtaskid;
	private String comments;
	private String worked_hours;
	private LocalDate date;
	private boolean isdelete;
	private LocalDateTime createddate;
	private LocalDateTime updateddate;
	private String sub_task;
	private LocalDate start_date;
	private LocalDate end_date;
	private int completion_percentage;
	private String task;
	private String employee_name;
	private String project_title;
	
}
