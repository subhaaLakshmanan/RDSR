package com.example.dsr.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.example.dsr.DTO.ReleaseDTO;
import com.example.dsr.model.ReleaseDetails;
import com.example.dsr.model.ReleaseMail;
import com.example.dsr.request.ReleaseRequest;

public interface ReleaseService {

	ResponseEntity<?> createRelease(ReleaseRequest reqobj, HttpServletRequest servrequest);

	List<ReleaseDetails> getAllActiveReleaseByProject(Long project_id);

	ResponseEntity<?> updateRelease(ReleaseRequest reqobj, HttpServletRequest servrequest);

	List<ReleaseDTO> getReleaseDetailsForOverview(Long employee_id, Long projectid, String fromdate, String todate);

	List<Map<String,String>> getReleaseMaildetailsById(Long id);

}
