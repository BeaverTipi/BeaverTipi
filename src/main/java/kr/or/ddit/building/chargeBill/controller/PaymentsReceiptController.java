package kr.or.ddit.building.chargeBill.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import kr.or.ddit.building.chargeBill.dto.ChargeBillHistoryDTO;
import kr.or.ddit.building.chargeBill.dto.EnergyUsageDTO;
import kr.or.ddit.building.chargeBill.dto.IntegratedMgmtFeeDTO;
import kr.or.ddit.building.chargeBill.service.PaymentsReceiptService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.ChargeBillVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.UnitVO;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/building/payments/receipt")
@Slf4j
public class PaymentsReceiptController {

	@Autowired
	PaymentsReceiptService service;
	
	
	@GetMapping("/list")
	public String paymentsProofList(@AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
			Model model ) {
		String mbrCd = principal.getRealUser().getMbrCd();
		String rentalPtyId = service.getRentalPtyId(mbrCd);
		model.addAttribute("rentalPtyId", rentalPtyId);
		return "building/managed/paymentsReceiptList";
	}
	
	@GetMapping("/list/history")
	@ResponseBody
	public Map<String, Object> getChargeBillHistoryPaged(
	    ChargeBillHistoryDTO cbhDTO,
	    @RequestParam(defaultValue = "1") int page,
	    @RequestParam(defaultValue = "15") int pageSize
	) {
	    // 1) 페이징 계산
	    int startRow = (page - 1) * pageSize + 1;
	    int endRow = page * pageSize;

	    // 2) 전체 건수 조회
	    int totalCount = service.getChargeBillHistoryCount(cbhDTO); // ✅ 인터페이스와 일치

	    // 3) 페이징된 리스트 조회 (start/end 직접 전달)
	    List<ChargeBillHistoryDTO> pagedList = service.getChargeBillHistoryPaged(cbhDTO, startRow, endRow);

	    // 4) 결과 조립
	    Map<String, Object> result = new HashMap<>();
	    result.put("billList", pagedList);
	    result.put("pagination", Map.of(
	        "currentPageNo", page,
	        "pageSize", pageSize,
	        "totalRecordCount", totalCount,
	        "totalPageCount", (int)Math.ceil((double)totalCount / pageSize),
	        "firstPageNoOnPageList", 1,
	        "lastPageNoOnPageList", (int)Math.ceil((double)totalCount / pageSize)
	    ));

	    return result;
	}
	@GetMapping("/list/history/summary")
	@ResponseBody
	public List<Map<String, Object>> getMonthlyChargeBillSummary(
			@AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
			@RequestParam("chgbillChargeMonth") String chgbillChargeMonth
			) {
		String mbrCd = principal.getRealUser().getMbrCd();
		String rentalPtyId = service.getRentalPtyId(mbrCd);
		return service.getMonthlyChargeBillSummary(rentalPtyId, chgbillChargeMonth);
	}
	
	@GetMapping("/list/history/buildings")
	@ResponseBody
	public List<BuildingVO> getOwnBuildings(@AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
		String mbrCd = principal.getRealUser().getMbrCd();
		String rentalPtyId = service.getRentalPtyId(mbrCd);
		return service.getOwnBuildings(rentalPtyId);
	}
	
	@GetMapping("/list/history/units")
	@ResponseBody
	public List<UnitVO> getUnits(
			@RequestParam("bldgId") String bldgId,
			@AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
		String mbrCd = principal.getRealUser().getMbrCd();
		String rentalPtyId = service.getRentalPtyId(mbrCd);
		return service.getUnits(bldgId, rentalPtyId);
	}
	
	@GetMapping("/list/history/details")
	public String getDetails(
		@RequestParam("chgbillChargeMonth") String chgbillChargeMonth,
		@RequestParam("unitId") String unitId,
	    Model model
	) {

		ChargeBillHistoryDTO cbhDTO = service.getChargebill(chgbillChargeMonth, unitId);
	    List<EnergyUsageDTO> energyUsage = service.getEnergyUsage(chgbillChargeMonth, unitId);
	    List<IntegratedMgmtFeeDTO> managementFee = service.getManagementFee(chgbillChargeMonth, unitId);
	    

	    
	    
	    model.addAttribute("cbhDTO", cbhDTO);               
	    model.addAttribute("energyUsage", energyUsage);    
	    model.addAttribute("managementFee", managementFee); 

	    return "building/managed/chargebillDetailModal"; 
	}
	
}
