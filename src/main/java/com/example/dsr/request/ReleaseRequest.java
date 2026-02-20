package com.example.dsr.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseRequest {

	private Long id;
	private String code;
	private Long projectid;
	private String title;
	private String version;
	private String parent_version;
	private String release_type;
	private LocalDate planned_date;
	private LocalDate released_date;
	private Long assigned_to;
	private String status;
	private boolean isactive;
	private boolean isdelete;
	
	private String username;
	private String mail_content;
	private String file_url;
	private String file_name;
	private boolean ismail;
	
	
}
