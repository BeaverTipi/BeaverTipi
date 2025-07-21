package kr.or.ddit.main.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.AdsClientVO;
import kr.or.ddit.vo.BoardVO;

@Mapper // MyBatis 매퍼 인터페이스임을 명시 (Spring Boot 등에서 자동 스캔)
public interface MemberAdsMapper {

    /**
     * 새로운 광고 게시글을 삽입하고, 생성된 brdNo를 BoardVO 객체에 다시 설정합니다.
     * BoardVO 내의 brdNo 필드가 <selectKey>에 의해 채워질 것입니다.
     * @param boardVO 삽입할 BoardVO 객체 (brdNo가 채워져 돌아옴)
     * @return 삽입된 행의 수
     */
    public int insertBoard(BoardVO boardVO);

    /**
     * 새로운 광고주 정보를 삽입합니다.
     * adsClientVO는 이미 BoardVO에서 생성된 brdNo를 가지고 있어야 합니다.
     * @param adsClientVO 삽입할 AdsClientVO 객체
     * @return 삽입된 행의 수
     */
    public int insertAdsClient(AdsClientVO adsClientVO);
}