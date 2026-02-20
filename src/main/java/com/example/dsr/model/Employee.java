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

import com.example.dsr.DTO.EmployeeDetails;
import com.example.dsr.DTO.TaskDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NamedNativeQuery(name = "Employee.getEmployeeDetails", query = "SELECT e.*,d.department_name,p.position,s.shift_type,r.role,REPLACE(e.image_url, '/var/www/html', (SELECT link_url FROM server_details WHERE isactive = 1 ORDER BY id DESC LIMIT 1)) AS profile_image_url FROM employee e LEFT JOIN department_master d ON e.deptid = d.id LEFT JOIN position_master p ON p.id = e.positionid LEFT JOIN shift_master s ON s.id = e.shiftid LEFT JOIN users u ON e.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id where r.role <> 'ROLE_ADMIN' and e.isdelete = 0", resultSetMapping = "Mapping.getEmployeeDetailsRS")
@SqlResultSetMapping(name = "Mapping.getEmployeeDetailsRS", classes = {
@ConstructorResult(targetClass = EmployeeDetails.class, columns = { @ColumnResult(name = "id", type = Long.class),
		@ColumnResult(name = "firstname", type = String.class), 
		@ColumnResult(name = "lastname", type = String.class),
		@ColumnResult(name = "image_url", type = String.class),
		@ColumnResult(name = "date_of_birth", type = LocalDate.class),
		@ColumnResult(name = "joining_date", type = LocalDate.class),
		@ColumnResult(name = "gender", type = String.class),
		@ColumnResult(name = "blood_group", type = String.class),
		@ColumnResult(name = "deptid", type = Long.class),
		@ColumnResult(name = "positionid", type = Long.class),
		@ColumnResult(name = "shiftid", type = Long.class),
		@ColumnResult(name = "marital_status", type = String.class),
		@ColumnResult(name = "mobile", type = String.class),
		@ColumnResult(name = "email", type = String.class),
		@ColumnResult(name = "password", type = String.class),
		@ColumnResult(name = "alternate_email", type = String.class),
		@ColumnResult(name = "skypeid", type = String.class),
		@ColumnResult(name = "createddate", type = LocalDateTime.class),
		@ColumnResult(name = "updateddate", type = LocalDateTime.class),
		@ColumnResult(name = "isactive", type = boolean.class),
		@ColumnResult(name = "isdelete", type = boolean.class),
		@ColumnResult(name = "attendanceid", type = Long.class),
		@ColumnResult(name = "managerid", type = Long.class),
		@ColumnResult(name = "sub_manager_id", type = Long.class),
		@ColumnResult(name = "wfh", type = boolean.class),
		@ColumnResult(name = "releaving_date", type = LocalDate.class),
		@ColumnResult(name = "releaving_remarks", type = String.class),
		@ColumnResult(name = "reference_no", type = Long.class),
		@ColumnResult(name = "department_name", type = String.class),
		@ColumnResult(name = "position", type = String.class),
		@ColumnResult(name = "shift_type", type = String.class),
		@ColumnResult(name = "role", type = String.class),
		@ColumnResult(name = "profile_image_url", type = String.class)}) })

@NamedNativeQuery(name = "Employee.getProjectMemberDetails", query = "SELECT e.*,d.department_name,p.position,s.shift_type,r.role,REPLACE(e.image_url, '/var/www/html', (SELECT link_url FROM server_details WHERE isactive = 1 ORDER BY id DESC LIMIT 1)) AS profile_image_url FROM employee e LEFT JOIN department_master d ON e.deptid = d.id LEFT JOIN position_master p ON p.id = e.positionid LEFT JOIN shift_master s ON s.id = e.shiftid LEFT JOIN users u ON e.email = u.email LEFT JOIN user_roles ur ON u.id = ur.user_id LEFT JOIN roles r ON ur.role_id = r.id LEFT JOIN project_members pm ON pm.projectid =:project_id where r.role <> 'ROLE_ADMIN' and e.isactive = 1 and e.isdelete = 0 and FIND_IN_SET(e.id, pm.employee_list)", resultSetMapping = "Mapping.getProjectMemberDetailsRS")
@SqlResultSetMapping(name = "Mapping.getProjectMemberDetailsRS", classes = {
@ConstructorResult(targetClass = EmployeeDetails.class, columns = { @ColumnResult(name = "id", type = Long.class),
		@ColumnResult(name = "firstname", type = String.class), 
		@ColumnResult(name = "lastname", type = String.class),
		@ColumnResult(name = "image_url", type = String.class),
		@ColumnResult(name = "date_of_birth", type = LocalDate.class),
		@ColumnResult(name = "joining_date", type = LocalDate.class),
		@ColumnResult(name = "gender", type = String.class),
		@ColumnResult(name = "blood_group", type = String.class),
		@ColumnResult(name = "deptid", type = Long.class),
		@ColumnResult(name = "positionid", type = Long.class),
		@ColumnResult(name = "shiftid", type = Long.class),
		@ColumnResult(name = "marital_status", type = String.class),
		@ColumnResult(name = "mobile", type = String.class),
		@ColumnResult(name = "email", type = String.class),
		@ColumnResult(name = "password", type = String.class),
		@ColumnResult(name = "alternate_email", type = String.class),
		@ColumnResult(name = "skypeid", type = String.class),
		@ColumnResult(name = "createddate", type = LocalDateTime.class),
		@ColumnResult(name = "updateddate", type = LocalDateTime.class),
		@ColumnResult(name = "isactive", type = boolean.class),
		@ColumnResult(name = "isdelete", type = boolean.class),
		@ColumnResult(name = "attendanceid", type = Long.class),
		@ColumnResult(name = "managerid", type = Long.class),
		@ColumnResult(name = "sub_manager_id", type = Long.class),
		@ColumnResult(name = "wfh", type = boolean.class),
		@ColumnResult(name = "releaving_date", type = LocalDate.class),
		@ColumnResult(name = "releaving_remarks", type = String.class),
		@ColumnResult(name = "reference_no", type = Long.class),
		@ColumnResult(name = "department_name", type = String.class),
		@ColumnResult(name = "position", type = String.class),
		@ColumnResult(name = "shift_type", type = String.class),
		@ColumnResult(name = "role", type = String.class),
		@ColumnResult(name = "profile_image_url", type = String.class)}) })


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employee")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long reference_no;
	private String firstname;
	private String lastname;
	private String image_url;
	
	private LocalDate date_of_birth;
	private LocalDate joining_date;
	
	private String gender;
	private String blood_group;
	
	private Long deptid;
	private Long positionid;
	private Long shiftid;
	
	private String marital_status;
	private String mobile;
	private String email;
	private String password;
	private String alternate_email;
	private String skypeid;
	
	@CreationTimestamp
	private LocalDateTime createddate;
	@UpdateTimestamp
	private LocalDateTime updateddate;
	
	private boolean isactive;
	private boolean isdelete;
	
	private Long attendanceid;
	private Long managerid;
	private Long sub_manager_id;
	
	private boolean wfh;
	private LocalDate releaving_date;
	private String releaving_remarks;
	
	@Transient
	private String username;
	@Transient
	private String role;
	
	
    public Employee(String firstname, String lastname, LocalDate date_of_birth, LocalDate joining_date, String gender,
			String blood_group, Long deptid, Long positionid, Long shiftid, String marital_status, String mobile,
			String email,String password,String alternate_email, String skypeid, boolean isactive, Long attendanceid, Long managerid,Long reference_no) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.date_of_birth = date_of_birth;
		this.joining_date = joining_date;
		this.gender = gender;
		this.blood_group = blood_group;
		this.deptid = deptid;
		this.positionid = positionid;
		this.shiftid = shiftid;
		this.marital_status = marital_status;
		this.mobile = mobile;
		this.email = email;
		this.password = password;
		this.alternate_email = alternate_email;
		this.skypeid = skypeid;
		this.isactive = isactive;
		this.attendanceid = attendanceid;
		this.managerid = managerid;
		this.reference_no = reference_no;
	}
    
    
	
	
	
}
