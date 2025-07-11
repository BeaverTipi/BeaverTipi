package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"solId"})
public class SolutionSubscriptionVO implements Serializable{
	private String subsStatusGrpCd;
	private String subsAutoRenewGrpCd;
	private String subsApprovalYn;
	private String subsId;
	private String mbrCd;
	private String solId;
	private String subsStartedAt;
	private String subsUpdatedDate;
	private String subsExpirationDate;
	private String subsStatus;
	private String subsAutoRenewYn;
	private String subsRevokedAt;
	
	private SolutionVO solution;
	private BrokerVO broker;
	private TenancyVO tenancy;
	private MemberVO member;
}
