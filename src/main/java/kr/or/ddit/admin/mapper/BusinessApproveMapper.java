package kr.or.ddit.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BusinessApproveSearchVO;
import kr.or.ddit.vo.SolutionSubscriptionVO;

@Mapper
public interface BusinessApproveMapper {
	public Integer selectTotalRecord(PaginationInfo<BusinessApproveSearchVO> paging);

	public List<SolutionSubscriptionVO> selectBusinessApproveList(PaginationInfo<BusinessApproveSearchVO> paging);
}
