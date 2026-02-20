package com.example.dsr.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dsr.model.Department;
import com.example.dsr.model.ERole;
import com.example.dsr.model.Employee;
import com.example.dsr.model.Log;
import com.example.dsr.model.MasterSettings;
import com.example.dsr.model.Role;
import com.example.dsr.model.User;
import com.example.dsr.repository.DepartmentRepository;
import com.example.dsr.repository.EmployeeRepository;
import com.example.dsr.repository.LogRepository;
import com.example.dsr.repository.MasterSettingsRepository;
import com.example.dsr.repository.ProjectRepository;
import com.example.dsr.repository.RoleRepository;
import com.example.dsr.repository.UserRepository;
import com.example.dsr.request.EmployeeValidationObj;
import com.example.dsr.request.UserCreateRequest;
import com.example.dsr.responce.MessageResponse;
import com.example.dsr.service.LogService;
import com.example.dsr.service.UserService;

@Service
public class UserServiceImplementation implements UserService {
	
	@Autowired
	EmployeeRepository employeeRepo;
	
	@Autowired
	DepartmentRepository departmentRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	LogService logService;
	
	@Autowired
	ProjectRepository projectRepo;
	
	@Autowired
	MasterSettingsRepository masterSettingsRepo;

	@Override
	public ResponseEntity<?> createUser(UserCreateRequest reqobj,HttpServletRequest servrequest) {
		try {
			String remoteAddress = servrequest.getRemoteAddr();
			
			EmployeeValidationObj Obj = new EmployeeValidationObj((long) 0,reqobj.getFirstname(), reqobj.getLastname(), reqobj.getDate_of_birth(), reqobj.getJoining_date(),
					reqobj.getDeptid(), reqobj.getMobile(), reqobj.getEmail(), reqobj.getAttendanceid(), reqobj.getPassword(), reqobj.getUsername(),reqobj.getRole(),reqobj.getReference_no());
			
			ResponseEntity responce = userValidation(Obj);

			MessageResponse message = (MessageResponse) responce.getBody();
			
		    // If address validation fails → return error response
			if (!message.getMessage().equals("Success")) {
				return responce;
			}
			
            Department dept = departmentRepo.getById(reqobj.getDeptid());
			
			if(reqobj.getRole().equalsIgnoreCase("ROLE_MANAGER")) {
				Long admin_id = (long) 1;
				reqobj.setManagerid(admin_id);
			}else {
				reqobj.setManagerid(dept.getDept_head());
			}
			
			reqobj.setJoin_date(LocalDate.parse(reqobj.getJoining_date()));
		    reqobj.setDOB(LocalDate.parse(reqobj.getDate_of_birth()));
			
			Employee empObj = new Employee(reqobj.getFirstname(), reqobj.getLastname(), reqobj.getDOB(), reqobj.getJoin_date(), reqobj.getGender(),
					reqobj.getBlood_group(), reqobj.getDeptid(), reqobj.getPositionid(), reqobj.getShiftid(), reqobj.getMarital_status(), reqobj.getMobile(),
					reqobj.getEmail(),reqobj.getPassword(), reqobj.getAlternate_email(), reqobj.getSkypeid(),true, reqobj.getAttendanceid(), reqobj.getManagerid(),reqobj.getReference_no()); 
			
			employeeRepo.save(empObj);
			
			empObj = employeeRepo.getByEmail(reqobj.getEmail());
			
			if(reqobj.getImage_url() != null) {
                String file_url = fileUpload(empObj.getId(),reqobj.getImage_url());
				
				empObj.setImage_url(file_url);
				employeeRepo.save(empObj);
			}
			
			User user = new User(reqobj.getEmail(),encoder.encode(reqobj.getPassword()),reqobj.getPassword());
			
			Set<Role> roles = new HashSet<>();
			
			if(reqobj.getRole().equalsIgnoreCase("ROLE_MANAGER")) {
				Role managerRole = (Role) roleRepository.findByRole(ERole.ROLE_MANAGER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(managerRole);
            }else {
            	Role employeeRole = (Role) roleRepository.findByRole(ERole.ROLE_EMPLOYEE).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(employeeRole);
			}
			
			user.setRoles(roles);
			userRepo.save(user);
			
			logService.ActivityLog(reqobj.getUsername(),"User creation",remoteAddress,empObj.getEmail());
			
		}catch(Exception e) {
			System.out.println("Error in CreateUser : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
		return ResponseEntity.ok(new MessageResponse("Created Successfully!"));
	}
	
	@Override
	public ResponseEntity<?> updateUser(Employee reqobj, HttpServletRequest servrequest) {
		try {
            String remoteAddress = servrequest.getRemoteAddr();
            
            Employee empObj = employeeRepo.getById(reqobj.getId());
            
            EmployeeValidationObj Obj = new EmployeeValidationObj(reqobj.getId(),reqobj.getFirstname(), reqobj.getLastname(), reqobj.getDate_of_birth().toString(), reqobj.getJoining_date().toString(),
					reqobj.getDeptid(), reqobj.getMobile(), reqobj.getEmail(), reqobj.getAttendanceid(), reqobj.getPassword(), reqobj.getUsername(),reqobj.getRole(),reqobj.getReference_no());
			
			ResponseEntity responce = userValidation(Obj);

			MessageResponse message = (MessageResponse) responce.getBody();
			
		    // If address validation fails → return error response
			if (!message.getMessage().equals("Success")) {
				return responce;
			}
			
			Department dept = departmentRepo.getById(reqobj.getDeptid());
				
		    if(reqobj.getRole().equalsIgnoreCase("ROLE_MANAGER")) {
			    Long admin_id = (long) 1;
			    reqobj.setManagerid(admin_id);
			}else {
			    reqobj.setManagerid(dept.getDept_head());
			}
		    
            Set<Role> roles = new HashSet<>();
			
			if(reqobj.getRole().equalsIgnoreCase("ROLE_MANAGER")) {
				Role managerRole = (Role) roleRepository.findByRole(ERole.ROLE_MANAGER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(managerRole);
            }else {
            	Role employeeRole = (Role) roleRepository.findByRole(ERole.ROLE_EMPLOYEE).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(employeeRole);
			}
			
            User user = userRepo.getByEmail(reqobj.getEmail());

			user = new User(user.getId(),reqobj.getEmail(),encoder.encode(reqobj.getPassword()),reqobj.getPassword(),roles);

			userRepo.save(user);
			
			if(reqobj.getImage_url() != null) {
				if(!empObj.getImage_url().equals(reqobj.getImage_url())) {
					String file_url = fileUpload(empObj.getId(),reqobj.getImage_url());
					reqobj.setImage_url(file_url);
				}
			}
			reqobj.setCreateddate(empObj.getCreateddate());
			reqobj.setUpdateddate(empObj.getUpdateddate());
			employeeRepo.save(reqobj);
			
			logService.ActivityLog(reqobj.getUsername(),"Edit User Details",remoteAddress,reqobj.getEmail());

		}catch(Exception e) {
			System.out.println("Error in UpdateUser : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
		return ResponseEntity.ok(new MessageResponse("Edited Successfully!"));
	}
	
	public String fileUpload(Long employee_id,String data_url) {
		String file_path = null;
		try {
			String fileName = "User_Profile_"+employee_id;
	        Path uploadPath = Paths.get("/var/www/html/DSR");
	        
	        file_path = uploadPath+"/"+fileName;
	        
	        Path path = Paths.get(file_path);
	      
	        byte[] bytes = Base64.getDecoder().decode(data_url.split(",")[1]);
			Files.write(path, bytes);
			
			Files.setPosixFilePermissions(path, PosixFilePermissions.fromString("rwxrwxrwx"));
			
		}catch(Exception e) {
			System.out.println("Error in user profile upload : "+e.getMessage());
		}
		return file_path;
	}
	
	
	public ResponseEntity<?> updatereleaveemployee(Long employee_d,String username,String reason,String releavedate, HttpServletRequest servrequest) {
		try {
            String remoteAddress = servrequest.getRemoteAddr();
            
            Employee empObj = employeeRepo.getById(employee_d);
			
            empObj.setReleaving_date(LocalDate.parse(releavedate));
            empObj.setReleaving_remarks(reason);
            empObj.setIsactive(false);

			employeeRepo.save(empObj);
			
			logService.ActivityLog(username,"Releave data Update",remoteAddress,empObj.getEmail());

		}catch(Exception e) {
			System.out.println("Error in UpdateUser : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
		return ResponseEntity.ok(new MessageResponse("Edited Successfully!"));
	}
	
	
	
	public ResponseEntity<?> userValidation(EmployeeValidationObj reqobj) {
		try {
			if(!userRepo.existsByEmail(reqobj.getUsername())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized User!"));
			}
			
			reqobj.setFirstname(reqobj.getFirstname().trim());
			reqobj.setLastname(reqobj.getLastname().trim());
			
			if(reqobj.getFirstname() == null || reqobj.getFirstname().isEmpty()){
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Enter The Valid Firstname"));
			}
			
			if(reqobj.getLastname() == null || reqobj.getLastname().isEmpty()){
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Enter The Valid Lastname"));
			}
			
			if(reqobj.getPassword() == null || reqobj.getPassword().isEmpty()){
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Enter The Valid Password"));
			}
			
			reqobj.setDate_of_birth(reqobj.getDate_of_birth().trim());
			reqobj.setJoining_date(reqobj.getJoining_date().trim());
			
			if(reqobj.getDate_of_birth() == null || reqobj.getDate_of_birth().isEmpty()){
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Enter The Valid Date Of Birth"));
			}

			if(reqobj.getJoining_date() == null || reqobj.getJoining_date().isEmpty()){
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Enter The Valid Joining Date"));
			}
			
		    reqobj.setJoin_date(LocalDate.parse(reqobj.getJoining_date()));
		   
			if(reqobj.getJoin_date().isAfter(LocalDate.now())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Enter The Valid Joining Date"));
			}
			
			reqobj.setMobile(reqobj.getMobile().trim());
			reqobj.setEmail(reqobj.getEmail().trim());
			
//			int mail_id_count = epmployeeRepo.getMailIdCountByEmployeeIdAndMail(reqobj.getId(),reqobj.getEmail());
//			int mobile_no_count = epmployeeRepo.getMobileNoCountByEmployeeId(reqobj.getId(),reqobj.getMobile());
			
//			if(reqobj.getMobile() == null || reqobj.getMobile().isEmpty()){
//				return ResponseEntity.badRequest().body(new MessageResponse("Error: Enter The Valid Mobile Number"));
//			}else if(epmployeeRepo.existsByMobile(reqobj.getMobile())) {
//				return ResponseEntity.badRequest().body(new MessageResponse("Error: Mobile Number is already registered! , please use different Mobile Number"));
//			}
//			
//			if(reqobj.getEmail() == null || reqobj.getEmail().isEmpty()){
//				return ResponseEntity.badRequest().body(new MessageResponse("Error: Enter The Valid Email"));
//			}else if(epmployeeRepo.existsByEmail(reqobj.getEmail())){
//				return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already registered! , please use different Email"));
//			}
			
			if(reqobj.getMobile() == null || reqobj.getMobile().isEmpty()){
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Enter The Valid Mobile Number"));
			}else {
				int mobile_no_count = reqobj.getId() == 0 ? employeeRepo.getMobileNoCount(reqobj.getMobile()): employeeRepo.getMobileNoCountByEmployeeId(reqobj.getId(),reqobj.getMobile());
				if(mobile_no_count > 0) {
					return ResponseEntity.badRequest().body(new MessageResponse("Error: Mobile Number is already registered! , please use different Mobile Number"));
				}
			}
			
			if(reqobj.getEmail() == null || reqobj.getEmail().isEmpty()){
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Enter The Valid Email"));
			}else {
				int mail_id_count = reqobj.getId() == 0 ? employeeRepo.getMailIdCount(reqobj.getEmail()) : employeeRepo.getMailIdCountByEmployeeId(reqobj.getId(),reqobj.getEmail());
				if(mail_id_count > 0) {
					return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already registered! , please use different Email"));
				}
			}
			
			if(reqobj.getAttendanceid() == null){
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Enter The Valid Attendance Id"));
			}else {
				int attendance_id_count = reqobj.getId() == 0 ? employeeRepo.getAttendanceIdCount(reqobj.getAttendanceid()): employeeRepo.getAttendanceIdCountByEmployeeId(reqobj.getId(),reqobj.getAttendanceid());
				if(attendance_id_count > 0) {
					return ResponseEntity.badRequest().body(new MessageResponse("Error: Duplicate Attendance Id"));
				}
			}
			
            boolean isReferenceNo = masterSettingsRepo.getById(1).isIsreference_no();
            
            if(isReferenceNo) {
            	if(reqobj.getReference_no() == null || reqobj.getReference_no() == 0) {
            		return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid Reference Id"));
            	}
            	
            	int reference_no_count = employeeRepo.getCountByReferenceNoAndEmployeeId(reqobj.getReference_no(),reqobj.getId());
            	
            	if(reference_no_count > 0) {
            		return ResponseEntity.badRequest().body(new MessageResponse("Error: Duplicate Reference Id"));
            	}
            }
			
//			if(epmployeeRepo.existsByAttendanceid(reqobj.getAttendanceid())) {
//				return ResponseEntity.badRequest().body(new MessageResponse("Error: Duplicate Attendance Id"));
//			}
			
			Department dept = departmentRepo.getById(reqobj.getDeptid());
			
			if(!reqobj.getRole().equalsIgnoreCase("ROLE_MANAGER")) {
				if(dept.getDept_head() == null){
					return ResponseEntity.badRequest().body(new MessageResponse("Error: Unable To Find Manager!"));
				}
			}
		 }catch(Exception e) {
			System.out.println("Error in UserValidation : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
		return ResponseEntity.ok(new MessageResponse("Success"));
	}
	
    @Override
	public ResponseEntity<?> deleteUser(Long employee_id,String username,HttpServletRequest servrequest) {
		try {
			
			String remoteAddress = servrequest.getRemoteAddr();
			
			if(!userRepo.existsByEmail(username)) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized User!"));
			}
			
			Employee empObj = employeeRepo.getById(employee_id);
			empObj.setIsdelete(true);
			
			employeeRepo.save(empObj);
			
			logService.ActivityLog(username, "Delete User",remoteAddress,empObj.getEmail());
			
			return ResponseEntity.ok(new MessageResponse("Deleted Successfully!"));
		}catch(Exception e) {
			System.out.println("Error in deleteUser : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
	}

	@Override
	public Map getEmployeeListByProject(Long project_id) {
		Map map = new HashMap<>();
        try {
    		 map.put("employee_list", employeeRepo.getEmployeeListByProject(project_id));
    		 map.put("assigned_employee_list", employeeRepo.getAssignedEmployeeListByProject(project_id));
    		 map.put("project", projectRepo.getById(project_id));
		}catch(Exception e) {
			System.out.println("Error in getEmployeeListByProject : "+e.getMessage());
		}
		return map;
	}

	@Override
	public String addUserByXLFile(MultipartFile file,HttpServletRequest servrequest) {
		try {
			String path = "/home/user/Documents";
			saveFile(path, file.getOriginalFilename(), file);
			List<List<String>> data = readExcelFile(path+"/"+file.getOriginalFilename());
			for (int i = 1; i < data.size(); i++) { // start from index 1 → 2nd line
			    List<String> row = data.get(i);

			    System.out.println("row.get(1): " + row.get(1).trim());
			    System.out.println("row.get(2): " +row.get(2).trim());
//			    System.out.println("row.get(3): " +row.get(3).trim());
//			    System.out.println("row.get(4): " +row.get(4).trim());
//			    System.out.println("row.get(5): " +row.get(5).trim());
//			    System.out.println("row.get(6): " +row.get(6).trim());
//			    System.out.println("row.get(8): " +row.get(8).trim());
//			    System.out.println("row.get(10): " +row.get(10).trim());
//			    System.out.println("row.get(11): " +row.get(11).trim());
//			    System.out.println("row.get(12): " +row.get(12).trim());
//			    System.out.println("row.get(13): " +row.get(13).trim());
			    System.out.println("row.get(14): " +row.get(14).trim());
//			    System.out.println("row.get(15): " +row.get(15).trim());
//			    System.out.println("row.get(16): " +row.get(16).trim());
			    
			    UserCreateRequest obj = new UserCreateRequest(row.get(1).trim(), row.get(2).trim(), row.get(3).trim(), row.get(4).trim(),
			    		row.get(5).trim(), row.get(6).trim(),Long.parseLong(row.get(8).trim()), Long.parseLong(row.get(10).trim()), Long.parseLong(row.get(11).trim()), row.get(12).trim(),
			    		row.get(13).trim(), row.get(14).trim(), null,null, Long.parseLong(row.get(16).trim()), row.get(15).trim(),"dsradmin@ridsys.com",null,"ROLE_EMPLOYEE");
			    
			    ResponseEntity responce = createUser(obj,servrequest);

				MessageResponse message = (MessageResponse) responce.getBody();
				
				System.out.println("=========================================================================message : "+message.getMessage());
			}
		}catch(Exception e) {
			System.out.println("Error in addUserByXLFile : "+e.getMessage());
		}
		return null;
	}
	
	public static List<List<String>> readExcelFile(String filePath) {
        List<List<String>> excelData = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath)) {
            Workbook workbook = null;
            if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (filePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(fis);
            }

            if (workbook != null) {
                Sheet sheet = workbook.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.iterator();

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    List<String> rowData = new ArrayList<>();

                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
//                        rowData.add(cell.toString());
                        rowData.add(convertCellToString(cell));
                    }
                    excelData.add(rowData);
                }
                workbook.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return excelData;
    }
	
    private static String convertCellToString(Cell cell) {
	     DataFormatter formatter = new DataFormatter();
	     return formatter.formatCellValue(cell);
    }
	
	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(uploadDir);

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

			try {
				String perm = "rwxrwxrwx";// 777
				Set<PosixFilePermission> permissions = PosixFilePermissions.fromString(perm);

				File filedir = new File(uploadDir);
				Files.setPosixFilePermissions(filedir.toPath(), permissions);

				filedir = new File(uploadDir + "/" + fileName);
				Files.setPosixFilePermissions(filedir.toPath(), permissions);
			} catch (IOException e) {
				// logger.warning("Can't set permission '" + perm + "' to " + dir.getPath());
				e.printStackTrace();
			}

		} catch (IOException ioe) {
			throw new IOException("Could not save image file: " + fileName, ioe);
		}
	}

	

}
