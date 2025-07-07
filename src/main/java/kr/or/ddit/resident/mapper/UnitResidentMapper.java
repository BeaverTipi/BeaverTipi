package kr.or.ddit.resident.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.UnitResidentVO;

@Mapper
public interface UnitResidentMapper {

	List<UnitResidentVO> selectByMember(@Param("mbrCd")String mbrCd);
}
