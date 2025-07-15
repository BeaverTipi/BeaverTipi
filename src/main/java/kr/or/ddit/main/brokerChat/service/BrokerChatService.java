package kr.or.ddit.main.brokerChat.service;

import java.util.List;

import kr.or.ddit.vo.BrokerChatParticipantVO;
import kr.or.ddit.vo.BrokerChatroomVO;
import kr.or.ddit.vo.ChatMessageVO;
import kr.or.ddit.vo.ListingVO;

public interface BrokerChatService {

	public void createChatRoom(BrokerChatroomVO bcVO);
	
	
	public List<BrokerChatParticipantVO> getChatRoomList(String mbrCd);
	
	public ListingVO getListingInfo(String lstgId);
	
	public List<ChatMessageVO> getMessages(String crId);
	
	public int createMessage(ChatMessageVO cmVO);
	
	public int editLeaveChat(String crId,String mbrCd);
	
	public int editJoinChat(String crId,String mbrCd);
	
	public boolean getCheckChatRoom(String lstgId, String inquirerCd);
}
