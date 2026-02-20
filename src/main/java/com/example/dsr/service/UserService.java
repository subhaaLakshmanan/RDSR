package com.example.dsr.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.example.dsr.model.Employee;
import com.example.dsr.request.UserCreateRequest;

public interface UserService {

	ResponseEntity<?> createUser(UserCreateRequest reqobj,HttpServletRequest servrequest);

	ResponseEntity<?> updateUser(Employee reqobj, HttpServletRequest servrequest);

	ResponseEntity<?> deleteUser(Long employee_id,String username,HttpServletRequest servrequest);

	Map getEmployeeListByProject(Long project_id);

	String addUserByXLFile(MultipartFile file,HttpServletRequest servrequest);
	ResponseEntity<?> updatereleaveemployee(Long employee_d,String username, String reason,String releavedate,HttpServletRequest servrequest);


}
