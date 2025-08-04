package kr.or.ddit.broker.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.or.ddit.broker.mapper.BrokerDashBoardMapper;
import kr.or.ddit.broker.service.BrokerDashBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
@RequiredArgsConstructor
public class BrokerDashBoardServiceImpl implements BrokerDashBoardService {
	private final BrokerDashBoardMapper mapper;
	@Override
	public Map<String, Object> readDashboardOverview(String mbrCd,String period) {
		 Map<String, Object> data = new HashMap<>();
		    
		    data.put("commissionTotal", mapper.selectCommissionTotal(mbrCd,period));
		    data.put("weeklySchedule", mapper.selectWeeklySchedule(mbrCd));
		    data.put("contractStatusSummary", this.readContractStatusSummary(mbrCd,period));
		    data.put("contractTrend", mapper.selectContractTrend(mbrCd,period));
		    data.put("newListings", mapper.selectNewListings(mbrCd));
		    data.put("unpopularListings", mapper.selectUnpopularListings(mbrCd));
		    data.put("longVacantListings", mapper.selectLongVacantListings(mbrCd));
		    data.put("listingStats", this.readListingStats(mbrCd, period));

		    return data;
	}
	
	
	 @Override
	    public Long readCommissionTotal(String mbrCd, String period) {
	        return mapper.selectCommissionTotal(mbrCd, period);
	    }

	 @Override
	 public Map<String, Object> readContractStatusSummary(String mbrCd, String period) {
		 Map<String, Object> raw = mapper.selectContractStatusSummary(mbrCd, period);

		    log.info("raw summary: {}", raw);

		    int success = ((Number) raw.getOrDefault("SUCCESS", 0)).intValue();
		    int cancel = ((Number) raw.getOrDefault("CANCEL", 0)).intValue();
		    int pending = ((Number) raw.getOrDefault("PENDING", 0)).intValue();
		    int total = ((Number) raw.getOrDefault("TOTAL", success + cancel + pending)).intValue(); // 혹시 TOTAL이 없을 경우에도 대비

		    Map<String, Object> result = new HashMap<>();
		    result.put("success", success);
		    result.put("cancel", cancel);
		    result.put("pending", pending);
		    result.put("total", total);

		    return result;
	 }


	    @Override
	    public Map<String, Object> readContractTrend(String mbrCd, String period) {
	        List<Map<String, Object>> rawTrend = mapper.selectContractTrend(mbrCd, period);

	        // 예: bucket = "27", "28", "29", ...
	        Map<String, Map<String, Integer>> trendMap = new LinkedHashMap<>();

	        for (Map<String, Object> row : rawTrend) {
	            String bucket = (String) row.get("BUCKET");
	            String status = (String) row.get("CONT_STAT_CD");
	            int count = ((Number) row.get("COUNT")).intValue();

	            trendMap
	                .computeIfAbsent(bucket, k -> new HashMap<>())
	                .put(status, count);
	        }

	        List<String> labels = new ArrayList<>(trendMap.keySet());
	        List<Integer> success = new ArrayList<>();
	        List<Integer> cancel = new ArrayList<>();
	        List<Integer> pending = new ArrayList<>();

	        for (String bucket : labels) {
	            Map<String, Integer> group = trendMap.get(bucket);
	            success.add(group.getOrDefault("002", 0));
	            cancel.add(group.getOrDefault("003", 0));
	            pending.add(group.getOrDefault("001", 0));
	        }

	        return Map.of(
	            "labels", labels,
	            "series", List.of(
	                Map.of("name", "success", "data", success),
	                Map.of("name", "cancel", "data", cancel),
	                Map.of("name", "pending", "data", pending)
	            )
	        );
	    }



	    @Override
	    public Map<String, Object> readListingStats(String mbrCd, String period) {
	    	List<Map<String, Object>> rawStats = mapper.selectListingStats(mbrCd, period);

	    	List<String> labels = new ArrayList<>();
	    	List<Integer> viewStats = new ArrayList<>();
	    	List<Integer> inquiryStats = new ArrayList<>();

	    	for (Map<String, Object> stat : rawStats) {
	    		labels.add((String) stat.get("LSTG_NM"));
	    		viewStats.add(((Number) stat.getOrDefault("VIEWS", 0)).intValue());
	    		inquiryStats.add(((Number) stat.getOrDefault("INQUIRIES", 0)).intValue());
	    	}

	    	return Map.of(
	    		"categories", labels,
	    		"viewStats", viewStats,
	    		"inquiryStats", inquiryStats
	    	);
	    }


		@Override
		public List<Map<String, Object>> readCommissionTrend(String mbrCd, String period) {
			// TODO Auto-generated method stub
			return mapper.selectCommissionTrend(mbrCd, period);
		}


}
