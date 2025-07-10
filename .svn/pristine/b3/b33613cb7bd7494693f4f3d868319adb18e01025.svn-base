package kr.or.ddit.resident.chating.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.resident.mapper.RsdChatMapper;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.ChatRoomInVO;
import kr.or.ddit.vo.ResidentChatRoomVO;
import kr.or.ddit.vo.UnitResidentVO;

@Service
public class RsdChatServiceImpl implements RsdChatServcie {

	@Autowired
	RsdChatMapper mapper;
	

	@Override
	public int createChatRoom(ResidentChatRoomVO rsdChatRoomVO) {
		return mapper.insertChatRoom(rsdChatRoomVO);
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

}