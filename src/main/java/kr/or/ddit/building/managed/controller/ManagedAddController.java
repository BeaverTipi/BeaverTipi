package kr.or.ddit.building.managed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // 추가
import kr.or.ddit.building.managed.service.BuildingManagedService;
import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.util.file.service.FileService; // 추가
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID; // 추가

@Slf4j
@Controller
@RequestMapping("/building/managed")
public class ManagedAddController {

    @Autowired
    private BuildingManagedService buildingService;

    @Autowired
    private CommonCodeService commonCodeService;

    @Autowired
    private FileService fileService; // 파일업로드 서비스 추가

    // 건물 등록 폼 (GET)
    @GetMapping("/add")
    public String addBuildingForm(Model model, @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
        MemberVO memberVO = principal.getRealUser();
        String rentalPtyId = memberVO.getTenancy().getRentalPtyId();

        BuildingVO buildingVO = new BuildingVO();
        log.info("삘딩쁘이오!! : {}", buildingVO);
        buildingVO.setRentalPtyId(rentalPtyId);
        buildingVO.setDelYn("N");

        // 계좌 리스트
        List<TenancyAccountVO> accList = buildingService.selectAccountsByRentalPtyId(rentalPtyId);
        buildingVO.setAccList(accList);
        if (!accList.isEmpty()) {
            buildingVO.setAccNum(accList.get(0).getAccNum());
        }

        // 내 매물 리스트
        List<ListingVO> listingList = buildingService.selectListingsByRentalPtyId(rentalPtyId);

        // 건물 유형 코드
        List<CommonCodeVO> bldgTypeList = commonCodeService.readCommonCodeList("BLDG");

        model.addAttribute("buildingVO", buildingVO);
        model.addAttribute("listingList", listingList);
        model.addAttribute("bldgTypeList", bldgTypeList);

        return "building/managed/managedAdd";
    }

    // 건물 등록 처리 (POST)
    @PostMapping("/add")
    public String addBuilding(
        @ModelAttribute("buildingVO") BuildingVO buildingVO,
        @RequestParam(value = "bldgImgFile", required = false) MultipartFile bldgImgFile // 추가!
    ) {
        if (buildingVO.getBldgFlrCnt() == null) buildingVO.setBldgFlrCnt(1);
        if (buildingVO.getBldgUnitCnt() == null) buildingVO.setBldgUnitCnt(1);

        buildingVO.setDelYn("N");
        // 1. 건물 등록 (bldgId 생성)
        buildingService.insertBuilding(buildingVO);

        // 2. 이미지 업로드 (첨부한 경우에만)
        if (bldgImgFile != null && !bldgImgFile.isEmpty()) {
            String dir = "building/" + buildingVO.getBldgId();
            String changedName = UUID.randomUUID() + "_" + bldgImgFile.getOriginalFilename();

            // FileService 이용 S3에 업로드 & DB 저장
            FileVO fileVO = fileService.uploadAndSave(
                bldgImgFile,
                dir, // building/{bldgId}
                "BUILDING", // 출처
                buildingVO.getBldgId(), // sourceId = bldgId
                "IMG" // 문서유형
            );

            // 건물 테이블에 이미지 경로 업데이트
            buildingService.updateBuildingImagePath(buildingVO.getBldgId(), fileVO.getFilePathUrl());
        }

        // 등록 후 상세/유닛 입력화면으로 이동
        return "redirect:/building/unitManaged/add?bldgId=" + buildingVO.getBldgId()
                + "&rentalPtyId=" + buildingVO.getRentalPtyId();
    }

    @GetMapping("/listing/detail")
    @ResponseBody
    public ListingVO getListingDetail(@RequestParam String lstgId) {
        return buildingService.selectListingById(lstgId);
    }
}
