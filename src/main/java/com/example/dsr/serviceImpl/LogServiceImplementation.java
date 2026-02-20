package com.example.dsr.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dsr.model.Log;
import com.example.dsr.repository.LogRepository;
import com.example.dsr.service.LogService;

@Service
public class LogServiceImplementation implements LogService{
	
	@Autowired
	LogRepository logRepository;

	@Override
	public void ActivityLog(String username, String action,String remoteAddress,String remark) {
		try {
            // Save login activity into LoginLog table
			Log log = new Log();
			
			log.setUsername(username);
			log.setAction(action);
			log.setUserip(remoteAddress);
			log.setRemark(remark);
			
			logRepository.save(log);
		}catch(Exception e) {
			System.out.println("Exception in ActivityLog : "+e.getMessage());
		}
	}
	
	

}
