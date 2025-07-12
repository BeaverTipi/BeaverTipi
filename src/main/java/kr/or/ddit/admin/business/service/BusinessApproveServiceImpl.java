package kr.or.ddit.admin.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.or.ddit.admin.mapper.BusinessApproveMapper;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.validate.exception.ApprovedException;
import kr.or.ddit.util.validate.exception.RejectedException;
import kr.or.ddit.vo.BusinessApproveSearchVO;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class BusinessApproveServiceImpl implements BusinessApproveService {
	private final BusinessApproveMapper mapper;
	@Override
	public Integer readTotalRecord(PaginationInfo<BusinessApproveSearchVO> paging) {
		// TODO Auto-generated method stub
		return mapper.selectTotalRecord(paging);
	}

	@Override
	public List<MemberVO> readBusinessApproveList(PaginationInfo<BusinessApproveSearchVO> paging) {
		// TODO Auto-generated method stub
		return mapper.selectBusinessApproveList(paging);
	}

	@Override
	public void approveMember(String mbrCd, String userType) {
	 
		if ("BROKER".equalsIgnoreCase(userType)) {
			if(mapper.updateBrokerApprove(mbrCd)<1) {
				throw new ApprovedException();
			}
		}else if ("TENANCY".equalsIgnoreCase(userType)){
			if(mapper.updateTenancyApprove(mbrCd)<1)throw new ApprovedException();
		}else {
			throw new ApprovedException();
		}
	}

	@Override
	public void rejectMember(String mbrCd, String userType) {
		if ("BROKER".equalsIgnoreCase(userType)) {
			if(mapper.updateBrokerReject(mbrCd)<1)throw new RejectedException();
		}else if ("TENANCY".equalsIgnoreCase(userType)){
			if(mapper.updateTenancyReject(mbrCd)<1)throw new RejectedException();
		}else {
			throw new RejectedException();
		}
	}

}
