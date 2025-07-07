package kr.or.ddit.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"virtualAccountId"})
public class VirtualAccountVO {
	private String virtualAccountId;
	private String accountType;
	private String accountNumber;
	private String bankCode;
	private String customerName;
	private String dueDate;
	private String expired;
	private String settlementStatus;
	private String secret;
	private String paymentKey;
}
