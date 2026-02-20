package com.example.dsr.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.dsr.model.ReleaseMail;

public interface ReleaseMailRepository extends JpaRepository<ReleaseMail, Long>{

	@Query(value = "select * from release_mail where releaseid =:id order by id desc",nativeQuery = true)
    List<ReleaseMail> collectAllMailsByRelease(Long id);

	List<ReleaseMail> findAllByReleaseid(Long id);
	
	@Query(value = "select r.*,REPLACE(r.file_url, '/var/www/html', s.link_url) AS file_path,concat(e.firstname,'',e.lastname) as employee_name from release_mail r left join employee e on r.empid = e.id CROSS JOIN server_details s where r.releaseid =:id and r.isactive = 1",nativeQuery = true)
	List<Map<String,String>> getAllMailsByRelease(Long id);


}
