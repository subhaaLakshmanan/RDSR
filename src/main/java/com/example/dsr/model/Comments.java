package com.example.dsr.model;

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
@Table(name = "comments")
public class Comments {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String code;
	private Long empid;
	private Long taskid;
	private Long sub_taskid;
	private Long phaseid;
	private Long releaseid;
	private String comments;
	private boolean isdelete;
	
	@CreationTimestamp
	private LocalDateTime created_date;
	@UpdateTimestamp
	private LocalDateTime updated_date;
	
	@Transient
	private String employee_name;
	
	
}
