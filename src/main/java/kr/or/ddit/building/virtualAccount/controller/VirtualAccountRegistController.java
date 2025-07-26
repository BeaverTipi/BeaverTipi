package kr.or.ddit.building.virtualAccount.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import kr.or.ddit.building.virtualAccount.service.VirtualAccountService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.VirtualAccountVO;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/virtualAccount")
public class VirtualAccountRegistController {

    @Autowired
    private VirtualAccountService service;

    @PostMapping("/register")
    public VirtualAccountVO register(
        @ModelAttribute VirtualAccountVO vo,
        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal
    ) {
    	 log.info(" 컨트롤러 customerName={}", vo.getCustomerName());
         log.info(" 컨트롤러 virtualAccountAmount={}", vo.getVirtualAccountAmount());
         log.info(" 컨트롤러 bankCode={}", vo.getBankCode());
        MemberVO member = principal != null ? principal.getRealUser() : null;
        if (member == null || member.getMbrCd() == null) {
            throw new RuntimeException("회원코드가 누락되었습니다.");
        }
        vo.setMbrCd(member.getMbrCd());
        return service.registerVirtualAccount(vo);
    }

}
