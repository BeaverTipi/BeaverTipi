package kr.or.ddit.broker.service;

import java.util.List;
import java.util.Map;

public interface BrokerDashBoardService {

	
	public Long readCommissionTotal(String mbrCd, String period);
	public List<Map<String, Object>> readCommissionTrend(String mbrCd, String period);
	public Map<String, Object> readContractStatusSummary(String mbrCd, String period);
	public Map<String, Object> readContractTrend(String mbrCd, String period);

	public Map<String, Object> readDashboardOverview(String mbrCd, String period);
	public Map<String, Object> readListingStats(String mbrCd, String period);

}
