package kr.or.ddit.resident.chating.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.resident.chating.service.RsdChatServiceImpl;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.ChatRoomInVO;
import kr.or.ddit.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/resident")
public class RsdChatController {

	@Autowired
	RsdChatServiceImpl service;

	@GetMapping("/chat")
	public String residentChat() {

	    return "resident/chat/Chat";
	}
	
//	채팅방 생성
	@GetMapping("/chat/createRoom")
	public String formChatRoom() {
		return "resident/chat/CreateChatRoom";
	}
	
	@PostMapping("/chat/createRoom")
	public int createChatRoom() {
		
		return 0;
	}

	@GetMapping("/chat/list")
	@ResponseBody
	public List<ChatRoomInVO> getChatRoomList(
	    @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
	    @RequestParam("bldgId") String bldgId
	) {
	    String mbrCd = principal.getRealUser().getMbrCd();
	    return service.getBuildingChatList(mbrCd, bldgId);
	}
	
	@GetMapping("/chat/residentBuilding")
	@ResponseBody
	public List<BuildingVO> getResidentBuildingList(
		@AuthenticationPrincipal RealUserWrapper<MemberVO> principal	
		) {
		String mbrCd = principal.getRealUser().getMbrCd();
		return service.getResidentBuildingList(mbrCd);
	}
	
	
}