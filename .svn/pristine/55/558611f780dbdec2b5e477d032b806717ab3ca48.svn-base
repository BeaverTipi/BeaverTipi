package kr.or.ddit.util.file.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.vo.FileVO;

public interface FileService {
	public FileVO uploadAndSave(MultipartFile files, String dir, String sourceRef, String sourceId, String docTypeCd);
	public List<FileVO> uploadMultipleFiles(List<MultipartFile> files, String dir, String sourceRef, String sourceId, String docTypeCd);
	public void removeOldFiles();
	public void deleteFile(String fileId);
	public void updateFile(String fileId, MultipartFile newFile);
	public ResponseEntity<Resource> downloadFile(String fileId);
	
	public String generatePresignedDownloadUrl(String fileId, int expireMinutes);

}
