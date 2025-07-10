package kr.or.ddit.building.account.controller;


import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.building.account.service.TenancyAccountService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.MemberVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/building/account")
public class TenancyAccountBuildingListContoller {

    @Autowired
    private TenancyAccountService accountService;

    @GetMapping("/buildingList")
    public List<BuildingVO> buildingListAjax(@AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
        String mbrCd = principal.getRealUser().getMbrCd();
        String rentalPtyId = accountService.findRentalPtyIdByMbrCd(mbrCd);

        if (rentalPtyId == null) {
            return Collections.emptyList();
        }

        return accountService.retrieveBuildingListByRentalPtyId(rentalPtyId);
    }
	}

