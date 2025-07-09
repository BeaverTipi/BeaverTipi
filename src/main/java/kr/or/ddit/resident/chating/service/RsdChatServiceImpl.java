package kr.or.ddit.resident.chating.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.resident.chating.dto.ChatMessageDTO;
import kr.or.ddit.resident.mapper.RsdChatMapper;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.ChatRoomInVO;
import kr.or.ddit.vo.ResidentChatMessageVO;
import kr.or.ddit.vo.ResidentChatRoomVO;
import kr.or.ddit.vo.UnitResidentVO;

@Service
public class RsdChatServiceImpl implements RsdChatServcie {

	@Autowired
	RsdChatMapper mapper;
	

	@Override
	public void createChatRoom(
	    ResidentChatRoomVO crVO,
	    ChatRoomInVO criVO,
	    List<ChatRoomInVO> residentList
	) {
//	    채팅방 생성
	    mapper.insertChatRoom(crVO);

//	    개설자 참여 등록
	    criVO.setResidentChatRoomId(crVO.getResidentChatRoomId());
	    mapper.insertChatRoomIn(criVO);

//	    선택된 입주민 참여 등록
	    for (ChatRoomInVO resident : residentList) {
	        resident.setResidentChatRoomId(crVO.getResidentChatRoomId());
	        mapper.insertChatRoomIn(resident);
	    }
	}

	@Override
	public List<ChatRoomInVO> getBuildingChatList(String mbrCd, String bldgId) {
		return mapper.selectBuildingChatList(mbrCd, bldgId);
	}

	@Override
	public List<BuildingVO> getResidentBuildingList(String mbrCd) {
		return mapper.selectResidentBuildingList(mbrCd);
	}

	@Override
	public List<UnitResidentVO> getResidentList(String bldgId) {
		return mapper.selectResidentList(bldgId);
	}

	@Override
	public List<ChatMessageDTO> getMessages(String residentChatRoomId) {
		return mapper.selectMessages(residentChatRoomId);
	}

	@Override
	public void createMessage(ResidentChatMessageVO rcmVO) {
		mapper.insertChatMessage(rcmVO);
		
	}


}