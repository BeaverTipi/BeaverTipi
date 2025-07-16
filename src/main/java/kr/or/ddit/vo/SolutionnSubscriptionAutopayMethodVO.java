package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="automethId")
public class SolutionnSubscriptionAutopayMethodVO implements Serializable{
	private String automethPaytypeGrpCd;
	private String automethIsActiveGrpCd;
	private String automethId;
	private String mbrCd;
	private String automethPaytype;
	private String automethProvider;
	private String automethBillingkey;
	private String automethVrtacct;
	private LocalDate automethStartedAt;
	private LocalDate automethRevokedAt;
	private String automethIsActive;
}
