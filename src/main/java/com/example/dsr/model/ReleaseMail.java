package com.example.dsr.model;


import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "release_mail")
public class ReleaseMail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Long releaseid;
	private Long empid;
	private String subject;
	private String message;
	private String file_url;
	private boolean isactive;
	@CreationTimestamp
	private LocalDateTime created_date;
	
	public ReleaseMail(Long releaseid, Long empid, String subject, String message, String file_url, boolean isactive) {
		super();
		this.releaseid = releaseid;
		this.empid = empid;
		this.subject = subject;
		this.message = message;
		this.file_url = file_url;
		this.isactive = isactive;
	}
}
