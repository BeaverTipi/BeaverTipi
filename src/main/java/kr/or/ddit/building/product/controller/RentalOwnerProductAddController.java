package kr.or.ddit.building.product.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import kr.or.ddit.building.product.service.RentalOwnerProductService;
import kr.or.ddit.vo.FacilityOptionVO;
import kr.or.ddit.vo.ListingVO;

@Controller
@RequestMapping("/building/product")
public class RentalOwnerProductAddController {

    @Autowired
    private RentalOwnerProductService service;

    /**
     * 매물 등록 폼 진입
     * - 시설 옵션 데이터를 facilityMap 으로 가공하여 모델에 담아 전달
     * - JSP에서 그룹코드별로 시설 옵션 반복 출력할 수 있도록 함
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        // 1. 시설 옵션 전체 조회
        List<FacilityOptionVO> optionList = service.selectAllFacilityOptions();

        // 2. FAC_TYPE_GROUP_CD 기준으로 Map<String, List<FacilityOptionVO>> 으로 그룹핑
        Map<String, List<FacilityOptionVO>> facilityMap = optionList.stream()
            .collect(Collectors.groupingBy(FacilityOptionVO::getFacTypeGroupCd));

        // 3. 모델에 facilityMap 담기
        model.addAttribute("facilityMap", facilityMap);

        // 4. JSP 반환
        return "building/product/rentalOwnerProductAdd";
    }

    /**
     * 매물 등록 처리
     * - 세션에서 mbrCd와 rentalPtyId를 꺼내 ListingVO에 주입
     * - 서비스 호출하여 등록 처리
     */
    @PostMapping("/add")
    public String addSubmit(@ModelAttribute("listing") ListingVO listing, HttpSession session) {

        // 1. 세션에서 사용자 정보 꺼냄
        String mbrCd = (String) session.getAttribute("mbrCd");
        String rentalPtyId = (String) session.getAttribute("rentalPtyId");

        // 2. VO에 주입
        listing.setMbrCd(mbrCd);
        listing.setRentalPtyId(rentalPtyId);

        // 3. 서비스 호출 → DB 등록
        int result = service.insertProduct(listing);

        // 4. 결과에 따라 리다이렉트 처리
        return result > 0
            ? "redirect:/building/product/list"
            : "redirect:/building/product/add?error";
    }
}
