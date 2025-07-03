package kr.or.ddit.vo;

import java.io.Serializable;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author developer_KCY
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
public class BrokerVO extends MemberVO implements Serializable{
	@Size(max = 200)
		private String brokNm;
	@Size(max = 10)
		private String brokRegNo;
	@Size(max = 50)
		private String crtfNo;
	@Size(max = 100)
		private String reprNm;
	@Size(max = 20) //@Pattern(regexp = "^\\d{2,4}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다 (예: 010-1234-5678)")
		private String reprTelNo;
	//@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}(?:[ T]\\d{2}:\\d{2}(:\\d{2})?)?$", message = "날짜 형식이 올바르지 않습니다 (예: 2025-07-03 또는 2025-07-03T14:00)")
		private String regDtm;
	@Size(max = 255)
		private String brokAddr1;
	@Size(max = 400)
		private String brokAddr2;
}
