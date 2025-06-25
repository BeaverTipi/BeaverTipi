package kr.or.ddit.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller

public class FileTestController {
	@Autowired
	private FileTestService testService;
	
	@PostMapping("/upload")
    public ResponseEntity<String> uploadFile(MultipartFile imgFile, TestFileEntity fileEntity) {
        log.info("파일 저장 컨트롤러 실행");
        try {
            testService.saveFile(imgFile, fileEntity.getImgText());
            return ResponseEntity.ok("파일 업로드 성공");
        } catch (Exception e) {
            log.error("파일 업로드 실패", e);
            return ResponseEntity.status(400).build();
        }
    }
}
