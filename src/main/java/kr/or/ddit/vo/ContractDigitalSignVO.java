package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(of= {"contDtSignId"})
public class ContractDigitalSignVO implements Serializable{
	private String contDtIpAddr;
	@ToString.Exclude
	private String contDtBaseData;
	private String contDtSignId;
	private String contId;
	private String contDtSignType;
	private String contDtSignDtm;
	private String contDtSignImg;
	private String contDtSignHashVal;
	private String contDtSignStat;
	private String mbrCd;
}
