package com.example.dsr.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.dsr.ApiResponse;
import com.example.dsr.model.Document;
import com.example.dsr.repository.DocumentRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/document")
public class DocumentController {
	private final DocumentRepository documentRepository;

	@Value("${file.upload-dir:/var/www/html/dsr/document}")
	private String uploadDir;

	public DocumentController(DocumentRepository documentRepository) {
		this.documentRepository = documentRepository;
	}


	@PostMapping("/upload")
	public ResponseEntity<ApiResponse> uploadDocument(@RequestParam("file") MultipartFile file,
			@RequestParam("projectId") Integer projectid, @RequestParam("userId") Integer userid,
			@RequestParam("title") String title) {

		try {
			if (file == null || file.isEmpty()) {
				return ResponseEntity.badRequest().body(new ApiResponse("File is required"));
			}

			Path uploadPath = Paths.get(uploadDir, String.valueOf(projectid));
			Files.createDirectories(uploadPath);
			String safeFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
			Path filePath = uploadPath.resolve(safeFileName);

			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			Files.setPosixFilePermissions(filePath, PosixFilePermissions.fromString("rwxrwxrwx"));
			
			Document document = new Document();
			document.setProjectid(projectid);
			document.setUserid(userid);
			document.setTitle(title);
			document.setFilepath(filePath.toString());
			document.setIsactive(true);
			documentRepository.save(document);

			return ResponseEntity
					.ok(new ApiResponse("File uploaded to project folder " + projectid + " successfully !!!"));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("File upload failed : " + e.getMessage()));
		}
	}


	@GetMapping("/documentList")
	public List<Map<String, String>> DocumentsByProjectId(@RequestParam("id") Integer projectid) {
	   return documentRepository.DocumentsByProjectId(projectid);
	}
	
	@GetMapping("/documentList/projectid")
	public ResponseEntity<List<Document>> getDocumentsByProjectId(@RequestParam("id") Integer projectid) {
	    List<Document> documents = documentRepository.findByProjectid(projectid);
	    if (documents.isEmpty()) {
	        return ResponseEntity.ok(new ArrayList<>()); 
	    }
	    return ResponseEntity.ok(documents);
	}

	@GetMapping("/documentList/id/{id}")
	public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {

		return documentRepository.findById(id).map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/download/{id}")
	public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws IOException {
		Document document = documentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Document record not found in DB"));

		Path path = Paths.get(document.getFilepath());
		Resource resource = new UrlResource(path.toUri());

		if (!resource.exists()) {
			throw new RuntimeException("Physical file not found at: " + document.getFilepath());
		}

		String contentType = Files.probeContentType(path);
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + document.getTitle() + "\"")
				.body(resource);
	}

}
