package kr.or.ddit.resident.chating.service;

import java.util.List;

import kr.or.ddit.vo.ResidentChatRoomVO;

public interface RsdChatServcie {
	
	public List<ResidentChatRoomVO> getChatList();
	
	public int createChatRoom(ResidentChatRoomVO rsdChatRoomVO);
}
