package kr.or.ddit.building.product.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
     * [GET] 매물 등록 폼 진입
     * - 로그인된 사용자 정보에서 mbrCd 추출
     * - 시설 옵션 목록 조회 후 그룹핑하여 모델 전달
     * - 로그인 안 되어있으면 회원가입 페이지로 리다이렉트
     */
    @GetMapping("/add")
    public String addForm(Model model,
                          @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {

        log.info("[GET] /building/product/add 진입");

        // 1. 로그인 정보 확인
        MemberVO member = principal.getRealUser();
        if (member == null || member.getMbrCd() == null) {
            log.warn("로그인 정보 없음 → /member/register 리다이렉트");
            return "redirect:/member/register";
        }

        String mbrCd = member.getMbrCd();
        log.info("로그인된 사용자 mbrCd: {}", mbrCd);

        // 2. 시설 옵션 전체 조회
        List<FacilityOptionVO> optionList = service.selectAllFacilityOptions();

        // 3. 그룹핑 (FAC_TYPE_GROUP_CD 기준)
        Map<String, List<FacilityOptionVO>> facilityMap = optionList.stream()
            .collect(Collectors.groupingBy(FacilityOptionVO::getFacTypeGroupCd));

        // 4. 모델에 추가
        model.addAttribute("facilityMap", facilityMap);

        return "building/product/rentalOwnerProductAdd";
    }

   
    @PostMapping("/add")
    public String processAdd(@ModelAttribute("listingVO") ListingVO listingVO,
                             @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {

        MemberVO member = principal.getRealUser();

        if (member == null || member.getMbrCd() == null) {
            return "redirect:/member/register";
        }


        listingVO.setMbrCd(member.getMbrCd());

        if (member.getTenancy() != null) {
            listingVO.setRentalPtyId(member.getTenancy().getRentalPtyId());
        }

        log.info("등록 요청 데이터: {}", listingVO);


        service.insertProduct(listingVO);


        return "redirect:/building/product/list";
    }
}
