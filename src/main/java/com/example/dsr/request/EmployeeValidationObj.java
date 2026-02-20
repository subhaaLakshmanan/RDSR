package com.example.dsr.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EmployeeValidationObj {

	private Long id;
	private String firstname;
	private String lastname;
	private String date_of_birth;
	private String joining_date;
    private Long deptid;
	private String mobile;
	private String email;
	private Long attendanceid;
	private String password;
	private String username;
	private String role;
	
	private LocalDate join_date;
	private Long reference_no;

	public EmployeeValidationObj(Long id, String firstname, String lastname, String date_of_birth, String joining_date,
			Long deptid, String mobile, String email, Long attendanceid, String password, String username,
			String role,Long reference_no) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.date_of_birth = date_of_birth;
		this.joining_date = joining_date;
		this.deptid = deptid;
		this.mobile = mobile;
		this.email = email;
		this.attendanceid = attendanceid;
		this.password = password;
		this.username = username;
		this.role = role;
		this.reference_no = reference_no;
	}
	
	
	

}
