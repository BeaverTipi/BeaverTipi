package kr.or.ddit.admin.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;

@Mapper
public interface BusinessAdsMapper {

    List<BoardVO> selectBusinessAdsList(PaginationInfo<BoardVO> pagingVO);

    int selectBusinessAdsCount(PaginationInfo<BoardVO> pagingVO);

    BoardVO selectBusinessAdsDetail(String brdNo);

	int updateAdsStatus(BoardVO boardToUpdate);
	
	List<BoardVO> selectApprovedAdsForMain(); // 메인 페이지에 표시할 승인된 광고 목록 조회
}