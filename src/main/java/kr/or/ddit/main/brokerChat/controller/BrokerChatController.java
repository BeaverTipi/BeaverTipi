package kr.or.ddit.main.brokerChat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.main.brokerChat.service.BrokerChatService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.BrokerChatParticipantVO;
import kr.or.ddit.vo.BrokerChatroomVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.MemberVO;

@Controller
@RequestMapping("/broker/chat")
public class BrokerChatController {
	@Autowired
	BrokerChatService service;
	
	@PostMapping("/create")
	public void createChatRoom(
			@RequestParam("lstgId") String lstgId,
			@AuthenticationPrincipal RealUserWrapper<MemberVO> principal ) {
		BrokerChatroomVO bcVO = new BrokerChatroomVO();
		ListingVO lvo = service.getListingInfo(lstgId);
		bcVO.setInquirerCd(principal.getRealUser().getMbrCd());
		bcVO.setSellerCd(lvo.getMbrCd());
		bcVO.setCrTitle(lvo.getLstgNm());
		bcVO.setLstgId(lstgId);
		service.createChatRoom(bcVO);		
	}
	
	
	@GetMapping("/list")
	public List<BrokerChatParticipantVO> getChatList(
			@AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
		String mbrCd = principal.getRealUser().getMbrCd();
		
		return service.getChatRoomList(mbrCd);
	}
	
	
	
}
