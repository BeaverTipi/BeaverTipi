package kr.or.ddit.resident.complaint.service;

import java.util.List;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.page.SimpleSearch;
import kr.or.ddit.vo.ResidentBoardVO;

public interface ComplaintService {

	/** 전체 건수 조회 */
    public int selectComplaintCount(SimpleSearch search);

    /** 페이징 처리된 목록 조회 */
    public List<ResidentBoardVO> selectComplaintList(SimpleSearch search,
                                              PaginationInfo pagingInfo);

    /** 단건 조회 */
    public ResidentBoardVO selectComplaintById(String rsdBrdId);

    /** 등록 */
    public void insertComplaint(ResidentBoardVO complaint);

    /** 수정 */
    public void updateComplaint(ResidentBoardVO complaint);

    /** 삭제(soft) */
    public void deleteComplaint(String rsdBrdId);
    
    public String getNextComplaintId();
    
    
}
