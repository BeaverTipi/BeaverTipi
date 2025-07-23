package kr.or.ddit.broker.service.impl;

import kr.or.ddit.broker.mapper.BrokerIntroCardMapper;
import kr.or.ddit.broker.service.BrokerIntroCardService;
import kr.or.ddit.util.file.service.FileService;
import kr.or.ddit.vo.FileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@Service
public class BrokerIntroCardServiceImpl implements BrokerIntroCardService {

    @Autowired
    private BrokerIntroCardMapper mapper;

    @Autowired
    private FileService fileService;

    // 1. 소개카드 단건 조회 (가장 최근 등록)
    @Override
    public FileVO selectIntroCardByMember(String mbrCd, String docTypeCd) {
        return mapper.selectIntroCardByMember(mbrCd, docTypeCd);
    }

    // 2. 소개카드 이미지 저장 (업로드 및 DB 등록)
    @Override
    public FileVO uploadAndSaveIntroCard(MultipartFile file, String dir, String sourceRef, String sourceId, String docTypeCd) {
        // FileService를 통한 실제 업로드 및 DB 등록 (dir은 "public/broker/introcard"로 고정해도 됨)
        return fileService.uploadAndSave(file, dir, sourceRef, sourceId, docTypeCd);
    }

    // 3. 소개카드 이미지 삭제
    @Override
    public int deleteIntroCardFile(Map<String, Object> params) {
        return mapper.deleteIntroCardFile(params);
    }
}
