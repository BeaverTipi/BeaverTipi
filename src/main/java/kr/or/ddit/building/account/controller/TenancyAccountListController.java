/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 9.     			윤현식            최초 생성
 *
 * </pre>
 */
package kr.or.ddit.building.account.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.ddit.building.account.service.TenancyAccountService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.TenancyAccountVO;

/**
 * 
 * @author 
 * @since
 * @see
 *만든이 이학범
 *
 */
@Controller
@RequestMapping("/building/account")
public class TenancyAccountListController {

    @Autowired
    private TenancyAccountService accountService;

    @GetMapping("/list")
    public String accountList(
        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
        Model model
    ) {
        String mbrCd = principal.getRealUser().getMbrCd();

        // 1. 수납계좌 리스트 조회
        List<TenancyAccountVO> accountList = accountService.retrieveAccountList(mbrCd);
        model.addAttribute("accountList", accountList);

        // 2. 연동 건물 리스트 조회
        List<BuildingVO> buildingList = accountService.retrieveBuildingListByMbrCd(mbrCd);
        model.addAttribute("buildingList", buildingList);
        
        String rentalPtyId = accountService.findRentalPtyIdByMbrCd(mbrCd);
        model.addAttribute("rentalPtyId", rentalPtyId);

        return "building/account/accountList";
    }
}
