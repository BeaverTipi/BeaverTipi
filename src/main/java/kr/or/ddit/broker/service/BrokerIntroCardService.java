package kr.or.ddit.broker.service;

import kr.or.ddit.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface BrokerIntroCardService {

    // 1. 소개카드 단건 조회 (가장 최근 등록)
    FileVO selectIntroCardByMember(String mbrCd, String docTypeCd);

    // 2. 소개카드 이미지 저장 (캡쳐파일 업로드 및 DB 등록)
    FileVO uploadAndSaveIntroCard(MultipartFile file, String dir, String sourceRef, String sourceId, String docTypeCd);

    // 3. 소개카드 이미지 삭제
    int deleteIntroCardFile(Map<String, Object> params);

}
