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

import com.example.dsr.DTO.ReleaseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NamedNativeQuery(name = "ReleaseDetails.getReleaseDetailsForOverview", query = "select re.*,p.project_title as project_name,concat(e.firstname,' ',e.lastname) as assignee_name from release_details re left join project_master p on re.projectid = p.id left join project_members pm on pm.projectid = p.id left join employee e on e.id = re.assigned_to left join employee emp on emp.id =:employee_id LEFT JOIN users u ON emp.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id where if(r.role = 'ROLE_ADMIN',re.id != 0,FIND_IN_SET(emp.id, pm.employee_list)) and re.isdelete = 0 and if(:projectid != 0,re.projectid=:projectid,re.id != 0) and date(re.planned_date) between date(:fromdate) and date(:todate) order by id desc", resultSetMapping = "Mapping.getReleaseDetailsForOverviewRS")
@SqlResultSetMapping(name = "Mapping.getReleaseDetailsForOverviewRS", classes = {
@ConstructorResult(targetClass = ReleaseDTO.class, columns = { @ColumnResult(name = "id", type = Long.class),
		@ColumnResult(name = "code", type = String.class), 
		@ColumnResult(name = "projectid", type = Long.class),
		@ColumnResult(name = "title", type = String.class),
		@ColumnResult(name = "version", type = String.class),
		@ColumnResult(name = "parent_version", type = String.class),
		@ColumnResult(name = "release_type", type = String.class),
		@ColumnResult(name = "planned_date", type = LocalDate.class),
		@ColumnResult(name = "released_date", type = LocalDate.class),
		@ColumnResult(name = "assigned_to", type = Long.class),
		@ColumnResult(name = "status", type = String.class),
		@ColumnResult(name = "isactive", type = boolean.class),
		@ColumnResult(name = "isdelete", type = boolean.class),
		@ColumnResult(name = "createddate", type = LocalDateTime.class),
		@ColumnResult(name = "updateddate", type = LocalDateTime.class),
		@ColumnResult(name = "project_name", type = String.class),
		@ColumnResult(name = "assignee_name", type = String.class)}) })


@NamedNativeQuery(name = "ReleaseDetails.getReleaseDetailsForDashboardByType", query = "select re.*,p.project_title as project_name,concat(e.firstname,' ',e.lastname) as assignee_name from release_details re left join project_master p on re.projectid = p.id left join project_members pm on pm.projectid = p.id left join employee e on e.id = re.assigned_to left join employee emp on emp.id =:employee_id LEFT JOIN users u ON emp.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id where if(r.role = 'ROLE_ADMIN',re.isdelete = 0,FIND_IN_SET(emp.id, pm.employee_list) or p.ispublic = 1 and re.isdelete = 0) and if(:type = 'upcomming',(MONTH(re.planned_date) = MONTH(CURDATE()) AND YEAR(re.planned_date) = YEAR(CURDATE()) and date(re.planned_date) > date(CURDATE())),(MONTH(re.released_date) = MONTH(CURDATE()) AND YEAR(re.released_date) = YEAR(CURDATE()) and date(re.released_date) < date(CURDATE())))", resultSetMapping = "Mapping.getReleaseDetailsForDashboardByTypeRS")
@SqlResultSetMapping(name = "Mapping.getReleaseDetailsForDashboardByTypeRS", classes = {
@ConstructorResult(targetClass = ReleaseDTO.class, columns = { @ColumnResult(name = "id", type = Long.class),
		@ColumnResult(name = "code", type = String.class), 
		@ColumnResult(name = "projectid", type = Long.class),
		@ColumnResult(name = "title", type = String.class),
		@ColumnResult(name = "version", type = String.class),
		@ColumnResult(name = "parent_version", type = String.class),
		@ColumnResult(name = "release_type", type = String.class),
		@ColumnResult(name = "planned_date", type = LocalDate.class),
		@ColumnResult(name = "released_date", type = LocalDate.class),
		@ColumnResult(name = "assigned_to", type = Long.class),
		@ColumnResult(name = "status", type = String.class),
		@ColumnResult(name = "isactive", type = boolean.class),
		@ColumnResult(name = "isdelete", type = boolean.class),
		@ColumnResult(name = "createddate", type = LocalDateTime.class),
		@ColumnResult(name = "updateddate", type = LocalDateTime.class),
		@ColumnResult(name = "project_name", type = String.class),
		@ColumnResult(name = "assignee_name", type = String.class)}) })

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "release_details")
public class ReleaseDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	@CreationTimestamp
	private LocalDateTime createddate;
	@UpdateTimestamp
	private LocalDateTime updateddate;
	
	private String parent_version;
	
	@Transient
	private String assignee_name;
	
	public ReleaseDetails(String code, Long projectid, String title, String version,String parent_version, String release_type,
			LocalDate planned_date, LocalDate released_date, Long assigned_to, String status, boolean isactive,
			boolean isdelete) {
		super();
		this.code = code;
		this.projectid = projectid;
		this.title = title;
		this.version = version;
		this.parent_version = parent_version;
		this.release_type = release_type;
		this.planned_date = planned_date;
		this.released_date = released_date;
		this.assigned_to = assigned_to;
		this.status = status;
		this.isactive = isactive;
		this.isdelete = isdelete;
	}

	public ReleaseDetails(Long id, String code, Long projectid, String title, String version,String parent_version, String release_type,
			LocalDate planned_date, LocalDate released_date, Long assigned_to, String status, boolean isactive) {
		super();
		this.id = id;
		this.code = code;
		this.projectid = projectid;
		this.title = title;
		this.version = version;
		this.parent_version = parent_version;
		this.release_type = release_type;
		this.planned_date = planned_date;
		this.released_date = released_date;
		this.assigned_to = assigned_to;
		this.status = status;
		this.isactive = isactive;
	}
	
	
	
	
   

}
