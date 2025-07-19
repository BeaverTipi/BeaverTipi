package kr.or.ddit.building.chargeBill.controller;

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

import kr.or.ddit.building.chargeBill.service.AccountBillWriteService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.ManagementEntityMonthlyChargeAggregationVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.TenancyAccountVO;
import kr.or.ddit.vo.UnitVO;

@Controller
@RequestMapping("/building/accountBill")
public class AccountBillWriteController {
	
	@Autowired
	AccountBillWriteService service;
	
	@GetMapping("/write")
	public String giro() {
		return "building/payments/giro";
	}
	
	@GetMapping("/buildings")
	@ResponseBody
	public List<BuildingVO> buildings(
			@AuthenticationPrincipal RealUserWrapper<MemberVO> principal
			) {
		String mbrCd = principal.getRealUser().getMbrCd();
		return service.getOwnBuildings(mbrCd);
	}
	
	@GetMapping("/unitPopup")
	public String unitPopup(
	  @RequestParam("bldgId") String bldgId,
	  Model model,
	  @AuthenticationPrincipal RealUserWrapper<MemberVO> principal
	) {
	  String rentalPtyId = service.getRentalPty(principal.getRealUser().getMbrCd());
	  List<UnitVO> unitList = service.getUnits(bldgId, rentalPtyId); 
	  model.addAttribute("unitList", unitList);
	  return "building/payments/giroUnit"; 
	}
	
	@GetMapping("/accounts")
	@ResponseBody
	public List<TenancyAccountVO> accounts(
			@AuthenticationPrincipal RealUserWrapper<MemberVO> principal
			) {
		String mbrCd = principal.getRealUser().getMbrCd();
		return service.getAccounts(mbrCd);
	}
	
	@GetMapping("/usage")
	@ResponseBody
	public List<ManagementEntityMonthlyChargeAggregationVO> OwnUsage(
			@RequestParam("unitId") String unitId
			) {
		return service.getOwnUsage(unitId);
	}
	

	
}
