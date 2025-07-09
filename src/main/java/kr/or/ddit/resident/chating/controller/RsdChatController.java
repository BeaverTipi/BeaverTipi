package kr.or.ddit.resident.chating.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.resident.chating.dto.ChatMessageDTO;
import kr.or.ddit.resident.chating.service.RsdChatServiceImpl;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.util.validate.InsertGroup;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.ChatRoomInVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.ResidentChatRoomVO;
import kr.or.ddit.vo.UnitResidentVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/resident")
public class RsdChatController {

	@Autowired
	RsdChatServiceImpl service;
//  채팅 팝업
	@GetMapping("/chat")
	public String residentChat() {

	    return "resident/chat/Chat";
	}
	
//	채팅방 생성 관련
	@GetMapping("/chat/createRoom")
	public String formChatRoom() {
		return "resident/chat/CreateChatRoom";
	}
	
	@PostMapping("/chat/createRoom")
	public String createChatRoom(
		@Validated(InsertGroup.class)
	    @ModelAttribute ResidentChatRoomVO crVO,
	    @RequestParam("selectedMembers[]") List<String> selectedMbrCds,
	    @AuthenticationPrincipal RealUserWrapper<MemberVO> principal
	) {
	    String creatorMbrCd = principal.getRealUser().getMbrCd();
	    crVO.setMbrCd(creatorMbrCd);

	    ChatRoomInVO criVO = new ChatRoomInVO();
	    criVO.setMbrCd(creatorMbrCd);

	    List<ChatRoomInVO> residentList = selectedMbrCds.stream()
	        .map(mbrCd -> {
	            ChatRoomInVO vo = new ChatRoomInVO();
	            vo.setMbrCd(mbrCd);
	            return vo;
	        })
	        .toList();

	    service.createChatRoom(crVO, criVO, residentList);

	    return "redirect:/resident/chat?popup=true";
	}
	
	@GetMapping("/chat/residentList")
	public String residentList(
		@RequestParam("bldgId") String bldgId
		) {
		return "resident/chat/ResidentList";
	}
	
	@GetMapping("/chat/getResidentList")
	@ResponseBody
	public List<UnitResidentVO> getResidentList(
		@RequestParam("bldgId") String bldgId			
		) {
		return service.getResidentList(bldgId);
	}
	
	
	
//	채팅방 관련
	@GetMapping("/chat/room")
	public String chatRoom(
	    @RequestParam("residentChatRoomId") String residentChatRoomId,
	    Model model,
	    @AuthenticationPrincipal RealUserWrapper<MemberVO> principal	
	) {
		String mbrCd = principal.getRealUser().getMbrCd();
		
	    List<ChatMessageDTO> messages = service.getMessages(residentChatRoomId);
	    model.addAttribute("messages", messages);
	    model.addAttribute("residentChatRoomId", residentChatRoomId);
	    model.addAttribute("mbrCd", mbrCd);
	    return "resident/chat/ChatRoom";
	}
	
	
	
	
	
	
//	참여중인 건물별 채팅 목록 조회
	@GetMapping("/chat/list")
	@ResponseBody
	public List<ChatRoomInVO> getChatRoomList(
	    @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
	    @RequestParam("bldgId") String bldgId
	) {
	    String mbrCd = principal.getRealUser().getMbrCd();
	    return service.getBuildingChatList(mbrCd, bldgId);
	}
	
//	입주중인 건물 목록
	@GetMapping("/chat/residentBuilding")
	@ResponseBody
	public List<BuildingVO> getResidentBuildingList(
		@AuthenticationPrincipal RealUserWrapper<MemberVO> principal	
		) {
		String mbrCd = principal.getRealUser().getMbrCd();
		return service.getResidentBuildingList(mbrCd);
	}
	
	
}