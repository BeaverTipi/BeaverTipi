package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class ResidentChatMessageVO implements Serializable{
	private String rcmId;
	private String rcmCont;
	private String rcmTime;
	private String rcmImg;
	private String residentChatRoomId;
	private String mbrCd;
}
