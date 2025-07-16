package kr.or.ddit.resident.chating.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.or.ddit.resident.chating.dto.ChatMessageDTO;
import kr.or.ddit.resident.chating.dto.LastMessageDTO;
import kr.or.ddit.resident.chating.dto.ParticipantDTO;
import kr.or.ddit.resident.mapper.RsdChatMapper;
import kr.or.ddit.util.websocket.ChatListSocketHandler;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.ChatRoomInVO;
import kr.or.ddit.vo.ResidentChatMessageVO;
import kr.or.ddit.vo.ResidentChatRoomVO;
import kr.or.ddit.vo.UnitResidentVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RsdChatServiceImpl implements RsdChatServcie {

    private final RsdChatMapper mapper;
    private final ChatListSocketHandler chatListSocketHandler; // ✅ 브로드캐스트 핸들러 주입

    @Override
    public void createChatRoom(
        ResidentChatRoomVO crVO,
        ChatRoomInVO criVO,
        List<ChatRoomInVO> residentList
    ) {
        // 1️⃣ 채팅방 생성
        mapper.insertChatRoom(crVO);

        // 2️⃣ 개설자 참여 등록
        criVO.setResidentChatRoomId(crVO.getResidentChatRoomId());
        mapper.insertChatRoomIn(criVO);

        // 3️⃣ 선택된 입주민 참여 등록
        for (ChatRoomInVO resident : residentList) {
            resident.setResidentChatRoomId(crVO.getResidentChatRoomId());
            mapper.insertChatRoomIn(resident);
        }

        // 4️⃣ 개설자 정보 조회 (unitRoom 등)
        ChatMessageDTO creator = mapper.selectWhoIsSender(crVO.getMbrCd(), crVO.getResidentChatRoomId());

        // 5️⃣ 브로드캐스트를 위한 payload 구성
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "newRoom");
        payload.put("residentChatRoomId", crVO.getResidentChatRoomId());
        payload.put("residentChatRoomTitle", crVO.getResidentChatRoomTitle());
        payload.put("lastMessage", ""); // 프리뷰 없음
        payload.put("sender", creator.getMbrNnm());
        payload.put("unitRoom", creator.getUnitRoom());

        chatListSocketHandler.broadcastNewChatRoom(payload); // ✅ 목록 브로드캐스트 전송
    }

    // 👇 이하 기존 메서드는 그대로 유지

    @Override
    public List<ResidentChatRoomVO> getBuildingChatList(String mbrCd, String bldgId) {
        return mapper.selectBuildingChatList(mbrCd, bldgId);
    }

    @Override
    public List<BuildingVO> getResidentBuildingList(String mbrCd) {
        return mapper.selectResidentBuildingList(mbrCd);
    }

    @Override
    public List<UnitResidentVO> getResidentList(UnitResidentVO uriVO) {
        return mapper.selectResidentList(uriVO);
    }

    @Override
    public List<ChatMessageDTO> getMessages(String residentChatRoomId, String mbrCd) {
        return mapper.selectMessages(residentChatRoomId, mbrCd);
    }
    
    @Override
    public void createMessage(ResidentChatMessageVO rcmVO) {
        mapper.insertChatMessage(rcmVO);
    }

    @Override
    public ChatMessageDTO getWhoIsSender(String mbrCd, String residentChatRoomId) {
        return mapper.selectWhoIsSender(mbrCd, residentChatRoomId);
    }

    @Override
    public List<ParticipantDTO> getParticiapntList(String residentChatRoomId) {
        return mapper.selectChatParticipantList(residentChatRoomId);
    }

    @Override
    public void editLeaveChatRoom(ChatRoomInVO criVO) {
        mapper.updateLeaveChatRoom(criVO);
    }

    @Override
    public ResidentChatRoomVO getResidentChatRoomInfo(String residentChatRoomId) {
        return mapper.selectResidentChatRoomInfo(residentChatRoomId);
    }

    @Override
    public void createInviteChatRoom(String residentChatRoomId, List<String> inviteMbrCdList) {
        for (String mbrCd : inviteMbrCdList) {
            mapper.inviteChatRoom(residentChatRoomId, mbrCd);
        }
    }

    @Override
    public List<UnitResidentVO> getNotInChatRoomResidentList(String residentChatRoomId) {
        return mapper.selectNotInChatRoomResidentList(residentChatRoomId);
    }

	@Override
	public LastMessageDTO getLastMessage(String residentChatRoomId) {
		return mapper.selectLastMessage(residentChatRoomId);
	}



}