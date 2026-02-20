package com.example.dsr.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class TaskDetails {

//	Task class fields
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
	private String completion_percentage;
	private boolean isactive;
	private boolean isdelete;
	private LocalDateTime created_date;
	private LocalDateTime updated_date;
	private String remark;
	
//	extra fields
	private String project_title;
	private String assigned_from_name;
	private String assigned_to_name;
	private String phase_title;
	
}
