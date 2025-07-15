package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(of="crId")
public class BrokerChatroomVO implements Serializable{
	private String crId;
	private String inquirerCd;
	private String sellerCd;
	private String crTitle;
	private LocalDateTime crCreDtm;
	private String lstgId;
	private String crStatus;
}
