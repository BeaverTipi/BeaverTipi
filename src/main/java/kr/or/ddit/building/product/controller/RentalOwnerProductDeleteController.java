package kr.or.ddit.building.product.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.building.product.service.RentalOwnerProductService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

	@Controller
	@RequestMapping("/building/product")
	@Slf4j
	public class RentalOwnerProductDeleteController {

	    @Autowired
	    private RentalOwnerProductService service;

	    @GetMapping("/delete")
	    public String deleteProduct(@RequestParam("lstgId") String lstgId,
	                                @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {

	        MemberVO member = principal.getRealUser();
	        if (member == null || member.getMbrCd() == null) {
	            return "redirect:/member/register";
	        }

	        ListingVO listing = service.selectProductById(lstgId);
	        if (listing == null || !member.getMbrCd().equals(listing.getMbrCd())) {
	            log.warn("삭제 권한 없음 or 매물 없음: {}", lstgId);
	            return "redirect:/building/product/list";
	        }

	        service.deleteProduct(lstgId);

	        return "redirect:/building/product/list";
	    }
	}


