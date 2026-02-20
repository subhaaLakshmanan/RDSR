package com.example.dsr.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ReleaseDTO {

//	Release class fields
	private Long id;
	private String code;
	private Long projectid;
	private String title;
	private String version;
	private String release_type;
	private LocalDate planned_date;
	private LocalDate released_date;
	private Long assigned_to;
	private String status;
	private boolean isactive;
	private boolean isdelete;
	private LocalDateTime createddate;
    private LocalDateTime updateddate;
    
//    extra fields
    private String project_name;
    private String assignee_name;
}
