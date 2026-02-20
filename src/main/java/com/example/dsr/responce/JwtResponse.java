package com.example.dsr.responce;

import java.util.List;

import lombok.Data;

@Data
public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private Long id;
	private String username;
	private String email;
	private List<String> roles;
	private Long empid;
	private String employee_name;
	private String designation;
	private String department;

	public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
	}

	public JwtResponse(String token, Long id, String username, String email, List<String> roles, Long empid,String employee_name,String designation,String department) {
		super();
		this.token = token;
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
		this.empid = empid;
		this.employee_name = employee_name;
		this.designation = designation;
		this.department=department;
	}

	
}
