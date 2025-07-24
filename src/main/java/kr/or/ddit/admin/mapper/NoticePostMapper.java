package kr.or.ddit.admin.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;

@Mapper
public interface NoticePostMapper {
    public List<Map<String, Object>> selectBoardList(PaginationInfo<BoardVO> paging);
    public Map<String, Object> selectBoardById(String brdNo);
    public int insertBoard(BoardVO board);
    public int updateBoard(BoardVO board);
    public int deleteBoard(String board);
    public int selectTotalBoardRecord(PaginationInfo<BoardVO> paging);
}
