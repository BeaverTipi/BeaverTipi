package kr.or.ddit.main.map.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.util.file.service.FileService;
import kr.or.ddit.vo.FileVO;

@Controller
@RequestMapping("/map/img")
public class KakaoMapImageController {

	@Autowired
	public FileService service;

	@PostMapping("/public")
	@ResponseBody
	public ResponseEntity<String> uploadMapImage(@RequestParam("lstgId") String lstgId,
	                                             @RequestParam("file") MultipartFile file) {
	    try {
	        // 여기서 service가 내부적으로 S3Uploader를 사용함
	        FileVO uploadedFile = service.uploadAndSave(
	            file, "listing", "LISTING", lstgId, "PROPERTY_IMG"
	        );
	        return ResponseEntity.ok(uploadedFile.getFilePathUrl());
	    } catch (Exception e) {
	        return ResponseEntity.internalServerError().body("업로드 실패: " + e.getMessage());
	    }
	}


}
