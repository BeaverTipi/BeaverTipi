package kr.or.ddit.resident.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.BoardCartegoryVO;

@Mapper
public interface BoardCartegoryMapper {

	List<BoardCartegoryVO> selectAllCartegories();
}
