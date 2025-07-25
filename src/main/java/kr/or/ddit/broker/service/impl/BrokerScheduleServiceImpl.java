package kr.or.ddit.broker.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.ddit.broker.mapper.BrokerScheduleMapper;
import kr.or.ddit.broker.service.BrokerScheduleService;
import kr.or.ddit.util.validate.exception.ScheduleException;
import kr.or.ddit.vo.ScheduleVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrokerScheduleServiceImpl implements BrokerScheduleService {

    private final BrokerScheduleMapper mapper;

    @Override
    public List<ScheduleVO> readScheduleList(String mbrCd) {
        return mapper.selectScheduleList(mbrCd);
    }

    @Override
    @Transactional
    public ScheduleVO createSchedule(ScheduleVO schedule) {
    	String scdId = mapper.selectNextScheduleIdSuffix();
    	schedule.setScdId(scdId);
        if (mapper.insertSchedule(schedule) < 1) {
            throw new ScheduleException("일정 등록 중 오류가 발생했습니다.");
        }
        return schedule;
    }

    @Override
    public ScheduleVO readScheduleDetail(ScheduleVO schedule) {
        ScheduleVO detail = mapper.selectSchedule(schedule);
        if (detail == null) {
            throw new ScheduleException("일정이 존재하지 않습니다.");
        }
        return detail;
    }

    @Override
    public void modifySchedule(ScheduleVO schedule) {
        if (mapper.updateSchedule(schedule) < 1) {
            throw new ScheduleException("일정 수정 중 오류가 발생했습니다.");
        }
    }

    @Override
    public void removeSchedule(ScheduleVO schedule) {
        if (mapper.updateDelYnSchedule(schedule) < 1) {
            throw new ScheduleException("일정 삭제 중 오류가 발생했습니다.");
        }
    }

}

