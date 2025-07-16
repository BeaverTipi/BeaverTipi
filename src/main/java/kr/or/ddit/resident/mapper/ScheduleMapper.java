package kr.or.ddit.resident.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.ScheduleVO;

@Mapper
public interface ScheduleMapper {

	// 일정 목록 조회
    public List<ScheduleVO> getAllSchedules();
    // 일정 생성
    public void createSchedule(ScheduleVO schedule);
    // 일정 수정
    public void updateSchedule(ScheduleVO schedule);
    // 일정 삭제
    public void deleteSchedule(String scdId);
    // 일정 상세 조회
    public ScheduleVO getScheduleById(@Param("scdId") String scdId);
    
    public String selectMaxScheduleId();
}
