package com.example.dsr.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.dsr.DTO.AttendanceDTO;
import com.example.dsr.model.AttendanceDetails;

@Repository
public interface AttendanceDetailsRepository extends JpaRepository<AttendanceDetails, Long>{

	@Query(value = "select  MAX(punch_time) as datetime from attendance_details",nativeQuery = true)
	LocalDateTime getLastIntime();
	
	@Query(value="select count(*) from attendance_details where substring(attendance_date,1,10)=:date and attendanceid=:id", nativeQuery=true)
	int checkAD(String date,long id);
	@Query(value="select count(*) from sub_task where substring(created_date,1,10)=:date and employeeid=:id", nativeQuery=true)
	int checkDSR(String date,long id);

	@Query(value="select count(*) from onduty_details where :date between from_date and to_date and emp_id=:id", nativeQuery=true)
	int checkOnDuty(String date,long id);
	
	@Query(value="select count(*) from holiday where substring(holiday_date,1,10)=:date", nativeQuery=true)
	int checkHoliday(String date);
	
	@Query(value="select from_date,to_date from leave_details where substring(from_date,1,10)=:date and emp_id=:id", nativeQuery=true)
	List<Object[]> checkLeave(String date,long id);
	
	@Query(value="select attendanceid,punch_time as intime,shift_id,date(attendance_date) attendanceDate,GROUP_CONCAT(time(punch_time) SEPARATOR ' - ') alltime from attendance_details where attendanceid=:id and substring(attendance_date,1,10)=:date", nativeQuery=true)
	AttendanceProjection getAD(String date,long id);
	
	@Query(value="select employeeid as attendanceId,date(created_date) as attendanceDate,'-' as punchTime,'WFH' as remarks,'WFH' as shift,concat(sum(worked_hours),' Hours') as workedHrs,'Present' as status from sub_task where substring(created_date,1,10)=:date and employeeid=:id", nativeQuery=true)
	AttendanceProjection getDSR(String date,long id);
	
	@Query(value="select emp_id as attendanceId,date(from_date) as attendanceDate,'-' as punchTime,concat('Place: ',place_of_visit,' Purpose: ',purpose_of_visit) as remarks,'OnDuty' as shift,'-' as workedHrs,'OnDuty' as status from onduty_details where :date between from_date and to_date and emp_id=:id limit 1", nativeQuery=true)
	AttendanceProjection getOnDuty(String date,long id);
	
	@Query(value="select 0 as attendanceId,date(holiday_date) as attendanceDate,'-' as punchTime,holiday_name as remarks,'-' as shift,'-' as workedHrs,'Holiday' as status from holiday where substring(holiday_date,1,10)=:date", nativeQuery=true)
	AttendanceProjection getHoliday(String date);
	
	@Query(value="select emp_id as attendanceId,date(from_date) as attendanceDate,'-' as punchTime,leave_reason as remarks,'-' as shift,'-' as workedHrs,'Leave' as status from leave_details where emp_id=:id and substring(from_date,1,10)=:date limit 1", nativeQuery=true)
	AttendanceProjection getLeave(String date,long id);
}
