package kr.or.ddit.main.brokerChat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.main.brokerChat.service.BrokerChatService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.BrokerChatroomVO;
import kr.or.ddit.vo.ChatMessageVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/broker/chat")
@Slf4j
public class BrokerChatController {

    @Autowired
    BrokerChatService service;

    @PostMapping("/create")
    @ResponseBody
    public String createChatRoom(
        @RequestParam("lstgId") String lstgId,
        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
        Model model) {

        String mbrCd = principal.getRealUser().getMbrCd();
        ListingVO lvo = service.getListingInfo(lstgId);

        BrokerChatroomVO bcVO = new BrokerChatroomVO();
        bcVO.setInquirerCd(mbrCd);
        bcVO.setSellerCd(lvo.getMbrCd());
        bcVO.setCrTitle(lvo.getLstgNm());
        bcVO.setLstgId(lstgId);

        String crId;
        if (service.getCheckChatRoom(lstgId, mbrCd)) {
        	crId = service.getChatInfoWithLstg(lstgId, mbrCd).getCrId();
            service.editJoinChat(crId, mbrCd);
            service.editJoinChat(crId, bcVO.getSellerCd());
            List<ChatMessageVO> messages = service.getMessages(crId);
            model.addAttribute("messages", messages);
        } else {
            service.createChatRoom(bcVO);
            crId = bcVO.getCrId();
        }

        bcVO = service.getChatInfo(crId);
        model.addAttribute("bcVO", bcVO);
        model.addAttribute("mbrCd", mbrCd);

        return "redirect:/broker/chat/room?crId=" + crId + "&popup=true";
    }

    @GetMapping("/room")
    public String chatRoom(
        @RequestParam("crId") String crId,
        @RequestParam(value = "popup", required = false) boolean isPopup,
        Model model,
        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {

        String mbrCd = principal.getRealUser().getMbrCd();
        String loginMbrId = principal.getRealUser().getMbrId();
        
        BrokerChatroomVO bcVO = service.getChatInfo(crId);
        List<ChatMessageVO> messages = service.getMessages(crId);
        
        MemberVO sellerMemberVO = service.getMemberByMbrCd(bcVO.getSellerCd());
        MemberVO inquirerMemberVO = service.getMemberByMbrCd(bcVO.getInquirerCd());
        
        model.addAttribute("loginMbrId", loginMbrId);
        model.addAttribute("sellerMemberVO", sellerMemberVO);
        model.addAttribute("inquirerMemberVO", inquirerMemberVO);

        model.addAttribute("mbrCd", mbrCd);
        model.addAttribute("messages", messages);
        model.addAttribute("bcVO", bcVO);
        model.addAttribute("isPopup", isPopup);

        return "main/chat/BrokerChatRoom";
    }

    @GetMapping("/list")
    @ResponseBody
    public List<BrokerChatroomVO> getChatList(
        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
        String mbrCd = principal.getRealUser().getMbrCd();
        return service.getChatRoomList(mbrCd);
    }

    @PostMapping("/room/leave")
    public String chatLeave(
        @RequestParam("crId") String crId,
        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
        String mbrCd = principal.getRealUser().getMbrCd();
        service.editLeaveChat(crId, mbrCd);
        return "redirect:/resident/chat?popup=true";
    }
}