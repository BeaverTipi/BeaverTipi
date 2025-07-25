package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(of= {"contDtSignId"})
public class ContractDigitalSignVO implements Serializable{
	private String contDtIpAddr;
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
