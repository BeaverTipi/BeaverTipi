package kr.or.ddit.main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;

@Mapper
public interface MainNoticeMapper {
	public List<BoardVO> selectMainNoticeList(PaginationInfo<BoardVO> paging);
	public int getTotalMainNoticeCount(PaginationInfo<BoardVO> paging);
}
