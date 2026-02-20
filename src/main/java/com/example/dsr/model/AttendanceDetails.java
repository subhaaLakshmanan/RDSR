package com.example.dsr.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import com.example.dsr.DTO.AttendanceDTO;
import com.example.dsr.DTO.ProjectDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@NamedNativeQuery(name = "Project.collectActiveProjects", query = "select attendanceid,intime,shift_id,date(attendance_date) attdate,GROUP_CONCAT(time(intime) SEPARATOR ' - ') alltime from attendance_details where attendanceid=:id and substring(attendance_date,1,10)=:date", resultSetMapping = "Mapping.getAd")
//@SqlResultSetMapping(name = "Mapping.getAd", classes = {
//@ConstructorResult(targetClass = AttendanceDTO.class, 
//columns = { @ColumnResult(name = "attendanceid", type = Long.class),		
//		@ColumnResult(name = "intime", type = String.class), 
//		@ColumnResult(name = "attdate", type = String.class),
//		@ColumnResult(name = "alltime", type = Long.class)
//		}) })


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attendance_details")
public class AttendanceDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long attendanceid;
	private Long shift_id;
	private LocalDate attendance_date;
	private LocalDateTime punch_time;
}
