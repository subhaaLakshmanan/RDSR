package com.example.dsr.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Table(name = "onduty_details")
public class OndutyDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	 private long id;
	    private long emp_id;
	    private String from_date;
	    private String to_date;
	    private String place_of_visit;
	    private String purpose_of_visit;
	    private String items_needed;
	    private String amount_needed;
	    private String status;
	    private String comments;
	    private String remarks;
	    private String created_date;
	    private boolean isactive;
	    private boolean isdelete;
	    private int no_of_days;
	    private long approved_by;
	   
}
