package kr.or.ddit.vo;

import lombok.Data;

@Data
public class ResidentChatMessageVO {
	private String rcmId;
	private String rcmCont;
	private String rcmTime;
	private String rcmImg;
	private String residentChatRoomId;
	private String mbrCd;
}
