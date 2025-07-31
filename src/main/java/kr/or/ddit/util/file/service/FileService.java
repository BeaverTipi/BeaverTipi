package kr.or.ddit.util.file.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.vo.ContractDigitalSignVO;
import kr.or.ddit.vo.FileVO;

public interface FileService {
	public FileVO uploadAndSave(MultipartFile files, String dir, String sourceRef, String sourceId, String docTypeCd);
	public List<FileVO> uploadMultipleFiles(List<MultipartFile> files, String dir, String sourceRef, String sourceId, String docTypeCd);
	public void removeOldFiles();
	public void deleteFile(String fileId);
	public FileVO updateFile(String fileId, MultipartFile newFile);
	public ResponseEntity<Resource> downloadFile(String fileId);
	
	public String generatePresignedDownloadUrl(String fileId, int expireMinutes);
	public FileVO readFile(String fileId);
	public List<FileVO> updateMultipleFiles(List<MultipartFile> newFiles, String dir, String sourceRef, String sourceId, String docTypeCd);
	public List<FileVO> readFileList(String sourceRef, String sourceId);
	public InputStream getFileStream(String fileId); // S3 파일 스트림 제공
    public String getPresignedUrl(String fileId, int expireMinutes); // Presigned URL 제공
    
    public void updateFileUrl(String fileId, String newUrl);
    public Integer readMaxAttachSeq(String contId);
    public FileVO uploadAndSaveTempSignedContract(MultipartFile file, ContractDigitalSignVO digitalSign);
}
