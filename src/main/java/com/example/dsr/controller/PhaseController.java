package com.example.dsr.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dsr.DTO.PhaseDetails;
import com.example.dsr.model.Phase;
import com.example.dsr.model.Task;
import com.example.dsr.repository.PhaseRepository;
import com.example.dsr.service.PhaseService;

@CrossOrigin(origins = "*", maxAge = 3600)  // Allowing all origins is risky. Consider restricting to trusted domains in production.
@RestController
@RequestMapping("/api/v1/phase")
public class PhaseController {
	
	@Autowired
	PhaseService phaseService;
	
	@Autowired
	PhaseRepository phaseRepo;

	@PostMapping("/create")
	public ResponseEntity<?> createPhase(@RequestBody Phase reqobj,HttpServletRequest servrequest){
		return phaseService.createPhase(reqobj,servrequest);
	}
	
	@GetMapping("/getphasebyproject")
	public List<PhaseDetails> getPhaseByProject(@RequestParam Long project_id){
		return phaseRepo.getPhaseByProject(project_id);
	}
	
	@PostMapping("/update")
	public ResponseEntity<?> updatePhase(@RequestBody Phase reqobj,HttpServletRequest servrequest){
		return phaseService.updatePhase(reqobj,servrequest);
	}
	
//	not in use
	@DeleteMapping("/delete")
	public ResponseEntity<?> deletePhase(@RequestParam String username,@RequestParam Long phase_id,HttpServletRequest servrequest){
		return phaseService.deletePhase(username,phase_id,servrequest);
	}
	
	
	
}
