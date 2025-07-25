package kr.or.ddit.broker.service;

import java.util.List;

import kr.or.ddit.vo.ScheduleVO;

public interface BrokerScheduleService {
    public List<ScheduleVO> readScheduleList(String mbrCd);
    public ScheduleVO createSchedule(ScheduleVO schedule);
    public ScheduleVO readScheduleDetail(ScheduleVO schedule);
    public void modifySchedule(ScheduleVO schedule);
    public void removeSchedule(ScheduleVO schedule);
}

