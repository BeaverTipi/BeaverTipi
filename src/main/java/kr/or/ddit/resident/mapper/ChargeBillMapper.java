package kr.or.ddit.resident.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.ChargeBillVO;
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
    // ✅ 청구서 납부금액 업데이트
    public int updateChargeBillAfterPayment(@Param("chgbillChargeMonth") String chgbillChargeMonth,
                                     @Param("rentalPtyId") String rentalPtyId,
                                     @Param("unitId") String unitId,
                                     @Param("bldgId") String bldgId,
                                     @Param("amount") int amount);

    // ✅ 납부 로그 기록
    public int insertChargeBillPaymentLog(@Param("chgbillChargeMonth") String chgbillChargeMonth,
                                   @Param("rentalPtyId") String rentalPtyId,
                                   @Param("unitId") String unitId,
                                   @Param("bldgId") String bldgId,
                                   @Param("paymentKey") String paymentKey,
                                   @Param("amount") int amount,
                                   @Param("method") String method,
                                   @Param("methodGrpCd") String methodGrpCd);
    
    public ChargeBillVO selectChargeBillInfo(
    	    @Param("chgbillChargeMonth") String chgbillChargeMonth,
    	    @Param("unitId") String unitId
    	);

    // ✅ 미납 청구서 목록 조회
    public List<ChargeBillVO> selectUnpaidChargeBills(@Param("mbrCd") String mbrCd);
    
}
