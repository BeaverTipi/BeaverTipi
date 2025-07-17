package kr.or.ddit.admin.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;

@Mapper
public interface BusinessAdsMapper {

    List<BoardVO> selectBusinessAdsList(PaginationInfo<BoardVO> pagingVO);

    int selectBusinessAdsCount(PaginationInfo<BoardVO> pagingVO);

    // ⭐ 상세 정보 조회 메서드 추가 ⭐
    BoardVO selectBusinessAdsDetail(String brdNo);
}