package kr.or.ddit.main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.BrokerChatParticipantVO;
import kr.or.ddit.vo.BrokerChatroomVO;
import kr.or.ddit.vo.ChatMessageVO;
import kr.or.ddit.vo.ListingVO;

@Mapper
public interface BrokerChatMapper {
	
	public int insertChatRoom(BrokerChatroomVO bcVO);

	public int insertChatParticipant(@Param("crId") String crId, @Param("mbrCd") String mbrCd);
	
	public List<BrokerChatroomVO> selectChatRoomList(String mbrCd);
	
	public ListingVO selectListingInfo(String lstgId);
	
	public List<ChatMessageVO> selectMessages(String crId);
	
	public int insertMessage(ChatMessageVO cmVO);

	
	public int updateLeaveChat(@Param("crId") String crId, @Param("mbrCd") String mbrCd);
	
	public int updateJoinChat(@Param("crId") String crId, @Param("mbrCd") String mbrCd);
	
	public boolean selectCheckChatRoom(@Param("lstgId") String lstgId, @Param("inquirerCd") String inquirerCd);
	
	public BrokerChatroomVO selectChatInfo(String crId);
	
	public BrokerChatroomVO selectChatInfoWithLstg(@Param("lstgId") String lstgId, @Param("inquirerCd") String inquirerCd);
	
}
