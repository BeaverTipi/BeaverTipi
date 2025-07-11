package kr.or.ddit.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;

@Mapper
public interface NoticePostMapper {
	public List<BoardVO> selectNoticeList(PaginationInfo<BoardVO> paging);
	public BoardVO selectNoticeById(String brdNo);
	public int insertNotice(BoardVO board);
	public int updateNotice(BoardVO board);
	public int deleteNotice(BoardVO board);
	
	public int selectTotalNoticeRecord(PaginationInfo<BoardVO> paging);
}
