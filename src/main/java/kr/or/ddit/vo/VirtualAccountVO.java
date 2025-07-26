package kr.or.ddit.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"virtualAccountId"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VirtualAccountVO {
	@JsonProperty("virtualAccountId")
	private String virtualAccountId;
	@JsonProperty("accountType")
	private String accountType;
	@JsonProperty("accountNumber")
	private String accountNumber;
	@JsonProperty("bankCode")
	private String bankCode;
	@JsonProperty("customerName")
	private String customerName;
	@JsonProperty("dueDate")
	private String dueDate;
	@JsonProperty("expired")
	private String expired;
	@JsonProperty("settlementStatus")
	private String settlementStatus;
	@JsonProperty("secret")
	private String secret;
	@JsonProperty("mbrCd")
	private String mbrCd;
	@JsonProperty("virtualAccountAmount")
	private Long virtualAccountAmount;
}
