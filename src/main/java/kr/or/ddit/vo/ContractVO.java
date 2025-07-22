package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(of= {"contId"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractVO implements Serializable{
	private String contId;
	private String mbrCd;
	private String mbrCdBrok;
	private String lstgId;
	private String contTypeCode;
	private String contTypeGroupCd;
	private Long contDeposit;
	private Long contTaxAmount;
	private Long contAmount;
	private String contStatCd;
	private String contDtm;
	private String contStatGroupCd;
	private String contDelYn;
	private String contSignYn;
	
	private transient MemberVO lesseeInfo;   // 임차인
	private transient BrokerVO brokerInfo;   // 중개인
	private transient TenancyVO tenancyInfo; // 임대인
	
	private transient ListingVO listingInfo;
}
