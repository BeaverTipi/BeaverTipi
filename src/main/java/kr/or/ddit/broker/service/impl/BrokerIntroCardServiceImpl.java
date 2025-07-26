package kr.or.ddit.broker.service.impl;

import java.io.IOException;
import java.time.LocalDate;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.broker.mapper.BrokerIntroCardMapper;
import kr.or.ddit.broker.service.BrokerIntroCardService;
import kr.or.ddit.util.file.service.FileService;
import kr.or.ddit.util.file.service.S3Uploader;
import kr.or.ddit.util.validate.exception.FileIOException;
import kr.or.ddit.vo.FileVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BrokerIntroCardServiceImpl implements BrokerIntroCardService {

    @Autowired
    private BrokerIntroCardMapper mapper;

    @Autowired
    private FileService fileService;
    @Autowired
    private S3Uploader s3Uploader;

    @Override
    public FileVO selectIntroCardByMember(Map<String, Object> params) {
        return mapper.selectIntroCardByMember(params);
    }

    @Override
    public FileVO uploadAndSaveIntroCard(MultipartFile file, String dir, String sourceRef, String sourceId, String docTypeCd) {
        return fileService.uploadAndSave(file, dir, sourceRef, sourceId, docTypeCd);
    }

    @Override
    @Transactional
    public int deleteIntroCardFile(Map<String, Object> params) {
        return mapper.deleteIntroCardFile(params);
    }

    @Override
    @Transactional
   	public void replaceAndSaveIntroCard(String fileId, MultipartFile newFile) {
		FileVO oldFile = mapper.selectFile(fileId);
	    if (oldFile == null) {
	        throw new FileIOException("수정할 파일이 존재하지 않습니다.");
	    }

	    // 1. 기존 S3 파일 삭제
	    s3Uploader.fileDelete(oldFile.getFileDir() +"/"+ oldFile.getFileSavedname());

	    // 2. 새 파일 업로드
	    String newSavedName = changedFileName(newFile.getOriginalFilename());
	    String fileUrl;
	    try {
	        fileUrl = s3Uploader.upload(newFile, oldFile.getFileDir() +"/" +newSavedName);
	    } catch (IOException e) {
	        throw new FileIOException("파일 업로드중 오류 발생",e);
	    }

	    // 3. DB 정보 수정
	    oldFile.setFileOriginalname(newFile.getOriginalFilename());
	    oldFile.setFileSavedname(newSavedName);
	    oldFile.setFilePathUrl(fileUrl);
	    oldFile.setFileMime(newFile.getContentType());
	    oldFile.setFileSize((int) newFile.getSize());
	    oldFile.setRegDtm(LocalDate.now()); // 업데이트 시점으로 덮어쓰기

	    mapper.updateFile(oldFile);
	    
	    
		
	}
    // 랜덤 파일 이름 메서드 (파일 이름 중복 방지)
    private String changedFileName(String originName) {
        String random = UUID.randomUUID().toString();
        return random + originName;
    }

}
