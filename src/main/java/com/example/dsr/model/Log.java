package com.example.dsr.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "log")
public class Log {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	private String action;
	
	@CreationTimestamp
	private LocalDateTime logdate;
	private String remark;
	private String userip;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public LocalDateTime getLogdate() {
		return logdate;
	}

	public void setLogdate(LocalDateTime logdate) {
		this.logdate = logdate;
	}
	
    public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUserip() {
		return userip;
	}

	public void setUserip(String userip) {
		this.userip = userip;
	}

	public Log() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Log(Long id, String username, String action, LocalDateTime logdate, String remark, String userip) {
		super();
		this.id = id;
		this.username = username;
		this.action = action;
		this.logdate = logdate;
		this.remark = remark;
		this.userip = userip;
	}

	


	
}
