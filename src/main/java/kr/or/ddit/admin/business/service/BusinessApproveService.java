package kr.or.ddit.admin.business.service;

import java.util.List;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BusinessApproveSearchVO;
import kr.or.ddit.vo.MemberVO;

public interface BusinessApproveService {
	public Integer readTotalRecord(PaginationInfo<BusinessApproveSearchVO> paging);

	public List<MemberVO> readBusinessApproveList(PaginationInfo<BusinessApproveSearchVO> paging);

	public void approveMember(String mbrCd, String userType);

	public void rejectMember(String mbrCd, String userType);
}
