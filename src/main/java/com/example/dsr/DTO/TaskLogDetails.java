package com.example.dsr.DTO;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class TaskLogDetails {
	
    private Long id;
	private Long empid;
	private Long taskid;
	private Long sub_taskid;
	private Long phaseid;
	private Long releaseid;
	private String action;
	private String remark;
	private String status;
	private LocalDateTime logdate;
	private String employee_name;
	
	public TaskLogDetails(Long id, Long empid, Long taskid, Long sub_taskid, Long phaseid, Long releaseid,
			String action, String remark, String status, LocalDateTime logdate, String employee_name) {
		super();
		this.id = id;
		this.empid = empid;
		this.taskid = taskid;
		this.sub_taskid = sub_taskid;
		this.phaseid = phaseid;
		this.releaseid = releaseid;
		this.action = action;
		this.remark = remark;
		this.status = status;
		this.logdate = logdate;
		this.employee_name = employee_name;
	}
	
	

}
