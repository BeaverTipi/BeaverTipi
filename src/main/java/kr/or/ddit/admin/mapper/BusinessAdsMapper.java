package kr.or.ddit.admin.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;

@Mapper
public interface BusinessAdsMapper {
    
    List<BoardVO> selectBusinessAdsList(PaginationInfo<BoardVO> pagingVO);

    int selectBusinessAdsCount(PaginationInfo<BoardVO> pagingVO);
}