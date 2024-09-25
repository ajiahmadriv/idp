package com.example.aeon.controller;

import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.aeon.dao.FileResponse;
import com.example.aeon.service.FileStorageService;
import com.example.aeon.utils.TemplateResponse;

@RestController
@RequestMapping("/upload")
public class UploadFileController {

	@Autowired
	private FileStorageService fileStorageService;
	@Autowired
	public TemplateResponse templateResponse;

	@PostMapping("/")
	public ResponseEntity<Map> upload(@RequestParam("file") MultipartFile file) {
		try {
			fileStorageService.save(file);
			FileResponse response = new FileResponse();
			response.setFileName(file.getOriginalFilename());
			response.setFileDownloadUri("http://localhost:9090/api/upload/showFile/" + file.getOriginalFilename());
			response.setFileType(FilenameUtils.getExtension(file.getOriginalFilename()));
			response.setSize(file.getSize());
			response.setError("false");
			return ResponseEntity.status(HttpStatus.OK).body(templateResponse.templateSukses(response));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(templateResponse.templateSukses("Gagal"));
		}
	}

	@GetMapping("/showFile/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		Resource file = fileStorageService.load(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

}
