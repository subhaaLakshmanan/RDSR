package com.example.dsr.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.dsr.model.ERole;
import com.example.dsr.model.Employee;
import com.example.dsr.model.Log;
import com.example.dsr.model.Role;
import com.example.dsr.model.User;
import com.example.dsr.repository.*;
import com.example.dsr.request.LoginRequest;
import com.example.dsr.request.SignupRequest;
import com.example.dsr.responce.JwtResponse;
import com.example.dsr.responce.MessageResponse;
import com.example.dsr.security.jwt.JwtUtils;
import com.example.dsr.security.service.UserDetailsImpl;
import com.example.dsr.service.LogService;


@CrossOrigin(origins = "*", maxAge = 3600)  // Allowing all origins is risky. Consider restricting to trusted domains in production.
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final DepartmentRepository departmentRepository;
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;
	
    @Autowired
	JwtUtils jwtUtils;

	@Autowired
	EmployeeRepository employeeRepo;
	
	@Autowired
	DesignationRepository designationRepo;
	
	@Autowired
	LogService logService;

    AuthController(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,HttpServletRequest servrequest) {
		
		// Capture client IP address for logging
		String remoteAddress = servrequest.getRemoteAddr();
		
		// Authenticate using Spring Security
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		
		// Store authentication in security context
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		// Generate JWT token for authenticated user
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		// Extract logged-in user details
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		
		// Fetch roles of the user
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
		
        // Handle login based on user roles
		// This check is redundant. You can just check roles.contains(...)
		if (roles.size() > 0) { 
			if (roles.contains("ROLE_ADMIN")) {
				Employee employee = employeeRepo.getByEmail(loginRequest.getUsername());
				
				logService.ActivityLog(loginRequest.getUsername(),"Admin login",remoteAddress,loginRequest.getUsername());
				 return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),userDetails.getEmail(), roles,employee.getId(),employee.getFirstname()+" "+employee.getLastname(),designationRepo.getPositionNameById(employee.getPositionid()),departmentRepository.getNameById(employee.getDeptid())));
			}else {
				Employee employee = employeeRepo.getByEmail(loginRequest.getUsername());
				
				if(employee.isIsactive() && !employee.isIsdelete()) {
					logService.ActivityLog(loginRequest.getUsername(),"User login",remoteAddress,loginRequest.getUsername());
					return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),userDetails.getEmail(), roles,employee.getId(),employee.getFirstname()+" "+employee.getLastname(),designationRepo.getPositionNameById(employee.getPositionid()),departmentRepository.getNameById(employee.getDeptid())));
				}else {
					return ResponseEntity.badRequest().body(new MessageResponse("Invalid User Credentials!"));
				}
			}
		}
		return ResponseEntity.badRequest().body(new MessageResponse("Login failed!"));
    }

    @PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest,HttpServletRequest servrequest) {
		String remoteAddress = servrequest.getRemoteAddr();

		// Check if email already exists
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		// Map roles from request
		Set<String> strRoles = signUpRequest.getRole();
        
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			// Default role USER
			Role userRole = (Role) roleRepository.findByRole(ERole.ROLE_EMPLOYEE).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			
			// Assign roles based on request
			strRoles.forEach(role -> {
				switch (role) {
				case "ROLE_ADMIN":
					Role adminRole = (Role) roleRepository.findByRole(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					break;
				case "ROLE_MANAGER":
					Role operatorRole = (Role) roleRepository.findByRole(ERole.ROLE_MANAGER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(operatorRole);
                    break;
                default:
					Role userRole = (Role) roleRepository.findByRole(ERole.ROLE_EMPLOYEE).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		// Create new user's account
		User user = new User(signUpRequest.getEmail(),encoder.encode(signUpRequest.getPassword()),signUpRequest.getPassword());
		user.setRoles(roles);
		userRepository.save(user);
		
        // create a activity log		
		logService.ActivityLog(signUpRequest.getUsername(),"User Creation",remoteAddress,"");

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
	
	
	
	
	
	
}
