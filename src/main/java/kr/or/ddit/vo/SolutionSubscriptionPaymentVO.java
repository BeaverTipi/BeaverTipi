package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="subsPaymentId")
public class SolutionSubscriptionPaymentVO implements Serializable{
	private String subsPaymentId;
	private String subsId;
	private String automethId;
	private String cardId;
	private String billingKey;
	private String customerKey;
	private String subsPaymentFailMsg;
	private String mbrCd;
	
}
