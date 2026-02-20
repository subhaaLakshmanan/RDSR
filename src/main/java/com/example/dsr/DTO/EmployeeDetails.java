package com.example.dsr.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeDetails {
	
//	employee class fields
    private Long id;
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
	private LocalDateTime createddate;
	private LocalDateTime updateddate;
	private boolean isactive;
	private boolean isdelete;
	private Long attendanceid;
	private Long managerid;
	private Long sub_manager_id;
	private boolean wfh;
	private LocalDate releaving_date;
	private String releaving_remarks;
	private Long reference_no;
	
//	Extra fields
	private String department_name;
	private String position;
	private String shift_type;
	private String role;
	private String profile_image_url;

}
