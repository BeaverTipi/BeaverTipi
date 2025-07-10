package kr.or.ddit.resident.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.ChatRoomInVO;
import kr.or.ddit.vo.ResidentChatRoomVO;
import kr.or.ddit.vo.UnitResidentVO;


@Mapper
public interface RsdChatMapper {
	
	List<BuildingVO> selectResidentBuildingList(String mbrCd);
	
	List<ChatRoomInVO> selectBuildingChatList(
			@Param("mbrCd") String mbrCd,
			@Param("bldgId") String bldgId
			);
	
	int insertChatRoom(ResidentChatRoomVO rsdChatRoomVO);
	
	List<UnitResidentVO> selectResidentList(String bldgId); 
	
	int insertChatRoomIn(ChatRoomInVO criVO);
}
