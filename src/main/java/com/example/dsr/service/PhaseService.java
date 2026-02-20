package com.example.dsr.service;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import com.example.dsr.model.Phase;

public interface PhaseService {

	ResponseEntity<?> createPhase(Phase reqobj, HttpServletRequest servrequest);

	ResponseEntity<?> updatePhase(Phase reqobj, HttpServletRequest servrequest);

	ResponseEntity<?> deletePhase(String username, Long phase_id, HttpServletRequest servrequest);

	

}
