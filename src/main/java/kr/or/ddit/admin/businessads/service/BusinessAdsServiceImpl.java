package kr.or.ddit.admin.businessads.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import kr.or.ddit.admin.mapper.BusinessAdsMapper;
import kr.or.ddit.util.file.service.FileService;
//import kr.or.ddit.util.file.mapper.FileMapper; // 사용하지 않으면 제거
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.AdsClientVO;
import kr.or.ddit.vo.BoardVO;
import kr.or.ddit.vo.FileVO;

@Service
public class BusinessAdsServiceImpl implements BusinessAdsService {

	@Inject
    private BusinessAdsMapper businessAdsMapper;

	@Inject
	private FileService fileService; // FileService 주입
	
	private static final String AD_FILE_SOURCE_REF = "AD_BOARD";
	
	@Override
	public List<BoardVO> selectBusinessAdsList(PaginationInfo<BoardVO> pagingVO) {
		return businessAdsMapper.selectBusinessAdsList(pagingVO);
	}

	@Override
	public int selectBusinessAdsCount(PaginationInfo<BoardVO> pagingVO) {
		return businessAdsMapper.selectBusinessAdsCount(pagingVO);
	}

	// ⭐ 상세 정보 조회 메서드 구현 ⭐
	@Override
	public BoardVO selectBusinessAdsDetail(String brdNo) {
		// 1. 광고(BoardVO) 상세 정보 조회
        BoardVO boardDetail = businessAdsMapper.selectBusinessAdsDetail(brdNo);

        // 2. 해당 광고에 연결된 파일 목록 조회
        if (boardDetail != null) {
            // brdNo를 fileSourceId로 사용하여 파일 조회
            List<FileVO> attachedFiles = fileService.readFileList(AD_FILE_SOURCE_REF, brdNo);
            boardDetail.setAttachFiles(attachedFiles);
        }

        return boardDetail;	///////////////////////////////// 확인 //////////////////////////////////////
	}
	
	@Override
    public int updateAdsStatus(String brdNo, String adsStatusCode) {

        // Controller에서 받던 Map<String, String>을 서비스 내부에서 VO로 변환
        BoardVO boardToUpdate = new BoardVO(); 
        boardToUpdate.setBrdNo(brdNo);
        
        AdsClientVO adsClientVO = new AdsClientVO(); // 실제 AdsClientVO 경로로 수정
        adsClientVO.setAdsStatusCode(adsStatusCode);
        boardToUpdate.setAdsClientVO(adsClientVO); // BoardVO에 setAdsClient 메서드가 있어야 함
        
        int result = businessAdsMapper.updateAdsStatus(boardToUpdate);
        return result;
    }
	
	@Override
    public List<BoardVO> selectApprovedAdsForMain() {
        // 현재 날짜를 기준으로 유효한 광고만 조회하도록 Mapper에 전달
        // Mapper에서는 '승인' 상태와 날짜 조건을 모두 검사
        List<BoardVO> approvedAds = businessAdsMapper.selectApprovedAdsForMain();

        // 각 광고에 대해 첨부 파일 목록 조회 (이미지 파일만 필터링하는 로직 추가 필요)
        for (BoardVO ad : approvedAds) {
            List<FileVO> attachedFiles = fileService.readFileList(AD_FILE_SOURCE_REF, ad.getBrdNo());
            // 이미지 파일만 필터링하는 로직 (선택 사항이지만 권장)
            attachedFiles.removeIf(file -> !file.getFileMime().startsWith("image/"));
            ad.setAttachFiles(attachedFiles);
        }
        return approvedAds;
    }
}