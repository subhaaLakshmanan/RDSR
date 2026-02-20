package com.example.dsr.model;


import java.time.LocalDateTime;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import com.example.dsr.DTO.TaskLogDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NamedNativeQuery(name = "ScheduleLog.getTaskLogById", query = "select sl.*,concat(e.firstname,' ',e.lastname) as employee_name from schedule_log sl left join employee e on e.id = sl.empid where if(:type = 'task',sl.taskid =:id or if(:tag <> 'status',FIND_IN_SET(sl.sub_taskid,(select group_concat(id) from sub_task where taskid =:id)),sl.taskid =:id),if(:type = 'sub_task',sl.sub_taskid =:id,if(:type = 'phase',sl.phaseid =:id,if(:type = 'release',sl.releaseid =:id,sl.id != 0)))) and if(:tag = 'status',sl.remark like '%Status changed%',sl.id != 0)", resultSetMapping = "Mapping.getTaskLogByIdRS")
@SqlResultSetMapping(name = "Mapping.getTaskLogByIdRS", classes = {
@ConstructorResult(targetClass = TaskLogDetails.class, columns = { @ColumnResult(name = "id", type = Long.class),
		@ColumnResult(name = "empid", type = Long.class), 
		@ColumnResult(name = "taskid", type = Long.class),
		@ColumnResult(name = "sub_taskid", type = Long.class),
		@ColumnResult(name = "phaseid", type = Long.class),
		@ColumnResult(name = "releaseid", type = Long.class),
		@ColumnResult(name = "action", type = String.class),
		@ColumnResult(name = "remark", type = String.class),
		@ColumnResult(name = "status", type = String.class),
		@ColumnResult(name = "logdate", type = LocalDateTime.class),
		@ColumnResult(name = "employee_name", type = String.class)})})


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedule_log")
public class ScheduleLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long empid;
	private Long taskid;
	private Long sub_taskid;
	private Long phaseid;
	private Long releaseid;
	private String action;
	private String remark;
	private String status;
	
	@CreationTimestamp
	private LocalDateTime logdate;
	
//	no need 
//	public ScheduleLog(Long empid, Long taskid, Long sub_taskid, Long phaseid, String action, String remark) {
//		super();
//		this.empid = empid;
//		this.taskid = taskid;
//		this.sub_taskid = sub_taskid;
//		this.phaseid = phaseid;
//		this.action = action;
//		this.remark = remark;
//	}

//	public ScheduleLog(Long empid, Long taskid, Long sub_taskid, Long phaseid, Long releaseid, String action,
//			String remark) {
//		super();
//		this.empid = empid;
//		this.taskid = taskid;
//		this.sub_taskid = sub_taskid;
//		this.phaseid = phaseid;
//		this.releaseid = releaseid;
//		this.action = action;
//		this.remark = remark;
//	}

public ScheduleLog(Long empid, Long taskid, Long sub_taskid, Long phaseid, Long releaseid, String action, String remark,
		String status) {
	super();
	this.empid = empid;
	this.taskid = taskid;
	this.sub_taskid = sub_taskid;
	this.phaseid = phaseid;
	this.releaseid = releaseid;
	this.action = action;
	this.remark = remark;
	this.status = status;
}
	
	
	
}
