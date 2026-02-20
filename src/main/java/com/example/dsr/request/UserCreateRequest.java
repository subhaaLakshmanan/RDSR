package com.example.dsr.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
	
//	its used for user creation
	private String firstname;
	private String lastname;
	private String date_of_birth;
	private String joining_date;
	private String gender;
	private String blood_group;
	private Long deptid;
	private Long positionid;
	private Long shiftid;
	private String marital_status;
	private String mobile;
	private String email;
	private String alternate_email;
	private String skypeid;
	private Long attendanceid;
	private String password;
	private String username;
	private String image_url;
	private String role;
	private Long reference_no;
	
//	its for other use
	private LocalDate DOB;
	private LocalDate join_date;
	private Long managerid;
	
	
	public UserCreateRequest(String firstname, String lastname, String date_of_birth, String joining_date,
			String gender, String blood_group, Long deptid, Long positionid, Long shiftid, String marital_status,
			String mobile, String email, String alternate_email, String skypeid, Long attendanceid, String password,
			String username, String image_url, String role) {
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
		this.alternate_email = alternate_email;
		this.skypeid = skypeid;
		this.attendanceid = attendanceid;
		this.password = password;
		this.username = username;
		this.image_url = image_url;
		this.role = role;
	}


	
	
	
}
