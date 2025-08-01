package kr.or.ddit.admin.businessads.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kr.or.ddit.admin.mapper.BusinessAdsMapper;
import kr.or.ddit.util.file.service.FileService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.AdsClientVO;
import kr.or.ddit.vo.BoardVO;
import kr.or.ddit.vo.FileVO;
import lombok.extern.slf4j.Slf4j; // SLF4J 로거를 사용하기 위한 import 추가

@Slf4j // Lombok을 사용하여 로거 필드를 자동으로 생성
@Service
public class BusinessAdsServiceImpl implements BusinessAdsService {

	@Autowired
    private BusinessAdsMapper businessAdsMapper;

	@Autowired
    private FileService fileService;
    
    private static final String AD_FILE_SOURCE_REF = "AD_BOARD";
    
    @Override
    public List<BoardVO> selectBusinessAdsList(PaginationInfo<BoardVO> pagingVO) {
        return businessAdsMapper.selectBusinessAdsList(pagingVO);
    }

    @Override
    public int selectBusinessAdsCount(PaginationInfo<BoardVO> pagingVO) {
        return businessAdsMapper.selectBusinessAdsCount(pagingVO);
    }

    @Override
    public BoardVO selectBusinessAdsDetail(String brdNo) {
        BoardVO boardDetail = businessAdsMapper.selectBusinessAdsDetail(brdNo);

        if (boardDetail != null) {
            List<FileVO> attachedFiles = fileService.readFileList(AD_FILE_SOURCE_REF, brdNo);
            boardDetail.setAttachFiles(attachedFiles);
        }

        return boardDetail;
    }
    
    @Override
    @Transactional
    public int updateAdsStatus(BoardVO boardToUpdate) { 
        log.info("updateAdsStatus 서비스 시작 - boardToUpdate: {}", boardToUpdate);

        // BoardVO 내의 AdsClientVO 객체에 접근합니다.
        // adsClientVO가 null일 수 있으므로, NullPointerException 방지를 위해 체크합니다.
        AdsClientVO adsClientVO = boardToUpdate.getAdsClientVO();
        if (adsClientVO == null) {
            log.warn("boardToUpdate.getAdsClientVO()가 null입니다. 새로운 AdsClientVO를 생성합니다.");
            adsClientVO = new AdsClientVO();
            boardToUpdate.setAdsClientVO(adsClientVO); // BoardVO에 새로 생성한 AdsClientVO 설정
        }

        String adsStatusCode = adsClientVO.getAdsStatusCode();
        String adsRejectMessage = adsClientVO.getAdsRejectMessage(); 
        
        log.debug("광고 상태: {}, 반려 내용: {}", adsStatusCode, adsRejectMessage);

        // 상태가 '반려'가 아닐 경우 adsRejectMessage를 null로 설정하여 DB에서도 비웁니다.
        // 이 로직은 컨트롤러나 서비스 시작 부분에서 처리하는 것이 더 명확할 수 있습니다.
        // 현재는 서비스 내부에서 처리하도록 유지합니다.
        if (!"반려".equals(adsStatusCode)) {
            adsClientVO.setAdsRejectMessage(null);
            log.debug("광고 상태가 '반려'가 아니므로 adsRejectMessage를 null로 설정했습니다.");
        }
        
        int result = businessAdsMapper.updateAdsStatus(boardToUpdate);
        log.info("updateAdsStatus 서비스 종료 - 업데이트 결과: {}", result);
        return result;
    }
    
    @Override
    public List<BoardVO> selectApprovedAdsForMain() {
        List<BoardVO> approvedAds = businessAdsMapper.selectApprovedAdsForMain();

        for (BoardVO ad : approvedAds) {
            List<FileVO> attachedFiles = fileService.readFileList(AD_FILE_SOURCE_REF, ad.getBrdNo());
            // 이미지 파일만 필터링 (필요하다면)
            if (attachedFiles != null) {
                attachedFiles.removeIf(file -> !file.getFileMime().startsWith("image/"));
            }
            ad.setAttachFiles(attachedFiles);
        }
        return approvedAds;
    }
}