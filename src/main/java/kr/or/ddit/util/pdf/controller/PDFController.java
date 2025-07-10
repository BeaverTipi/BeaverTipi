package kr.or.ddit.util.pdf.controller;

import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import kr.or.ddit.util.pdf.service.PDFService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ajax/pdf")
@RequiredArgsConstructor
public class PDFController {

    private final PDFService pdfService;

    @PostMapping("/extract-crtfno")
    public ResponseEntity<Map<String, Object>> extractCrtfNoFromFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(pdfService.extractCrtfNo(file));
    }

    @PostMapping("/extract-bizregno")
    public ResponseEntity<Map<String, Object>> extractBizRegNoFromFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(pdfService.extractBizRegNo(file));
    }
    
    @PostConstruct
    public void init() {
        ImageIO.scanForPlugins(); // 이걸 넣지 않으면 SPI가 동작하지 않음
    }
}
