package kr.or.ddit.util.pdf.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface PDFService {
	public Map<String, Object> extractCrtfNo(MultipartFile file);
    public Map<String, Object> extractBizRegNo(MultipartFile file);
    public File mergeToSinglePdf(List<MultipartFile> files) throws Exception;
}
