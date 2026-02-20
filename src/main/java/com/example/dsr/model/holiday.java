package com.example.dsr.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "holiday")
public class holiday {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	   private long id;
	    private String holiday_name;
	    private String holiday_date;
	    private int days_allocated;
	    private String comments;
	    private String remarks;
	    private String created_date;
	    private boolean isactive;
	    private boolean isdelete;
}
