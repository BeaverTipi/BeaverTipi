package kr.or.ddit.admin.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.or.ddit.admin.mapper.BusinessApproveMapper;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BusinessApproveSearchVO;
import kr.or.ddit.vo.SolutionSubscriptionVO;
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
	public List<SolutionSubscriptionVO> readBusinessApproveList(PaginationInfo<BusinessApproveSearchVO> paging) {
		// TODO Auto-generated method stub
		return mapper.selectBusinessApproveList(paging);
	}

}
