package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(of="brokConfId")
public class BrokerageComfirmationVO implements Serializable{
	private String brokConfId;
	private String contId;
	private String brokConfFile;
	private String crtfNo;
	private String signDtm;
	private String fileId;
}
