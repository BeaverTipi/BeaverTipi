package kr.or.ddit.broker.service;

import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.vo.FileVO;

import java.util.Map;

public interface BrokerIntroCardService {

    FileVO selectIntroCardByMember(Map<String, Object> params);

    FileVO uploadAndSaveIntroCard(MultipartFile file, String dir, String sourceRef, String sourceId, String docTypeCd);

    int deleteIntroCardFile(Map<String, Object> params);

    void replaceAndSaveIntroCard(String fileId, MultipartFile newFile);

}
