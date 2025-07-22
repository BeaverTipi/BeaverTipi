package kr.or.ddit.broker.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.broker.mapper.BrokerNameCardMapper;
import kr.or.ddit.broker.service.BrokerNameCardService;
import kr.or.ddit.util.file.service.FileService;
import kr.or.ddit.vo.FileVO;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class BrokerNameCardServiceImpl implements BrokerNameCardService {

    @Autowired
    private BrokerNameCardMapper mapper;

    @Autowired
    private FileService fileService;

    @Override
    public List<FileVO> selectNameCardListByMember(String mbrCd) {
        return mapper.selectNameCardListByMember(mbrCd);
    }

    @Override
    public FileVO uploadAndSave(MultipartFile file, String dir, String sourceRef, String sourceId, String docTypeCd) {
        return fileService.uploadAndSave(file, dir, sourceRef, sourceId, docTypeCd);
    }

    @Override
    @Transactional
    public int deleteFileById(Map<String, Object> params) {
        String fileId = (String) params.get("fileId");
        Integer fileAttachSeq = (Integer) params.get("fileAttachSeq");

        // 삭제 전 파일 정보 조회 (대표명함 여부 확인용)
        FileVO file = mapper.selectNameCardDetail(params);

        // 실제 삭제
        int result = mapper.deleteFileById(params);

        // 대표명함이 삭제된 경우
        if (file != null && "NAMECARD_MAIN".equals(file.getDocTypeCd())) {
            // 해당 회원의 남아있는 명함 중 최신 한 건을 대표로 지정
            List<FileVO> remains = mapper.selectNameCardListByMember(file.getFileSourceId());
            if (!remains.isEmpty()) {
                FileVO newest = remains.get(0); 
                mapper.setMainNameCard(newest.getFileId(), newest.getFileSourceId());
            }
        }

        return result;
    }


    @Override
    public FileVO selectNameCardDetail(Map<String, Object> params) {
        return mapper.selectNameCardDetail(params);
    }
    
    @Override
    @Transactional
    public void setMainNameCard(String mbrCd, String nameCardId) {
    	log.info("[setMainNameCard-Service] nameCardId={}, mbrCd={}", nameCardId, mbrCd);
        // 기존 대표명함 해제
        mapper.unsetMainNameCard(mbrCd);
        // 새 대표명함 지정
        mapper.setMainNameCard(nameCardId, mbrCd);
    }
}
