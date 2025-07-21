package kr.or.ddit.broker.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.vo.FileVO;

public interface BrokerNameCardService {

    // 1. 회원별 명함 리스트 조회
    List<FileVO> selectNameCardListByMember(String mbrCd);

    // 2. 명함 이미지 업로드 + 저장
    FileVO uploadAndSave(MultipartFile file, String dir, String sourceRef, String sourceId, String docTypeCd);

    // 3. 명함 이미지 삭제
    int deleteFileById(Map<String, Object> params);

    // 4. 명함 이미지 상세 조회
    FileVO selectNameCardDetail(Map<String, Object> params);
    
    // 5. 대표명함 설정할꺼임. 이걸 설계한 나는 어쩌면 똑똑할지도...?
    void setMainNameCard(String mbrCd, String nameCardId);
}
