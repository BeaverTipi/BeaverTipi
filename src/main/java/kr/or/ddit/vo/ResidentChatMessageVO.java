package kr.or.ddit.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ResidentChatMessageVO {
	private String rcmId;
	private String rcmCont;
	private LocalDateTime rcmTime;
	private String rcmImg; 
	private String residentChatRoomId;
	private String mbrCd; 
}
