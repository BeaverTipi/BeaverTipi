package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="cardId")
public class CardVO implements Serializable{
	private String cardId;
	private Integer amount;
	private String issuerCode;
	private String acquirerCode;
	private String number;
	private Integer installmentPlanMonths;
	private String approveNo;
	private String useCardPoint;
	private String cardType;
	private String ownerType;
	private String acquireStatus;
	private String isinterestFree;
	private String interestPayer;
	private String paymentKey;
}
