package kr.or.ddit.resident.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.resident.chating.dto.ChatMessageDTO;
import kr.or.ddit.resident.chating.dto.LastMessageDTO;
import kr.or.ddit.resident.chating.dto.ParticipantDTO;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.ChatRoomInVO;
import kr.or.ddit.vo.ResidentChatMessageVO;
import kr.or.ddit.vo.ResidentChatRoomVO;
import kr.or.ddit.vo.UnitResidentVO;


@Mapper
public interface RsdChatMapper {
	
	public List<BuildingVO> selectResidentBuildingList(String mbrCd);
	
	public List<ChatRoomInVO> selectBuildingChatList(
			@Param("mbrCd") String mbrCd,
			@Param("bldgId") String bldgId
			);
	
	public int insertChatRoom(ResidentChatRoomVO rsdChatRoomVO);
	
	public List<UnitResidentVO> selectResidentList(UnitResidentVO uriVO); 
	
	public List<UnitResidentVO> selectNotInChatRoomResidentList(String residentChatRoomId); 
	
	public int insertChatRoomIn(ChatRoomInVO criVO);
	
	public List<ChatMessageDTO> selectMessages(
			  @Param("residentChatRoomId") String residentChatRoomId,
			  @Param("mbrCd") String mbrCd 
			);
	
	public int insertChatMessage(ResidentChatMessageVO rcmVO);
	
	public ChatMessageDTO selectWhoIsSender(
			@Param("mbrCd") String mbrCd,
			@Param("residentChatRoomId") String residentChatRoomId
			); 
	public List<ParticipantDTO> selectChatParticipantList(String residentChatRoomId);
	
	public int updateLeaveChatRoom(ChatRoomInVO criVO);
	
	public ResidentChatRoomVO selectResidentChatRoomInfo(String residentChatRoomId);
	
	public int inviteChatRoom(
			  @Param("residentChatRoomId") String residentChatRoomId,
			  @Param("mbrCd") String mbrCd 
			);
	public LastMessageDTO selectLastMessage(String residentChatRoomId);
	
	
}
