package kr.or.ddit.building.unitManaged.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import kr.or.ddit.building.unitManaged.service.UnitManagedService;
import kr.or.ddit.vo.UnitVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/building/unitManaged")
public class UnitManagedAddController {

    @Autowired
    private UnitManagedService unitService;

    /**
     * 📌 호실 등록 폼 진입 (GET)
     * - 건물 ID와 임대사업자 ID를 파라미터로 받아 폼으로 전달
     */
    @GetMapping("/add")
    public String showUnitAddForm(@RequestParam String bldgId,
                                   @RequestParam String rentalPtyId,
                                   Model model) {
    	 log.info(">>> bldgId in GET: {}", bldgId); 
    	
        model.addAttribute("bldgId", bldgId);
        model.addAttribute("rentalPtyId", rentalPtyId);
        return "building/unit/unitDetailAdd";
    }

    /**
     * 📌 다건 등록 처리 (POST)
     * - JSP에서 입력한 각 호실 정보를 파라미터로 받아서 UnitVO 리스트 구성 후 저장
     */
    @PostMapping("/add")
    public String insertUnits(HttpServletRequest request) {
    	log.info("POST요청이 들어온거야 이게보이면");
        String bldgId = request.getParameter("bldgId");
        log.info("이게 왜 안나올까 bldgId: {}", bldgId);
        String rentalPtyId = request.getParameter("rentalPtyId");

        // 배열 형태로 넘어온 값들 추출
        String[] unitFlrNo = request.getParameterValues("unitList[].unitFlrNo");
        String[] unitCmar = request.getParameterValues("unitList[].unitCmar");
        String[] unitXuar = request.getParameterValues("unitList[].unitXuar");
        String[] unitDsrMnthRentAmt = request.getParameterValues("unitList[].unitDsrMnthRentAmt");
        String[] unitDsrSaleAmt = request.getParameterValues("unitList[].unitDsrSaleAmt");
        String[] unitDpstAmt = request.getParameterValues("unitList[].unitDpstAmt");
        String[] unitDtlDescCn = request.getParameterValues("unitList[].unitDtlDescCn");
        String[] unitRoom = request.getParameterValues("unitList[].unitRoom");
        String[] unitStatCd = request.getParameterValues("unitList[].unitStatCd");

        List<UnitVO> unitList = new ArrayList<>();

        // 각 유닛 데이터를 반복문으로 바인딩
        for (int i = 0; i < unitFlrNo.length; i++) {
            UnitVO unit = new UnitVO();
            unit.setBldgId(bldgId);
            unit.setRentalPtyId(rentalPtyId);
            unit.setUnitFlrNo(parseInt(unitFlrNo[i]));
            unit.setUnitCmar(parseBigDecimal(unitCmar[i]));
            unit.setUnitXuar(parseBigDecimal(unitXuar[i]));
            unit.setUnitDsrMnthRentAmt(parseBigDecimal(unitDsrMnthRentAmt[i]));
            unit.setUnitDsrSaleAmt(parseBigDecimal(unitDsrSaleAmt[i]));
            unit.setUnitDpstAmt(parseBigDecimal(unitDpstAmt[i]));
            unit.setUnitDtlDescCn(unitDtlDescCn[i]);
            unit.setUnitRoom(unitRoom[i]);
            unit.setUnitStatCd(unitStatCd[i]);

            unitList.add(unit);
        }

        int inserted = unitService.insertUnitList(unitList);
        log.info("총 등록된 유닛 수: {}", inserted);

        return "redirect:/building/managed/list";
    }

    // 숫자 변환 예외처리
    private Integer parseInt(String val) {
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
            return null;
        }
    }

    // BigDecimal 변환 예외처리
    private BigDecimal parseBigDecimal(String val) {
        try {
            return new BigDecimal(val);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
