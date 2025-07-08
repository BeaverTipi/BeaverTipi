package kr.or.ddit.building.product.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import kr.or.ddit.building.product.service.RentalOwnerProductService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.FacilityOptionVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/building/product")
public class RentalOwnerProductAddController {

    @Autowired
    private RentalOwnerProductService service;

    /**
     * 매물 등록 폼 진입
     * - 로그인된 사용자에서 mbrCd만 꺼냄
     * - 시설 옵션 데이터를 그룹화하여 모델에 담아 전달
     * - mbrCd가 null이면 회원가입 페이지로 리다이렉트
     */
    @GetMapping("/add")
    public String addForm(Model model,
                          @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {

        log.info("[GET] /building/product/add 진입");

        // 1. 로그인된 사용자 정보 꺼냄
        MemberVO sessionMember = principal.getRealUser();
        log.info("로그인 사용자: {}", sessionMember);

        if (sessionMember == null || sessionMember.getMbrCd() == null) {
            log.warn("로그인 안 됐거나 mbrCd 없음 → /member/register 리다이렉트");
            return "redirect:/member/register";
        }

        String mbrCd = sessionMember.getMbrCd();
        log.info("로그인된 사용자 mbrCd: {}", mbrCd);

        // 2. 시설 옵션 전체 조회
        List<FacilityOptionVO> optionList = service.selectAllFacilityOptions();
        log.info("옵션 개수: {}", (optionList != null ? optionList.size() : "null"));

        if (optionList == null || optionList.isEmpty()) {
            log.warn("시설 옵션 데이터 없음");
        }

        // 3. 그룹핑
        Map<String, List<FacilityOptionVO>> facilityMap = optionList.stream()
            .collect(Collectors.groupingBy(FacilityOptionVO::getFacTypeGroupCd));
        log.info("facilityMap.keySet(): {}", facilityMap.keySet());

        // 4. 모델 전달
        model.addAttribute("facilityMap", facilityMap);

        return "building/product/rentalOwnerProductAdd";
    }

    @PostMapping("/add")
    public String processAdd(@ModelAttribute("listingVO") ListingVO listingVO,
                             @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
        
        MemberVO member = principal.getRealUser();

        // 로그인 확인
        if (member == null || member.getMbrCd() == null) {
            return "redirect:/member/register";
        }

        // 필수 정보 셋팅
        listingVO.setMbrCd(member.getMbrCd());

        if (member.getTenancy() != null) {
            listingVO.setRentalPtyId(member.getTenancy().getRentalPtyId());
        }

        log.info("등록 요청 데이터: {}", listingVO);

        // 등록 서비스 호출
        service.insertProduct(listingVO);

        // 등록 완료 후 목록으로 이동
        return "redirect:/building/product/list";
    }
}
