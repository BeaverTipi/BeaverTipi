package kr.or.ddit.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BusinessApproveSearchVO;
import kr.or.ddit.vo.MemberVO;

@Mapper
public interface BusinessApproveMapper {
	public Integer selectTotalRecord(PaginationInfo<BusinessApproveSearchVO> paging);

	public List<MemberVO> selectBusinessApproveList(PaginationInfo<BusinessApproveSearchVO> paging);
	
	public Integer updateTenancyApprove(String mbrCd);
	public Integer updateTenancyReject(String mbrCd);
	public Integer updateBrokerReject(String mbrCd);
	public Integer updateBrokerApprove(String mbrCd);
}
