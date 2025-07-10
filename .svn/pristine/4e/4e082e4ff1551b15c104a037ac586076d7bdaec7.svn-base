package kr.or.ddit.resident.chating.service;

import java.util.List;

import kr.or.ddit.resident.chating.dto.ChatMessageDTO;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.ChatRoomInVO;
import kr.or.ddit.vo.ResidentChatMessageVO;
import kr.or.ddit.vo.ResidentChatRoomVO;
import kr.or.ddit.vo.UnitResidentVO;

public interface RsdChatServcie {
	
	public List<ChatRoomInVO> getBuildingChatList(String mbrCd, String bldgId);

	public void createChatRoom(		
			ResidentChatRoomVO crVO,
			ChatRoomInVO criVO,
			List<ChatRoomInVO> residentList);

	public List<BuildingVO> getResidentBuildingList(String mbrCd);

	public List<UnitResidentVO> getResidentList(String bldgId);
	
	public List<ChatMessageDTO> getMessages(String residentChatRoomId);
	
	public void createMessage(ResidentChatMessageVO rcmVO);
	
	public ChatMessageDTO getWhoIsSender(String mbrCd, String residentChatRoomId);
}
