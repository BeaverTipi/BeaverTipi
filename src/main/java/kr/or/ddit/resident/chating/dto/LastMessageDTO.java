package kr.or.ddit.resident.chating.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LastMessageDTO {
	private String rcmCont;
	private String residentChatRoomId;
	private String mbrNnm;
	private String unitRoom;
	private LocalDateTime rcmTime; 
}
