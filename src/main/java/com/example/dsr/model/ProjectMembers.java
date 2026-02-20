package com.example.dsr.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "project_members")
public class ProjectMembers {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long projectid;
	private String employee_list;
	
	@CreationTimestamp
	private LocalDateTime created_date;
	@UpdateTimestamp
	private LocalDateTime updated_date;
	
	public ProjectMembers(Long projectid, String employee_list) {
		super();
		this.projectid = projectid;
		this.employee_list = employee_list;
	}

	public ProjectMembers(Long id, Long projectid, String employee_list) {
		super();
		this.id = id;
		this.projectid = projectid;
		this.employee_list = employee_list;
	}
	
	
	
	
	
	

}
