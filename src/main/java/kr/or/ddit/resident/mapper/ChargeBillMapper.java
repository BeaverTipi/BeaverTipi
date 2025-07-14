package kr.or.ddit.resident.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface ChargeBillMapper {
   
   public List<Map<String, Object>> selectChargeBillComparisonDetail(
           @Param("unitId") String unitId,
           @Param("months") List<String> months

       );
    // 에너지 사용량 요약 (월별 총량)
    public List<Map<String, Object>> selectEnergyUsageSummary(
        @Param("unitId") String unitId,
        @Param("months") List<String> months
    );
    
    List<Map<String, Object>> selectMonthlyCharges(
            @Param("unitId") String unitId,
            @Param("month") String month
        );

        List<Map<String, Object>> selectEnergySummary(
            @Param("unitId") String unitId,
            @Param("month") String month
        );

   
}
