package com.example.dsr.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sub_task")
public class SubTask {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String code;
	private Long taskid;
	private Long employeeid;
	private String task;
	private String description;
	private String estimated_hours;
	private String worked_hours;
	private String status;
	private LocalDate start_date;
	private LocalDate end_date;
	private String priority;
	private boolean isactive;
	private boolean isdelete;
	private int completion_percentage;
	
	@CreationTimestamp
	private LocalDateTime created_date;
	@UpdateTimestamp
	private LocalDateTime updated_date;
	
//	@Transient
//	private String dsr_comment;
//	@Transient
//	private String today_worked_hours;
	
}
