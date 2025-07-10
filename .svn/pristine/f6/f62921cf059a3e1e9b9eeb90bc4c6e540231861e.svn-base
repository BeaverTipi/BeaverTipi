package kr.or.ddit.resident.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.page.SimpleSearch;
import kr.or.ddit.vo.ResidentBoardVO;

@Mapper
public interface ComplaintMapper {
	 /** 페이징 전 전체 건수 조회 */
    public int selectComplaintTotalCount(SimpleSearch search);

    /** 페이징 처리된 민원 목록 조회 */
    public List<ResidentBoardVO> selectComplaintList(
        @Param("search") SimpleSearch search,
        @Param("paging") PaginationInfo pagingInfo
    );

    /** 단건 상세 조회 */
    public ResidentBoardVO selectComplaintById(@Param("rsdBrdId") String rsdBrdId);

    /** PK 채번용 시퀀스 조회 */
    public String getNextComplaintId();

    /** 공통 BOARD 테이블에 글 기본 정보 INSERT */
    public void insertComplaintBoard(ResidentBoardVO vo);

    /** RESIDENT_BOARD 테이블에 민원 추가 정보 INSERT */
    public void insertComplaint(ResidentBoardVO vo);

    /** 공통 BOARD 테이블 UPDATE */
    public void updateComplaintBoard(ResidentBoardVO vo);

    /** RESIDENT_BOARD 테이블 UPDATE */
    public void updateComplaint(ResidentBoardVO vo);

    /** 소프트 삭제 (BOARD 전용 삭제여부만 ‘Y’ 처리) */
    public void softDeleteComplaint(@Param("rsdBrdId") String rsdBrdId);

    public void softDeleteResidentBoard(@Param("rsdBrdId") String rsdBrdId);
}
