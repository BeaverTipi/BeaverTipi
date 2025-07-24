package kr.or.ddit.admin.member.controller;

// import java.time.LocalDate; // 더 이상 여기서 plusDays()를 사용하지 않으므로, 이 import는 필요 없을 수도 있습니다.
// import java.time.format.DateTimeFormatter; // 이 import도 필요 없을 수 있습니다.
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.admin.member.service.ManageMemberService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.renderer.DefaultPaginationRenderer;
import kr.or.ddit.vo.MemberSearchVO;
import kr.or.ddit.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("")
public class ManageMemberListController {

    private ManageMemberService service;

    @Autowired
    public void setService(ManageMemberService service) {
        this.service = service;
    }

    /**
     * 회원 목록 조회 및 페이징/검색 처리
     * @param model 뷰로 데이터를 전달하는 Model 객체
     * @param page 현재 페이지 번호 (기본값 1)
     * @param search MemberSearchVO 객체에 바인딩된 검색 조건
     * @return 뷰 경로
     */
    @GetMapping("/admin/member/list")
    public String listHandler(
            Model model,
            @RequestParam(required = false, defaultValue = "1") int page,
            @ModelAttribute("search") MemberSearchVO search
    ) {
        log.info("회원 목록 조회 요청. 현재 페이지: {}, 검색 조건: {}", page, search);

        PaginationInfo<MemberSearchVO> paging = new PaginationInfo<>();
        paging.setCurrentPageNo(page);
        paging.setDetailSearch(search);

        // 2. 전체 회원 수 조회
        int totalRecord = service.getTotalRecord(paging);
        paging.setTotalRecordCount(totalRecord);

        // 3. 현재 페이지에 해당하는 회원 목록 조회
        List<MemberVO> memberList = service.getMemberList(paging);

        // 4. 페이징 HTML 생성
        String pagingHTML = new DefaultPaginationRenderer().renderPagination(paging, "fnPaging");

        // 5. 모델에 데이터 바인딩
        model.addAttribute("memberList", memberList);
        model.addAttribute("pagingHTML", pagingHTML);
        model.addAttribute("pagingInfo", paging);

        log.info("총 회원 수: {}", totalRecord);

        return "admin/memberManagement/memberList";
    }

    /**
     * ⭐ 추가된 기능: 회원 상세 정보를 조회하는 API (모달용)
     * GET /admin/member/detail/{mbrCd}
     * @param mbrCd 조회할 회원의 코드
     * @return MemberVO 객체를 JSON 형태로 반환
     */
    @GetMapping("/ajax/admin/member/detail/{mbrCd}")
    @ResponseBody // JSON 응답을 위해 @ResponseBody 추가
    public ResponseEntity<MemberVO> getMemberDetail(@PathVariable String mbrCd) {
        log.info("회원 상세 조회 요청. MBR_CD: {}", mbrCd);
        MemberVO member = service.getMemberDetailByMbrCd(mbrCd); // 서비스 메서드 호출

        if (member != null) {
            log.info("회원 상세 정보 조회 성공: {}", member.getMbrId());
            return ResponseEntity.ok(member); // 200
        } else {
            log.warn("회원 상세 정보 없음. MBR_CD: {}", mbrCd);
            return ResponseEntity.notFound().build(); // 404
        }
    }

    /**
     * ⭐ 추가된 기능: 모달에서 단일 회원의 상태를 업데이트하는 API
     * POST /admin/member/updateStatusFromDetail
     * @param mbrCd 회원 코드
     * @param mbrStatusCode 변경할 회원 상태 코드
     * @return "SUCCESS" 또는 "FAIL" 문자열 반환
     */
    @PostMapping("/admin/member/updateStatusFromDetail")
    @ResponseBody // JSON 또는 문자열 응답을 위해 @ResponseBody 추가
    public ResponseEntity<String> updateMemberStatusFromDetail(
            @RequestParam("mbrCd") String mbrCd,
            @RequestParam("mbrStatusCode") String mbrStatusCode) {
        log.info("회원 상태 개별 업데이트 요청. MBR_CD: {}, 새로운 상태: {}", mbrCd, mbrStatusCode);
        try {
            boolean result = service.updateMemberStatus(mbrCd, mbrStatusCode); // 서비스 메서드 호출
            if (result) {
                log.info("회원 {} 상태가 {}로 성공적으로 업데이트되었습니다.", mbrCd, mbrStatusCode);
                return ResponseEntity.ok("SUCCESS"); // 성공 시 "SUCCESS" 반환
            } else {
                log.warn("회원 {} 상태 업데이트 실패. (업데이트된 행 없음)", mbrCd);
                return ResponseEntity.ok("FAIL"); // 실패 시 "FAIL" 반환 (예: 업데이트된 행이 없을 경우)
            }
        } catch (Exception e) {
            log.error("회원 상태 업데이트 중 오류 발생. MBR_CD: {}, 상태: {}", mbrCd, mbrStatusCode, e);
            return ResponseEntity.status(500).body("FAIL"); // 서버 오류 시 500과 "FAIL" 반환
        }
    }
}