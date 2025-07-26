package kr.or.ddit.resident.complaint.service;

import java.util.List;
import java.util.Map;

import kr.or.ddit.vo.ResidentBoardVO;

public interface ComplaintService {

	/** 전체 건수 조회 */
    public int selectComplaintCount(Map<String, Object> param);

    /** 페이징 처리된 목록 조회 */
    public List<ResidentBoardVO> selectComplaintList(Map<String, Object> param);

    /** 단건 조회 */
    public ResidentBoardVO selectComplaintById(String rsdBrdId);

    /** 등록 */
    public void insertComplaint(ResidentBoardVO complaint);

    /** 수정 */
    public void updateComplaint(ResidentBoardVO complaint);

    /** 삭제(soft) */
    public void deleteComplaint(String rsdBrdId);
    
    public String getNextComplaintId();
    
    public boolean isBuildingOwner(String mbrCd, String bldgId);
    
//    public boolean isLandlordOfBuilding(String mbrCd, String bldgId);
    
    public void replyToComplaint(ResidentBoardVO compaint);
    
    public boolean canViewComplaint(String rsdBrdId,String mbrCd);
    
    public String getFirstMoveInBuildingId(String mbrCd);
    
}
