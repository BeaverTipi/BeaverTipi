package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatMessageVO implements Serializable{
	private String cmId;
	private String crId;
	private String mbrCd;
	private String cmCont;
	private LocalDateTime cmTime;
	private String cmImg;

	
	private MemberVO member;
}
