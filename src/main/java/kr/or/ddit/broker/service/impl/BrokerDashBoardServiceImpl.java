package kr.or.ddit.broker.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import kr.or.ddit.broker.mapper.BrokerDashBoardMapper;
import kr.or.ddit.broker.service.BrokerDashBoardService;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class BrokerDashBoardServiceImpl implements BrokerDashBoardService {
	private final BrokerDashBoardMapper mapper;
	@Override
	public Map<String, Object> readDashboardOverview(String mbrCd) {
		// TODO Auto-generated method stub
		return null;
	}

}
