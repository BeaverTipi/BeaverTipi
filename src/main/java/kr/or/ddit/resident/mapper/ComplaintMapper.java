package kr.or.ddit.resident.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.ResidentBoardVO;

@Mapper
public interface ComplaintMapper {
	 /** 페이징 전 전체 건수 조회 */
    public int selectComplaintTotalCount(Map<String , Object> param);

    /** 페이징 처리된 민원 목록 조회 */
    public List<ResidentBoardVO> selectComplaintList(
       Map<String, Object > param
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

    public int isLandlordOfBuilding(@Param("mbrCd") String mbrCd, @Param("bldgId") String bldgId);

    public void updateReplyToComplaint(ResidentBoardVO complaint);
    // 건물의 임대인 확인 (임대인 MBR_CD가 일치하는지)
    public int isBuildingOwner(@Param("bldgId") String bldgId, @Param("mbrCd") String mbrCd);

    /** 게시글 조회 가능 여부 (작성자 또는 임대인 여부 판단) */
    public int canViewComplaint(@Param("rsdBrdId") String rsdBrdId, @Param("mbrCd") String mbrCd);

}	

