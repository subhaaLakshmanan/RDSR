package com.example.dsr.model;

import java.time.LocalDate;
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
import javax.persistence.Transient;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.dsr.DTO.PhaseDetails;
import com.example.dsr.DTO.TaskDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NamedNativeQuery(name = "Phase.getPhaseByProject", query = "select p.*,pm.project_title,(select count(id) from task where phaseid =p.id and status = 'Closed' and isdelete = 0) as tasks_done,(select count(id) from task where phaseid =p.id and status <> 'Closed' and isdelete = 0) as tasks_pending from phase p left join project_master pm on p.projectid = pm.id where p.projectid =:project_id and p.isdelete = 0", resultSetMapping = "Mapping.getPhaseByProjectRS")
@SqlResultSetMapping(name = "Mapping.getPhaseByProjectRS", classes = {
@ConstructorResult(targetClass = PhaseDetails.class, columns = { @ColumnResult(name = "id", type = Long.class),
		@ColumnResult(name = "code", type = String.class), 
		@ColumnResult(name = "phase_title", type = String.class),
		@ColumnResult(name = "phase_type", type = String.class),
		@ColumnResult(name = "projectid", type = Long.class),
		@ColumnResult(name = "version", type = String.class),
		@ColumnResult(name = "start_date", type = LocalDate.class),
		@ColumnResult(name = "end_date", type = LocalDate.class),
		@ColumnResult(name = "status", type = String.class),
		@ColumnResult(name = "assignee", type = Long.class),
		@ColumnResult(name = "remark", type = String.class),
		@ColumnResult(name = "isactive", type = Boolean.class),
		@ColumnResult(name = "isdelete", type = Boolean.class),
		@ColumnResult(name = "createddate", type = LocalDateTime.class),
		@ColumnResult(name = "updateddate", type = LocalDateTime.class),
		@ColumnResult(name = "project_title", type = String.class),
		@ColumnResult(name = "tasks_done", type = Integer.class),
		@ColumnResult(name = "tasks_pending", type = Integer.class)})})

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "phase")
public class Phase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	
	@CreationTimestamp
	private LocalDateTime createddate;
	@UpdateTimestamp
	private LocalDateTime updateddate;
	
	@Transient
	private String username;
}
