package kr.or.ddit.main.brokerChat.service;

import java.util.List;

import kr.or.ddit.vo.BrokerChatParticipantVO;
import kr.or.ddit.vo.BrokerChatroomVO;
import kr.or.ddit.vo.ChatMessageVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.MemberVO;

public interface BrokerChatService {

	public void createChatRoom(BrokerChatroomVO bcVO);
	
	
	public List<BrokerChatroomVO> getChatRoomList(String mbrCd);
	
	public ListingVO getListingInfo(String lstgId);
	
	public List<ChatMessageVO> getMessages(String crId);
	
	public int createMessage(ChatMessageVO cmVO);
	
	public int editLeaveChat(String crId,String mbrCd);
	
	public int editJoinChat(String crId,String mbrCd);
	
	public boolean getCheckChatRoom(String lstgId, String inquirerCd);
	
	public BrokerChatroomVO getChatInfo(String crId);
	
	public BrokerChatroomVO getChatInfoWithLstg(String lstgId, String inquirerCd);
	
	public MemberVO getMemberByMbrCd(String mbrCd);
}
