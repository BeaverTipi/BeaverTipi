package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="residentChatRoomId")
public class ChatRoomInVO implements Serializable{
	private String residentChatRoomId;
	private String mbrCd;
}
