package kr.or.ddit.main.brokerChat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.main.mapper.BrokerChatMapper;
import kr.or.ddit.vo.BrokerChatParticipantVO;
import kr.or.ddit.vo.BrokerChatroomVO;
import kr.or.ddit.vo.ChatMessageVO;
import kr.or.ddit.vo.ListingVO;

@Service
public class BrokerChatServiceImpl implements BrokerChatService {

	@Autowired
	BrokerChatMapper mapper;
	
	@Override
	public void createChatRoom(BrokerChatroomVO bcVO) {
		mapper.insertChatRoom(bcVO);
		mapper.insertChatParticipant(bcVO.getCrId(), bcVO.getInquirerCd());
		mapper.insertChatParticipant(bcVO.getCrId(), bcVO.getSellerCd());
	}

	@Override
	public List<BrokerChatParticipantVO> getChatRoomList(String mbrCd) {
		
		return mapper.selectChatRoomList(mbrCd);
	}

	@Override
	public ListingVO getListingInfo(String lstgId) {
		
		return mapper.selectListingInfo(lstgId);
	}

	@Override
	public List<ChatMessageVO> getMessages(String crId) {
		// TODO Auto-generated method stub
		return mapper.selectMessages(crId);
	}

	@Override
	public int createMessage(ChatMessageVO cmVO) {
		// TODO Auto-generated method stub
		return mapper.insertMessage(cmVO);
	}

	@Override
	public int editLeaveChat(String crId, String mbrCd) {
		// TODO Auto-generated method stub
		return mapper.updateLeaveChat(crId, mbrCd);
	}

	@Override
	public int editJoinChat(String crId, String mbrCd) {
		// TODO Auto-generated method stub
		return mapper.updateJoinChat(crId, mbrCd);
	}

	@Override
	public boolean getCheckChatRoom(String lstgId, String inquirerCd) {
		// TODO Auto-generated method stub
		return mapper.selectCheckChatRoom(lstgId, inquirerCd);
	}


}
