package com.example.dsr.serviceImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dsr.DTO.AttendanceDTO;
import com.example.dsr.model.AttendanceDetails;
import com.example.dsr.model.Comments;
import com.example.dsr.model.Employee;
import com.example.dsr.model.ScheduleLog;
import com.example.dsr.model.Task;
import com.example.dsr.repository.AttendanceDetailsRepository;
import com.example.dsr.repository.AttendanceProjection;
import com.example.dsr.repository.CommentsRepository;
import com.example.dsr.repository.EmployeeRepository;
import com.example.dsr.repository.LogRepository;
import com.example.dsr.repository.ScheduleLogRepository;
import com.example.dsr.repository.UserRepository;
import com.example.dsr.responce.MessageResponse;
import com.example.dsr.service.CommentsService;
import com.example.dsr.service.LogService;




@Service
public class CommentsServiceImplementation implements CommentsService{
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	CommentsRepository commentsRepo;
	
	@Autowired
	ScheduleLogRepository scheduleLogRepo;
	
	@Autowired
	LogService logService;
	
	@Autowired
	EmployeeRepository employeeRepo;
	
	@Autowired
	AttendanceDetailsRepository attendanceDetailsRepo;

	@Override
	public ResponseEntity<?> createComments(Comments reqobj,HttpServletRequest servrequest) {
	    try{
	    	
            String remoteAddress = servrequest.getRemoteAddr();
			
            ResponseEntity responce = Validation(reqobj);

			MessageResponse message = (MessageResponse) responce.getBody();
			
		    if (!message.getMessage().equals("Success")) {
				return responce;
			}
		    
		    long max_id = 0;
			try {
				max_id = commentsRepo.getMaxId() + 1;
			}catch(Exception e) {
				max_id = max_id + 1;
			}
			
			String code = String.format("%06d", max_id);
			reqobj.setCode("CMD" + code);
			
		    commentsRepo.save(reqobj);
		    
		    Employee employee = employeeRepo.getById(reqobj.getEmpid());
		    
		    String action = null;
		    ScheduleLog sch_log = new ScheduleLog();
		   
		    if(reqobj.getTaskid() != 0) {
		    	action = "Add Comments For Task";
		    	sch_log = new ScheduleLog(reqobj.getEmpid(), reqobj.getTaskid(),null,null,null,action,"Comments created by "+employee.getFirstname()+" "+employee.getLastname(),null);
		    }else if(reqobj.getSub_taskid() != 0) {
		    	action = "Add Comments For Sub-task";
		    	sch_log = new ScheduleLog(reqobj.getEmpid(), null,reqobj.getSub_taskid(),null,null,action,"Comments created by "+employee.getFirstname()+" "+employee.getLastname(),null);
		    }else if(reqobj.getPhaseid() != 0) {
		    	action = "Add Comments For Phase";
		    	sch_log = new ScheduleLog(reqobj.getEmpid(), null,null,reqobj.getPhaseid(),null,action,"Comments created by "+employee.getFirstname()+" "+employee.getLastname(),null);
		    }else if(reqobj.getReleaseid() != 0) {
		    	action = "Add Comments For Release";
		    	sch_log = new ScheduleLog(reqobj.getEmpid(), null,null,null,reqobj.getReleaseid(),action,"Comments created by "+employee.getFirstname()+" "+employee.getLastname(),null);
		    }
		    
		    scheduleLogRepo.save(sch_log);
			
			logService.ActivityLog(employee.getEmail(),action, remoteAddress,reqobj.getCode());
			
	    }catch(Exception e) {
		    System.out.println("Error in createComments : "+e.getMessage());
		    return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
	    }
	    return ResponseEntity.ok(new MessageResponse("Success!"));
	}
	
	@Override
	public ResponseEntity<?> updateComments(Comments reqobj, HttpServletRequest servrequest) {
		try {
            String remoteAddress = servrequest.getRemoteAddr();
			
            ResponseEntity responce = Validation(reqobj);

			MessageResponse message = (MessageResponse) responce.getBody();
			
		    if (!message.getMessage().equals("Success")) {
				return responce;
			}
		    
		    commentsRepo.save(reqobj);
		    
           Employee employee = employeeRepo.getById(reqobj.getEmpid());
		    
		    String action = null;
		    ScheduleLog sch_log = new ScheduleLog();
		   
		    if(reqobj.getTaskid() != 0) {
		    	action = "Edit Comments For Task";
		    	sch_log = new ScheduleLog(reqobj.getEmpid(), reqobj.getTaskid(),null,null,null,action,"Comments edited by "+employee.getFirstname()+" "+employee.getLastname(),null);
		    }else if(reqobj.getSub_taskid() != 0) {
		    	action = "Edit Comments For Sub-task";
		    	sch_log = new ScheduleLog(reqobj.getEmpid(), null,reqobj.getSub_taskid(),null,null,action,"Comments edited by "+employee.getFirstname()+" "+employee.getLastname(),null);
		    }else if(reqobj.getPhaseid() != 0) {
		    	action = "Edit Comments For Phase";
		    	sch_log = new ScheduleLog(reqobj.getEmpid(), null,null,reqobj.getPhaseid(),null,action,"Comments edited by "+employee.getFirstname()+" "+employee.getLastname(),null);
		    }else if(reqobj.getReleaseid() != 0) {
		    	action = "Edit Comments For Release";
		    	sch_log = new ScheduleLog(reqobj.getEmpid(), null,null,null,reqobj.getReleaseid(),action,"Comments edited by "+employee.getFirstname()+" "+employee.getLastname(),null);
		    }
		    
		    scheduleLogRepo.save(sch_log);
			
			logService.ActivityLog(employee.getEmail(),action, remoteAddress,reqobj.getCode());
		}catch(Exception e) {
		    System.out.println("Error in updateComments : "+e.getMessage());
		    return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
	    }
	    return ResponseEntity.ok(new MessageResponse("Success!"));
	}

	private ResponseEntity Validation(Comments reqobj) {
		try {
			if(!userRepo.existsById(reqobj.getEmpid())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized User!"));
			}
			
			if(reqobj.getTaskid() == 0 && reqobj.getSub_taskid() == 0 && reqobj.getPhaseid() == 0 && reqobj.getReleaseid() == 0) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Something went wrong!"));
			}
			
			reqobj.setComments(reqobj.getComments().trim());
			if(reqobj.getComments() == null || reqobj.getComments().isEmpty()){
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Enter the valid comments!"));
			}
			
		}catch(Exception e) {
		    System.out.println("Error in Comments Validation : "+e.getMessage());
		    return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
	    }
	    return ResponseEntity.ok(new MessageResponse("Success"));
	}

	@Override
	public List<Comments> getCommentsById(String type, Long id) {
		List<Comments> list = new ArrayList<>();
		try {
			list = commentsRepo.getCommentsByIdAndType(type,id).stream().peek(cmd -> {
				cmd.setEmployee_name(employeeRepo.getNameById(cmd.getEmpid()));
			}).collect(Collectors.toList());
		}catch(Exception e) {
			System.out.println("Error in getCommentsById : "+e.getMessage());
		}
		return list;
	}

	@Override
	public ResponseEntity<?> deleteComments(Long id, String username, HttpServletRequest servrequest) {
		try {
            String remoteAddress = servrequest.getRemoteAddr();
			
			if(!userRepo.existsByEmail(username)) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized User!"));
			}
			
			Comments cmd = commentsRepo.getById(id);
			cmd.setIsdelete(false);
			commentsRepo.save(cmd);
			
			Employee employee = employeeRepo.getByEmail(username);
			
			String action = null;
		    ScheduleLog sch_log = new ScheduleLog();
		    
			if(cmd.getTaskid() != 0) {
		    	action = "Delete Comments For Task";
		    	sch_log = new ScheduleLog(cmd.getEmpid(), cmd.getTaskid(),null,null,null,action,"Comments deleted by "+employee.getFirstname()+" "+employee.getLastname(),null);
		    }else if(cmd.getSub_taskid() != 0) {
		    	action = "Delete Comments For Sub-task";
		    	sch_log = new ScheduleLog(cmd.getEmpid(), null,cmd.getSub_taskid(),null,null,action,"Comments deleted by "+employee.getFirstname()+" "+employee.getLastname(),null);
		    }else if(cmd.getPhaseid() != 0) {
		    	action = "Delete Comments For Phase";
		    	sch_log = new ScheduleLog(cmd.getEmpid(), null,null,cmd.getPhaseid(),null,action,"Comments deleted by "+employee.getFirstname()+" "+employee.getLastname(),null);
		    }else if(cmd.getReleaseid() != 0) {
		    	action = "Delete Comments For Release";
		    	sch_log = new ScheduleLog(cmd.getEmpid(), null,null,null,cmd.getReleaseid(),action,"Comments deleted by "+employee.getFirstname()+" "+employee.getLastname(),null);
		    }
			
			scheduleLogRepo.save(sch_log);
			
			logService.ActivityLog(employee.getEmail(),action, remoteAddress,cmd.getCode());
			
			return ResponseEntity.ok(new MessageResponse("Deleted Successfully!"));
		}catch(Exception e) {
			 System.out.println("Error in deleteComments : "+e.getMessage());
			 return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
	}
	
	@Override
	public ResponseEntity<?> loadAttendance(MultipartFile file) {

	    if (file == null || file.isEmpty()) {
	        return ResponseEntity.badRequest().body(new MessageResponse("File is empty or not provided"));
	    }
	    
	    LocalDateTime latest_in_time = attendanceDetailsRepo.getLastIntime();
	    
	    latest_in_time = latest_in_time == null ? LocalDateTime.parse("2026-01-01T00:00:00") : latest_in_time;
	    
	    System.out.println("latest_in_time : "+latest_in_time);
	    
	    List<AttendanceDetails> attendanceList = new ArrayList<>();

	    List<String> processedRows = new ArrayList<>();

	    try (BufferedReader br =
	                 new BufferedReader(new InputStreamReader(file.getInputStream()))) {

	        String line;
	        int lineNumber = 0;

	        while ((line = br.readLine()) != null) {

	            lineNumber++;

	            // Trim whitespace
	            line = line.trim();

	            // Skip empty lines
	            if (line.isEmpty()) {
	                continue;
	            }

	            // Split by whitespace (tab or spaces)
	            String[] parts = line.split("\\s+");

	            if (parts.length < 7) {
	                System.out.println("Invalid row at line: " + lineNumber);
	                continue;
	            }
	            
	            

	            int id = Integer.parseInt(parts[0]);
	            String dateTime = parts[1] + " " + parts[2];
	            int v1 = Integer.parseInt(parts[3]);
	            int v2 = Integer.parseInt(parts[4]);
	            int v3 = Integer.parseInt(parts[5]);
	            int v4 = Integer.parseInt(parts[6]);

		           
		           
	            
	            // Example: just storing processed info
	            processedRows.add("Processed ID: " + id + " at line " + lineNumber);
	           
	            if (latest_in_time.isBefore(LocalDateTime.parse(dateTime.replace(" ","T")))) {
	            	
	            	AttendanceDetails obj = new AttendanceDetails();
	            	
	            	obj.setAttendanceid((long) id);
	            	obj.setAttendance_date(LocalDateTime.parse(dateTime.replace(" ","T")).toLocalDate());
	            	obj.setShift_id((long) 1);
	            	obj.setPunch_time(LocalDateTime.parse(dateTime.replace(" ","T")));
	            	attendanceList.add(obj);
                }
               }
	        
	        attendanceDetailsRepo.saveAll(attendanceList);

	        return ResponseEntity.ok(new MessageResponse("Uploaded successfully. Total rows: "+ processedRows.size()));

	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
	    }
	}


//	@Override
//	public ResponseEntity<?> loadAttendance(MultipartFile file) {
//
//	    try (BufferedReader br = 
//	            new BufferedReader(new InputStreamReader(file.getInputStream()))) {
//
//	        String line;
//
//	        while ((line = br.readLine()) != null) {
//
//	            line = line.trim();
//	            if (line.isEmpty()) continue;
//
//	            String[] parts = line.split("\\s+");
//
//	            int id = Integer.parseInt(parts[0]);
//	            String dateTime = parts[1] + " " + parts[2];
//	            int v1 = Integer.parseInt(parts[3]);
//	            int v2 = Integer.parseInt(parts[4]);
//	            int v3 = Integer.parseInt(parts[5]);
//	            int v4 = Integer.parseInt(parts[6]);
//	            
//	            System.out.println("id : "+id);
//	            System.out.println("dateTime : "+dateTime);
//	            System.out.println("v1 : "+v1);
//	            System.out.println("v2 : "+v2);
//	            System.out.println("v3 : "+v3);
//	            System.out.println("v4 : "+v4);
//
//	            
//	        }
//
//	        return ResponseEntity.ok(
//	                new MessageResponse("File processed successfully: "
//	                        + file.getOriginalFilename()));
//
//	    } catch (Exception e) {
//	        return ResponseEntity.badRequest()
//	                .body(new MessageResponse("Error: " + e.getMessage()));
//	    }
//	}
	
	@Override
	public List<AttendanceDTO> getAttendance(Long id, String fromdate, String todate) {

	    List<AttendanceDTO> result = new ArrayList<>();

	    LocalDate from = LocalDate.parse(fromdate);
	    LocalDate to = LocalDate.parse(todate);

	    long days = ChronoUnit.DAYS.between(from, to);

	    for (int i = 0; i <= days; i++) {

	        LocalDate d = from.plusDays(i);
	        String date = d.toString();

	        AttendanceProjection e = null;

	        if(attendanceDetailsRepo.checkAD(date,id)>0){
	            e = attendanceDetailsRepo.getAD(date,id);
	        }
	        else if(attendanceDetailsRepo.checkDSR(date,id)>0){
	            e = attendanceDetailsRepo.getDSR(date,id);
	        }
	        else if(attendanceDetailsRepo.checkOnDuty(date,id)>0){
	            e = attendanceDetailsRepo.getOnDuty(date,id);
	        }
	        else if(attendanceDetailsRepo.checkHoliday(date)>0){
	            e = attendanceDetailsRepo.getHoliday(date);
	        }
	        String empname=employeeRepo.getNameById(id);
	        System.out.println("DATE="+date+" ID="+id);
	        System.out.println("AD count="+attendanceDetailsRepo.checkAD(date,id));
	        System.out.println("DSR count="+attendanceDetailsRepo.checkDSR(date,id));
	        if (e != null) {

	            AttendanceDTO p = convertToDTO(e,d);
p.setEmployeeName(empname);
System.out.println("date ------"+p.getAttendanceDate());

	            calculateThumbStatus(p);
	            calculateWorkedHours(p);

	            result.add(p);
	        }
	    }
	    return result;
	}

	private AttendanceDTO convertToDTO(AttendanceProjection e, LocalDate d){

	    AttendanceDTO p = new AttendanceDTO();

	    p.setAttendanceId(e.getAttendanceId());
	    p.setAttendanceDate(e.getAttendanceDate());
	    p.setStatus(e.getStatus());
	    p.setAlltime(e.getAlltime());
	    p.setDay(d.getDayOfWeek().toString());

	    return p;
	}
	private void calculateThumbStatus(AttendanceDTO e) {

	    if (e.getAlltime() == null ||
	        "-".equals(e.getAlltime()) ||
	        e.getIntime() == null) return;

	    String[] thumb = e.getAlltime().split("-");

	    boolean improper = (thumb.length % 2 != 0);

	    String[] t = e.getIntime().split(" ")[1].split(":");

	    int hour = Integer.parseInt(t[0]);
	    int min  = Integer.parseInt(t[1]);

	    boolean late = hour > 9 || (hour == 9 && min > 30);

	    if (improper && late)
	        e.setStatusemp("Improper Thumb & Late");
	    else if (improper)
	        e.setStatusemp("Improper Thumb");
	    else if (late)
	        e.setStatusemp("Late");
	    else
	        e.setStatusemp("Proper Thumb");
	}
	
	private void calculateWorkedHours(AttendanceDTO e) {

	    if (e.getAlltime() == null ||
	        "-".equals(e.getAlltime())) {
	        return;
	    }

	    try {

	        String[] str = e.getAlltime().split("-");

	        int count = str.length;

	        if (count % 2 != 0) {
	            e.setStatusemp("Improper Thumb");
//	            e.setLatetemp("#808080");
	            return;
	        }

	        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

	        long totalMillis = 0;

	        for (int i = 0; i < count; i += 2) {

	            Date in = format.parse(str[i].trim());
	            Date out = format.parse(str[i + 1].trim());

	            totalMillis += (out.getTime() - in.getTime());
	        }

	        long hours = (totalMillis / (1000 * 60 * 60));
	        long minutes = (totalMillis / (1000 * 60)) % 60;
	        long seconds = (totalMillis / 1000) % 60;

	        String duration = checkLength(hours, minutes, seconds);

	        e.setWorkedhoursdash(duration);

	        if (hours < 8) {
	            e.setStatusemp("Wor.hrs shortage");
	        }

	        if ("1".equals(e.getStatus())) {
	            e.setStatusemp("Improper Thumb & Late");
	        } else if ("2".equals(e.getStatus())) {
	            e.setStatusemp("Improper Thumb");
	        } else if ("3".equals(e.getStatus())) {
	            e.setStatusemp("Late");
	        } else if ("4".equals(e.getStatus())) {
	            e.setStatusemp("Proper Thumb");
	        }

	        if ("4".equals(e.getStatus()) && hours < 8) {
	            e.setStatusemp("Wor.Hrs Shortage");
	        }

	    } catch (Exception ex) {
	        e.setWorkedhoursdash("-");
	    }
	}
	private String checkLength(long h, long m, long s) {
	    return String.format("%02d:%02d:%02d", h, m, s);
	}
	
}
