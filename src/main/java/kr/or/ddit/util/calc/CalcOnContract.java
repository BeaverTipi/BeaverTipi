package kr.or.ddit.util.calc;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalcOnContract {

	/*
	 * 5천만원 미만: 거래금액의 0.5% 이내 5천만원 이상 ~ 1억원 미만: 거래금액의 0.4% 이내 1억원 이상 ~ 6억원 미만: 거래금액의
	 * 0.3% 이내 6억원 이상 ~ 12억원 미만: 거래금액의 0.4% 이내 12억원 이상 ~ 15억원 미만: 거래금액의 0.5% 이내 15억원
	 * 이상: 거래금액의 0.6% 이내
	 */
	public static BigDecimal calculateAgentCommissionRate(long deposit) {
		if (deposit < 50_000_000L)
			return BigDecimal.valueOf(0.005); // 0.5%
		if (deposit < 100_000_000L)
			return BigDecimal.valueOf(0.004); // 0.4%
		if (deposit < 600_000_000L)
			return BigDecimal.valueOf(0.003); // 0.3%
		if (deposit < 1_200_000_000L)
			return BigDecimal.valueOf(0.004); // 0.4%
		if (deposit < 1_500_000_000L)
			return BigDecimal.valueOf(0.005); // 0.5%
		return BigDecimal.valueOf(0.006); // 0.6%
	}

	public static Long getTaxAmount(Long deposit) {
		// 수수료율 계산
		BigDecimal commissionRate = calculateAgentCommissionRate(deposit);

		// 수수료 금액 계산 (소수점 이하 절사 or 반올림 선택 가능)
		Long commission = BigDecimal
				.valueOf(deposit)
				.multiply(commissionRate)
				.setScale(0, RoundingMode.DOWN /* RoundingMode.HALF_UP */)
				.longValue();

		// 최소 수수료 적용 (예: 5만원)
		long minimumFee = 50_000L;
		return Math.max(commission, minimumFee);
	}
}
