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
@Table(name = "shift_master")
public class Shift {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String shift_type;
	private String in_time;
	private String out_time;
	private String shift_description;
	
	@CreationTimestamp
	private LocalDateTime created_date;
	@UpdateTimestamp
	private LocalDateTime updated_date;
	
	private boolean isactive;
	private boolean isdelete;
}
