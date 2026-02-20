package com.example.dsr.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.dsr.model.Document;

@Repository	
public interface DocumentRepository extends JpaRepository<Document, Long> {
//
//	List<Document> findByProjectid(Integer projectid);
//
//	@Query(value = "select * from document where projectid = :projectid", nativeQuery = true)
//	String getProjectNameById(Long projectid);
	
	List<Document> findByProjectid(Integer projectid);

//	@Query(value = "SELECT d.*,CONCAT(e.firstname,' ',e.lastname) AS employee_name,REPLACE(d.filepath, '/var/www/html', s.link_url) AS file_url FROM document d LEFT JOIN employee e ON d.userid = e.id CROSS JOIN server_details s WHERE d.projectid =:projectid AND s.isactive = 1",nativeQuery = true);
	@Query(value = "SELECT d.*,CONCAT(e.firstname,' ',e.lastname) AS employee_name,REPLACE(d.filepath, '/var/www/html', s.link_url) AS file_url FROM document d LEFT JOIN employee e ON d.userid = e.id CROSS JOIN server_details s WHERE d.projectid =:projectid AND s.isactive = 1", nativeQuery = true)
	List<Map<String, String>> DocumentsByProjectId(Integer projectid);
}
