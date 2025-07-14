package kr.or.ddit.resident.chargebill.service;

import java.util.List;
import java.util.Map;

import kr.or.ddit.resident.chargebill.dto.ChargeComparisonDto;
public interface PaymentService {
   
   public List<Map<String, Object>> selectChargeBillComparisonDetail(
           String unitId,
           String currentMonth,
           String previousMonth
       );

   public List<ChargeComparisonDto> getChargeComparisonList(
           String unitId,
              String currentMonth,
              String previousMonth
              
         );
   
   public Map<String, Map<String, Object>> getEnergyUsageSummary(String unitId, String currentMonth, String previousMonth);

   
   
}
   