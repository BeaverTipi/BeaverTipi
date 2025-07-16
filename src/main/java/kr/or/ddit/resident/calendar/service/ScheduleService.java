package kr.or.ddit.resident.calendar.service;

import java.util.List;

import kr.or.ddit.vo.ScheduleVO;

public interface ScheduleService {

    // 전체 일정 조회
    public List<ScheduleVO> getAllSchedules();

    // 일정 생성
    public void createSchedule(ScheduleVO schedule);

    // 일정 수정
    public void updateSchedule(ScheduleVO schedule);

    // 일정 삭제
    public void deleteSchedule(String scdId);

    // 일정 상세 조회
    public ScheduleVO getScheduleById(String scdId);
}
