package kr.or.ddit.resident.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.UnitResidentVO;

@Mapper
public interface UnitResidentMapper {

	public List<UnitResidentVO> selectByMember(@Param("mbrCd")String mbrCd);
	
	public List<Map<String, Object>> selectInitialBuildingPosts(String mbrCd);

	public List<UnitResidentVO> selectMyUnitsInBuilding(@Param("mbrCd") String mbrCd,
            @Param("bldgId") String bldgId);

	public int isMyUnit(@Param("mbrCd") String mbrCd, @Param("unitId") String unitId);

}
