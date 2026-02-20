package com.example.dsr.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.example.dsr.DTO.AttendanceDTO;
import com.example.dsr.model.Comments;

public interface CommentsService {

	ResponseEntity<?> createComments(Comments reqobj,HttpServletRequest servrequest);

	List<Comments> getCommentsById(String type, Long id);

	ResponseEntity<?> updateComments(Comments reqobj, HttpServletRequest servrequest);

	ResponseEntity<?> deleteComments(Long id, String username, HttpServletRequest servrequest);

	ResponseEntity<?> loadAttendance(MultipartFile file);
	
	List<AttendanceDTO> getAttendance(Long id, String fromdate,String todate);

	 


}
