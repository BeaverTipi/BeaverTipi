package kr.or.ddit.building.virtualAccount.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import kr.or.ddit.building.virtualAccount.service.VirtualAccountService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.VirtualAccountVO;


@RestController
@RequestMapping("building/virtualAccount")
public class VirtualAccountListController {

    @Autowired
    private VirtualAccountService service;



    
    @GetMapping("/list")
    public List<VirtualAccountVO> list(
        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal
    ) {
        MemberVO member = principal != null ? principal.getRealUser() : null;
        if (member == null || member.getMbrCd() == null) {
            throw new RuntimeException("회원코드가 누락되었습니다.");
        }
        String mbrCd = member.getMbrCd();
        return service.getVirtualAccountListByMember(mbrCd);
    }
}
