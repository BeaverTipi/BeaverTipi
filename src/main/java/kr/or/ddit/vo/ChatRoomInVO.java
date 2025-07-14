package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="residentChatRoomId")
public class ChatRoomInVO implements Serializable{
	private String residentChatRoomId;
	private String mbrCd;
	private String residentChatRoomStatusCode;
	private LocalDateTime chatJoinDate;
	private LocalDateTime chatLeaveDate;
}
