package kr.or.ddit.main.businessads.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.vo.AdsClientVO;
import kr.or.ddit.vo.BoardVO;

public interface MemberAdsService {

    /**
     * 회원의 광고 요청을 생성하고 관련 정보(게시글, 광고주, 파일)를 저장합니다.
     * @param boardVO 광고 게시글 정보
     * @param adsClientVO 광고주 정보
     * @param attachFiles 첨부 파일 목록
     * @return 광고 요청 처리 성공 여부
     */
    public boolean createAdsRequest(BoardVO boardVO, AdsClientVO adsClientVO, List<MultipartFile> attachFiles);
}