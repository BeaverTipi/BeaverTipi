package kr.or.ddit.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.BoardVO;

@Mapper
public interface NoticePostMapper {
	public List<BoardVO> selectNoticeList();
}
