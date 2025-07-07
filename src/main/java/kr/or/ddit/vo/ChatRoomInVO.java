package kr.or.ddit.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="residentChatRoomId")
public class ChatRoomInVO {
	private String residentChatRoomId;
	private String mbrCd;
}
