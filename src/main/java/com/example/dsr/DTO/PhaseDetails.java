package com.example.dsr.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PhaseDetails {

//	Phase class fields
	private Long id;
	private String code;
	private String phase_title;
	private String phase_type;
	private Long projectid;
	private String version;
	private LocalDate start_date;
	private LocalDate end_date;
	private String status;
	private Long assignee;
	private String remark;
	private boolean isactive;
	private boolean isdelete;
	private LocalDateTime createddate;
	private LocalDateTime updateddate;
	
//	Extra fields
	private String project_name;
	private int tasks_done;
	private int tasks_pending;
	
	
}
