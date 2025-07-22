package kr.or.ddit.resident.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.BuildingScheduleVO;

@Mapper
public interface BuildingScheduleMapper {
	
    public List<BuildingScheduleVO> selectSchedulesByBuilding(Map<String, Object> paramMap);

    public String selectMaxScheduleId();

    public void insertSchedule(BuildingScheduleVO schedule);

    public void updateSchedule(BuildingScheduleVO schedule);

    public void deleteSchedule(String bscId);

    public BuildingScheduleVO selectScheduleById(String bscId);
}
