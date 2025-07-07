package kr.or.ddit.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="residentChatRoomId")
public class ResidentChatRoomVO {
	private String residentChatRoomId;
	private String bldgId;
	private String mbrCd;
	private String residentChatRoomDate;
	private String residentChatRoomTitle;
	private String residentChatRoomIsPublic;
}
