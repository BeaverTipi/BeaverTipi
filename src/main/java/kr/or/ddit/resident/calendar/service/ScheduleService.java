package kr.or.ddit.resident.calendar.service;

import java.util.List;

import kr.or.ddit.vo.BuildingScheduleVO;

public interface ScheduleService {

    public List<BuildingScheduleVO> getSchedulesByBuilding(String bldgId,String rentalPtyId);
    
    public void createSchedule(BuildingScheduleVO schedule);
    
    public void updateSchedule(BuildingScheduleVO schedule);
    
    public void deleteSchedule(String bscId);
    
    public BuildingScheduleVO getScheduleById(String bscId);
    
    public String getRentalPtyIdByMbrCd(String mbrCd);
}
