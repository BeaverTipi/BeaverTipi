package kr.or.ddit.building.account.controller;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;


import kr.or.ddit.building.account.service.TenancyAccountService;
import kr.or.ddit.util.security.auth.RealUserWrapper;

import kr.or.ddit.vo.MemberVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/building/account")
public class TenancyAccountBuildingListAjaxContoller {
	 @Autowired
	    private TenancyAccountService accountService;

	    @GetMapping
	    public String viewAccountPage(@AuthenticationPrincipal RealUserWrapper<MemberVO> principal, Model model) {
	        MemberVO member = principal.getRealUser();
	        String mbrCd = member.getMbrCd();

	        String rentalPtyId = accountService.findRentalPtyIdByMbrCd(mbrCd);
	        model.addAttribute("accountList", accountService.retrieveAccountList(mbrCd));
	        model.addAttribute("rentalPtyId", rentalPtyId);

	        return "building/account/accountList";  // JSP 경로
	    }
	}

