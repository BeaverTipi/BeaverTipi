package kr.or.ddit.resident.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.ChatRoomInVO;
import kr.or.ddit.vo.ResidentChatRoomVO;

@Mapper
public interface RsdChatMapper {
	List<ChatRoomInVO> selectChatList();
	
	int insertChatRoom(ResidentChatRoomVO rsdChatRoomVO);
	
	
}
