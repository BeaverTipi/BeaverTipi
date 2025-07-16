package kr.or.ddit.resident.calendar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.resident.mapper.ScheduleMapper;
import kr.or.ddit.vo.ScheduleVO;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	private ScheduleMapper scheduleMapper;
	
	@Override
	public List<ScheduleVO> getAllSchedules() {
		// TODO Auto-generated method stub
		return scheduleMapper.getAllSchedules();
	}

	@Override
	public void createSchedule(ScheduleVO schedule) {
		String maxId = scheduleMapper.selectMaxScheduleId();
		int nextSeq = 1;
		
		if(maxId != null && maxId.length() == 8) {
			String numberPart = maxId.substring(1);
			nextSeq = Integer.parseInt(numberPart)+1;
		}
		
		String newId = String.format("S%07d", nextSeq);
		schedule.setScdId(newId);
		scheduleMapper.createSchedule(schedule);
	}

	@Override
	public void updateSchedule(ScheduleVO schedule) {
		// TODO Auto-generated method stub
		scheduleMapper.updateSchedule(schedule);
	}

	@Override
	public void deleteSchedule(String scdId) {
		// TODO Auto-generated method stub
		scheduleMapper.deleteSchedule(scdId);
	}

	@Override
	public ScheduleVO getScheduleById(String scdId) {
		// TODO Auto-generated method stub
		return scheduleMapper.getScheduleById(scdId);
	}

}
