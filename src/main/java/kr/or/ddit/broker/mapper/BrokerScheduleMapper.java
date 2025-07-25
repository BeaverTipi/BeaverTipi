package kr.or.ddit.broker.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.ScheduleVO;
@Mapper
public interface BrokerScheduleMapper {

   public List<ScheduleVO> selectScheduleList(@Param("username") String username);
   public ScheduleVO selectSchedule(ScheduleVO schedule);
   public String selectNextScheduleIdSuffix();
   public Integer insertSchedule(ScheduleVO schedule);
   public Integer updateSchedule(ScheduleVO schedule);
   public Integer updateDelYnSchedule(ScheduleVO schedule);
}
