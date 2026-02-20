package com.example.dsr.service;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.example.dsr.DTO.DsrDetails;
import com.example.dsr.model.DSR;
import com.example.dsr.model.SubTask;

public interface SubTaskService {

	ResponseEntity<?> CreateSubTask(SubTask reqobj, HttpServletRequest servrequest);

	ResponseEntity<?> UpdateSubTask(SubTask reqobj, HttpServletRequest servrequest);

	ResponseEntity<?> DeleteSubTask(Long sub_task_id, String username, HttpServletRequest servrequest);

	ResponseEntity<?> CreateDailyStatus(DSR reqobj,HttpServletRequest servrequest);

	List<DSR> getDSRDetailsBySubTask(Long sub_task_id);

	ResponseEntity<?> DeleteDSR(String username, Long dsr_id);

	List<DsrDetails> getDsrOverviewDetails(Long employee_id, String from_date, String to_date,Long user_id);

//	ResponseEntity<?> UpdateDailyStatus(DSR reqobj);

}
