package com.example.dsr.DTO;

import lombok.Data;

@Data
public class AttendanceDTO {

	 private String employeeName;
	    private Long attendanceId;
	    private String attendanceDate;
	    private String day;

	    private String status;
	    
	    private String alltime;
	    private String intime;
	    private String statusemp;
//	    private String latetemp;
	    
	    private String workedhoursdash;
}
