package kr.or.ddit.resident.calendar.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.resident.mapper.BuildingScheduleMapper;
import kr.or.ddit.resident.mapper.TenancyMapper;
import kr.or.ddit.vo.BuildingScheduleVO;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	private BuildingScheduleMapper buildingScheduleMapper;
	
	@Autowired
	private TenancyMapper tenancyMapper;
	
	@Override
	public List<BuildingScheduleVO> getSchedulesByBuilding(String bldgId, String rentalPtyId) {
	    Map<String, Object> paramMap = new HashMap<>();
	    paramMap.put("bldgId", bldgId);
	    if (rentalPtyId != null) {
	        paramMap.put("rentalPtyId", rentalPtyId);
	    }
	    return buildingScheduleMapper.selectSchedulesByBuilding(paramMap);
	}

	@Override
	public void createSchedule(BuildingScheduleVO schedule) {
	    String maxId = buildingScheduleMapper.selectMaxScheduleId();
	    int nextSeq = (maxId != null && maxId.length() == 8)
	                    ? Integer.parseInt(maxId.substring(1)) + 1 : 1;
	    String newId = String.format("B%07d", nextSeq);
	    schedule.setBscId(newId);

	    // ✅ rentalPtyId 보정 추가해야 함
	    if (schedule.getRentalPtyId() != null && schedule.getRentalPtyId().startsWith("M")) {
	        String resolved = tenancyMapper.selectRentalPtyIdByMbrCd(schedule.getRentalPtyId());
	        if (resolved == null) {
	            throw new IllegalArgumentException("임대인 MBR_CD에 해당하는 RENTAL_PTY_ID를 찾을 수 없습니다.");
	        }
	        schedule.setRentalPtyId(resolved);
	    }

	    buildingScheduleMapper.insertSchedule(schedule);
	}

	@Override
	public void updateSchedule(BuildingScheduleVO schedule) {
		buildingScheduleMapper.updateSchedule(schedule);
	}

	@Override
	public void deleteSchedule(String bscId) {
		buildingScheduleMapper.deleteSchedule(bscId);
	}

	@Override
	public BuildingScheduleVO getScheduleById(String bscId) {
		return buildingScheduleMapper.selectScheduleById(bscId);
	}

	@Override
	public String getRentalPtyIdByMbrCd(String mbrCd) {
		return tenancyMapper.selectRentalPtyIdByMbrCd(mbrCd);
	}



}
