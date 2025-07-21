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
    public int deleteFileById(Map<String, Object> params) {
        return mapper.deleteFileById(params);
    }

    @Override
    public FileVO selectNameCardDetail(Map<String, Object> params) {
        return mapper.selectNameCardDetail(params);
    }
    
    @Override
    @Transactional
    public void setMainNameCard(String mbrCd, String nameCardId) {
        // 기존 대표명함 해제
        mapper.unsetMainNameCard(mbrCd);
        // 새 대표명함 지정
        mapper.setMainNameCard(nameCardId, mbrCd);
    }
}
