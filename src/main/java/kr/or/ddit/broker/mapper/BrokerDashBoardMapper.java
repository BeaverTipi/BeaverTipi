package kr.or.ddit.broker.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.ScheduleVO;

@Mapper
public interface BrokerDashBoardMapper {
    public Long selectCommissionTotal(@Param("mbrCd") String mbrCd, @Param("period") String period);
    public Map<String, Object> selectCommissionTrend(@Param("mbrCd") String mbrCd, @Param("period") String period);
    public Map<String, Object> selectContractStatusSummary(@Param("mbrCd") String mbrCd, @Param("period") String period);
    public List<Map<String, Object>> selectContractTrend(@Param("mbrCd") String mbrCd, @Param("period") String period);
    public List<Object> selectNewListings(@Param("mbrCd") String mbrCd);
    public List<Object> selectLongVacantListings(@Param("mbrCd")  String mbrCd);
    public List<Map<String, Object>> selectListingStats(@Param("mbrCd") String mbrCd, @Param("period") String period);
    public List<Object> selectUnpopularListings(@Param("mbrCd") String mbrCd);
    public List<ScheduleVO> selectWeeklySchedule(@Param("mbrCd") String mbrCd);
}
