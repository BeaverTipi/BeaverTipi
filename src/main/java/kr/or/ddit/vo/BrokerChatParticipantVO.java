package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BrokerChatParticipantVO implements Serializable{
	private String crId;
	private String mbrCd;
	private String bcpStatus;
	private LocalDateTime bcpLeavedAt;
}
