package com.example.dsr.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.dsr.DTO.ReleaseDTO;
import com.example.dsr.model.Employee;
import com.example.dsr.model.Project;
import com.example.dsr.model.ReleaseDetails;
import com.example.dsr.model.ReleaseMail;
import com.example.dsr.model.ScheduleLog;
import com.example.dsr.model.ServerDetails;
import com.example.dsr.repository.EmployeeRepository;
import com.example.dsr.repository.ProjectRepository;
import com.example.dsr.repository.ReleaseDetailsRepository;
import com.example.dsr.repository.ReleaseMailRepository;
import com.example.dsr.repository.ScheduleLogRepository;
import com.example.dsr.repository.ServerDetailsRepository;
import com.example.dsr.repository.UserRepository;
import com.example.dsr.request.ReleaseRequest;
import com.example.dsr.responce.MessageResponse;
import com.example.dsr.service.LogService;
import com.example.dsr.service.ReleaseService;
import org.apache.commons.io.FilenameUtils;


@Service
public class ReleaseServiceImplementation implements ReleaseService{
	
	@Autowired
	ReleaseDetailsRepository releaseDetailsRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	EmployeeRepository employeeRepo;
	
	@Autowired
	ScheduleLogRepository scheduleLogRepo;
	
	@Autowired
	LogService logService;
	
	@Autowired
	ReleaseMailRepository releaseMailRepo;
	
	@Autowired
	ProjectRepository projectRepo;
	
	@Autowired
	ServerDetailsRepository serverDetailsRepo;

	@Override
	public ResponseEntity<?> createRelease(ReleaseRequest reqobj, HttpServletRequest servrequest) {
		try {
            String remoteAddress = servrequest.getRemoteAddr();
			
            ResponseEntity responce = Validation(reqobj);

			MessageResponse message = (MessageResponse) responce.getBody();
			
		    if (!message.getMessage().equals("Success")) {
				return responce;
			}
			
		    long max_id = 0;
			try {
				max_id = releaseDetailsRepo.getMaxId() + 1;
			}catch(Exception e) {
				max_id = max_id + 1;
			}
			
			String code = String.format("%06d", max_id);
			reqobj.setCode("RLS" + code);
			
			ReleaseDetails release = new ReleaseDetails(reqobj.getCode(),reqobj.getProjectid(),reqobj.getTitle(),reqobj.getVersion(),reqobj.getParent_version() ,reqobj.getRelease_type(),reqobj.getPlanned_date(),reqobj.getReleased_date(),
					reqobj.getAssigned_to(),reqobj.getStatus(),true, false);
			
			releaseDetailsRepo.save(release);
			
			release = releaseDetailsRepo.getByCode(reqobj.getCode());
			
			Employee employee = employeeRepo.getByEmail(reqobj.getUsername());
			
			String createdby = employee.getFirstname()+" "+employee.getLastname();
            String assignedto = employeeRepo.getNameById(reqobj.getAssigned_to());
			
			if(reqobj.isIsmail()) {
				
				String project_name = projectRepo.getProjectNameById(release.getProjectid());
				
				String subject = release.getRelease_type()+" Release For "+project_name+" - "+release.getVersion();
				
				String file_path = null;
				if(reqobj.getFile_url() != null) {
					
					 Random random = new Random();
                     int number = 1000 + random.nextInt(9000); // 1000–9999
                     
                     String dir_path = "/var/www/html/R-SPACE/release_document/";
                     
                     createDirIfNotExists(dir_path);
                     
                     String extension = FilenameUtils.getExtension(reqobj.getFile_name());
                     
                     file_path = dir_path+project_name+"_"+release.getVersion()+"_"+number+"."+extension;
                     
                     fileUpload(reqobj.getFile_url(),file_path);
				}
				
				ReleaseMail mail = new ReleaseMail(release.getId(),employee.getId(),subject,reqobj.getMail_content(),file_path,true);
				releaseMailRepo.save(mail);
				
				send_mail(createdby,employee.getEmail(),reqobj.getMail_content(),release,subject,file_path);
			}
			
			ScheduleLog sch_log = new ScheduleLog(employee.getId(), null,null,null,release.getId(),"Release Creation","Release created by "+createdby+" &  assigned to "+assignedto,release.getStatus());
			scheduleLogRepo.save(sch_log);
			
			ScheduleLog sch_log1 = new ScheduleLog(employee.getId(), null,null,null,release.getId(),"Release Creation","Status changed, Release opened by "+createdby,release.getStatus());
			scheduleLogRepo.save(sch_log1);

			logService.ActivityLog(employee.getEmail(),"Release Creation", remoteAddress, release.getCode());
		}catch(Exception e) {
			System.out.println("Error in createRelease : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
		return ResponseEntity.ok(new MessageResponse("Created successfully!"));
	}
	
	@Override
	public ResponseEntity<?> updateRelease(ReleaseRequest reqobj, HttpServletRequest servrequest) {
		try {
            String remoteAddress = servrequest.getRemoteAddr();
			
            ResponseEntity responce = Validation(reqobj);

			MessageResponse message = (MessageResponse) responce.getBody();
			
		    if (!message.getMessage().equals("Success")) {
				return responce;
			}
		    
		    ReleaseDetails old_obj =  releaseDetailsRepo.getByCode(reqobj.getCode());
		    
		    Employee employee = employeeRepo.getByEmail(reqobj.getUsername());
			
			String createdby = employee.getFirstname()+" "+employee.getLastname();
            String assignedto = employeeRepo.getNameById(reqobj.getAssigned_to());
            
            String remarks = null;
		    
		    if(!old_obj.getPlanned_date().equals(reqobj.getPlanned_date()) || !old_obj.getReleased_date().equals(reqobj.getReleased_date())){ 
				remarks = "Release rescheduled by "+createdby;
			}else if(reqobj.getAssigned_to() != null) {

				String assignee_name = employeeRepo.getNameById(reqobj.getAssigned_to());
				
				if(old_obj.getAssigned_to() == null && reqobj.getAssigned_to() != null) {
					remarks = "Release assigned to "+assignee_name+" by "+createdby;
				}else if((!old_obj.getStatus().equals("Open") && reqobj.getStatus().equals("Open") && old_obj.getAssigned_to() != reqobj.getAssigned_to())) {
					remarks = "Status changed, Release re-opened and reassigned to "+assignee_name+" by "+createdby;
				}else if(old_obj.getAssigned_to() != null && old_obj.getAssigned_to() != reqobj.getAssigned_to()) {
					remarks = "Release reassigned to "+assignee_name+" by "+createdby;
				}else{
					
					if(!old_obj.getStatus().equals("Open") && reqobj.getStatus().equals("Open")) {
						remarks = "Status changed, Release re-opened by "+createdby;
					}else if(!old_obj.getStatus().equals("Closed") && reqobj.getStatus().equals("Closed")) {
						remarks = "Status changed, Release closed by "+createdby;
					}else if(!old_obj.getStatus().equals(reqobj.getStatus())) {
						remarks = "Status changed, Release status changed from "+old_obj.getStatus()+" to "+reqobj.getStatus()+" by "+createdby;
					}else {
						remarks = "Release data edited by "+createdby;
					}
	            }
			}else if(!old_obj.getStatus().equals("Open") && reqobj.getStatus().equals("Open")) {
				remarks = "Status changed, Release re-opened by "+createdby;
			}else if(!old_obj.getStatus().equals("Closed") && reqobj.getStatus().equals("Closed")) {
				remarks = "Status changed, Release closed by "+createdby;
			}else if(!old_obj.getStatus().equals(reqobj.getStatus())) {
				remarks = "Status changed, Release status changed from "+old_obj.getStatus()+" to "+reqobj.getStatus()+" by "+createdby;
			}else{
				remarks = "Release data edited by "+createdby;
            }
		    
		    ReleaseDetails release = new ReleaseDetails(reqobj.getId(), reqobj.getCode(), reqobj.getProjectid(), reqobj.getTitle(), reqobj.getVersion(),reqobj.getParent_version(), reqobj.getRelease_type(),
		    		reqobj.getPlanned_date(), reqobj.getReleased_date(), reqobj.getAssigned_to(), reqobj.getStatus(), reqobj.isIsactive());
		    
		    releaseDetailsRepo.save(release);
		    
            release = releaseDetailsRepo.getByCode(reqobj.getCode());
			
			if(reqobj.isIsmail()) {
				
				String project_name = projectRepo.getProjectNameById(release.getProjectid());
				
				String subject;
				
				if(reqobj.getStatus().equalsIgnoreCase("Rejected") || reqobj.getStatus().equalsIgnoreCase("Completed") || reqobj.getStatus().equalsIgnoreCase("Failed")
						|| reqobj.getStatus().equalsIgnoreCase("Passed")) {
					 subject = release.getRelease_type()+" Release For "+project_name+" - "+release.getVersion()+" - "+reqobj.getStatus();
				}else {
					 subject = release.getRelease_type()+" Release For "+project_name+" - "+release.getVersion();
				}
				
				String file_path = null;
				if(reqobj.getFile_url() != null) {
					
					 Random random = new Random();
                     int number = 1000 + random.nextInt(9000); // 1000–9999
                     
                     String dir_path = "/var/www/html/R-SPACE/release_document/";
                     
                     createDirIfNotExists(dir_path);
                     
                     String extension = FilenameUtils.getExtension(reqobj.getFile_name());
                     
                     file_path = dir_path+project_name+"_"+release.getVersion()+"_"+number+"."+extension;
                     
                     fileUpload(reqobj.getFile_url(),file_path);
				}
				
				ReleaseMail mail = new ReleaseMail(release.getId(),employee.getId(),subject,reqobj.getMail_content(),file_path,true);
				releaseMailRepo.save(mail);
				
				send_mail(createdby,employee.getEmail(),reqobj.getMail_content(),release,subject,file_path);
			}
			
			ScheduleLog sch_log = new ScheduleLog(employee.getId(), null,null,null,release.getId(),"Edit Release Details",remarks,release.getStatus());
			scheduleLogRepo.save(sch_log);
			
			logService.ActivityLog(employee.getEmail(),"Release Creation", remoteAddress, release.getCode());
		    
		}catch(Exception e) {
			System.out.println("Error in updateRelease : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
		return ResponseEntity.ok(new MessageResponse("Updated successfully!"));
	}
	
	@Override
	public List<ReleaseDTO> getReleaseDetailsForOverview(Long employee_id,Long projectid, String fromdate, String todate) {
		List<ReleaseDTO> list = new ArrayList<>();
		try { 
             list = releaseDetailsRepo.getReleaseDetailsForOverview(employee_id,projectid,fromdate,todate);
		}catch(Exception e) {
			System.out.println("Error in getReleaseDetailsForOverview : "+e.getMessage());
		}
		return list;
	}
	
	private void send_mail(String createdby,String email,String msg,ReleaseDetails release,String subject,String file_path){
        try{
            Multipart multipart = new MimeMultipart();
            MimeBodyPart textPart = new MimeBodyPart();
            MimeBodyPart attachmentPart = new MimeBodyPart();
            
            ServerDetails server = serverDetailsRepo.getServerDetails();
            
            String mail_content = null;
           
            try{
            	 File file = null;
                 mail_content = "Created by "+createdby+"\n\n"+msg;
                    
                 if(file_path != null) {
                     file = new File(file_path);
                 }
                 
                 if (file != null && file.exists()) {
                      attachmentPart.attachFile(file);
                      multipart.addBodyPart(attachmentPart);
                 } 
            }catch(Exception e){
                System.out.println("Error : "+e.getMessage());
            }
            
            List<ReleaseMail> mail_list = releaseMailRepo.collectAllMailsByRelease(release.getId());
            
            if(!mail_list.isEmpty() && mail_list.size() > 1){
                for(int i=1;i<mail_list.size();i++){
                	ReleaseMail mail = mail_list.get(i);
                    Employee employee = employeeRepo.getById(mail.getEmpid());
                    mail_content = mail_content+"\n\n\n\n"+"On "+mail.getCreated_date()+", "+employee.getFirstname()+" "+employee.getLastname()+" wrote:\n\nSubject : "+mail.getSubject()+"\n\n"+mail.getMessage();
                }
            }
            
            textPart.setText(mail_content);
            
            Project project = projectRepo.getById(release.getProjectid());
            
            String to_mail = employeeRepo.getMailById(release.getAssigned_to());

            Properties props = new Properties();
            props.put("mail.smtp.host", server.getMail_host());
            props.put("mail.smtp.port", server.getMail_port());
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "false");  // no TLS
            props.put("mail.smtp.ssl.enable", "false");       // no SSL

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(server.getMail(), server.getMail_password());
                }
            });
//            session.setDebug(true); // enable debug output
 
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(server.getMail()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to_mail));
            message.setSubject(subject);

            multipart.addBodyPart(textPart);
            message.setContent(multipart);
            
            List<String> cc_list = employeeRepo.getEmployeeMailsByProject(project.getId());
            
            for(String cc : cc_list){
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
            }

            Transport.send(message);
           
        }catch(Exception e){
            System.out.println("EXCEPTION--- " +e.getMessage());
        }
    }
	
	public void fileUpload(String data_url,String file_path) {
		try {
//			Path uploadPath = Paths.get(dir_path);
	        
//	        String file_path = dir_path+"/"+file_name;
	        
	        Path path = Paths.get(file_path);
	      
	        byte[] bytes = Base64.getDecoder().decode(data_url.split(",")[1]);
			Files.write(path, bytes);
			
			Files.setPosixFilePermissions(path, PosixFilePermissions.fromString("rwxrwxrwx"));
			
		}catch(Exception e) {
			System.out.println("Error in release file Upload : "+e.getMessage());
		}
	}

	private ResponseEntity Validation(ReleaseRequest reqobj) {
		try {
			if(!userRepo.existsByEmail(reqobj.getUsername())) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Unauthorized User!"));
			}
			
			if(reqobj.getProjectid() == null || reqobj.getProjectid() == 0) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid project details!"));
			}
			
			reqobj.setTitle(reqobj.getTitle().trim());
			if(reqobj.getTitle() == null || reqobj.getTitle().isEmpty()){
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid release title!"));
			}
			
			reqobj.setVersion(reqobj.getVersion().trim());
			if(reqobj.getVersion() == null || reqobj.getVersion().isEmpty()){
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Enter the valid release version!"));
			}
			
			if(reqobj.getStatus() == null || reqobj.getStatus().trim().equals("")) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid release status!"));
			}
			
			if (reqobj.getPlanned_date().isAfter(reqobj.getReleased_date())) {
        	    return ResponseEntity.badRequest().body(new MessageResponse("Error: Planed date cannot be after Released date"));
        	}
			
			if(reqobj.getAssigned_to() == null || reqobj.getAssigned_to() == 0) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid assignee details!"));
			}
			
			if(reqobj.isIsmail()) {
				reqobj.setMail_content(reqobj.getMail_content().trim());
				if(reqobj.getMail_content() == null || reqobj.getMail_content().isEmpty()){
					return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid mail content!"));
				}
			}
		}catch(Exception e) {
			System.out.println("Error in release Validation : "+e.getMessage());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: "+e.getMessage()));
		}
		return ResponseEntity.ok(new MessageResponse("Success"));
	}

	@Override
	public List<ReleaseDetails> getAllActiveReleaseByProject(Long project_id) {
		List<ReleaseDetails> list = new ArrayList<>();
	    try{
	    	list = releaseDetailsRepo.getAllActiveReleaseByProject(project_id).stream()
	    	        .peek(obj -> {
	    	            obj.setAssignee_name(
	    	                employeeRepo.getNameById(obj.getAssigned_to())
	    	            );
	    	        }).collect(Collectors.toList());
        }catch(Exception e) {
		    System.out.println("Error in getAllActiveReleaseByProject : "+e.getMessage());
	    }
	    return list;
	}
	
	private boolean createDirIfNotExists(String uploadDir) throws IOException {
		Path uploadPath = Paths.get(uploadDir);
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
			setPermissions(uploadDir);
			return true;
		} else {
			return true;
		}
	}
	
	public void setPermissions(String path) {
		try {
			String perm = "rwxrwxrwx";// in octal = 777
			Set<PosixFilePermission> permissions = PosixFilePermissions.fromString(perm);

			File filedir = new File(path);
			Files.setPosixFilePermissions(filedir.toPath(), permissions);
        } catch (IOException e) {
			e.printStackTrace();
		}
    }

	@Override
	public List<Map<String,String>> getReleaseMaildetailsById(Long id) {
		List<Map<String,String>> list = new ArrayList();
		try {
			list = releaseMailRepo.getAllMailsByRelease(id);
			
//			for(Map m : list) {
//				if(m.get("file_url") != null){
//					
//					String fileurl = (String) m.get("file_url");
////					obj.setFile_url(obj.getFile_url().replace("/var/www/html", serverDetailsRepo.getServerDetails().getLink_url()));
//					m.replace("file_url", fileurl.replace("/var/www/html", serverDetailsRepo.getServerDetails().getLink_url()));
//				}
//			}
		}catch(Exception e) {
			System.out.println("Error in getReleaseMaildetailsById : "+e.getMessage());
		}
		return list;
	}

	

}
