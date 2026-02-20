package com.example.dsr.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.dsr.DTO.ReleaseDTO;
import com.example.dsr.model.ReleaseDetails;
import com.example.dsr.model.ReleaseMail;
import com.example.dsr.repository.ReleaseDetailsRepository;
import com.example.dsr.request.ReleaseRequest;
import com.example.dsr.service.ReleaseService;


@CrossOrigin(origins = "*", maxAge = 3600)  // Allowing all origins is risky. Consider restricting to trusted domains in production.
@RestController
@RequestMapping("/api/v1/release")
public class ReleaseController {
	
	@Autowired
	ReleaseDetailsRepository releaseDetailsRepo;
	
	@Autowired
	ReleaseService releaseService;
	
	@PostMapping("/create")
	public ResponseEntity<?> createRelease(@RequestBody ReleaseRequest reqobj,HttpServletRequest servrequest){
		return releaseService.createRelease(reqobj,servrequest);
	}
	
	@GetMapping("/getreleasebyproject")
	public List<ReleaseDetails> getAllActiveReleaseByProject(@RequestParam Long project_id){
		return releaseService.getAllActiveReleaseByProject(project_id);
	}
	
	@PostMapping("/update")
	public ResponseEntity<?> updateRelease(@RequestBody ReleaseRequest reqobj,HttpServletRequest servrequest){
		return releaseService.updateRelease(reqobj,servrequest);
	}
	
	@GetMapping("/releaseoverview")
	public List<ReleaseDTO> getReleaseDetailsForOverview(@RequestParam Long employee_id,@RequestParam Long projectid,@RequestParam String fromdate,@RequestParam String todate){
		return releaseService.getReleaseDetailsForOverview(employee_id,projectid,fromdate,todate);
	}
	
	@GetMapping("/releasemailbyid")
	public List<Map<String,String>> getReleaseMaildetailsById(@RequestParam Long id){
		return releaseService.getReleaseMaildetailsById(id);
	}
 
}
