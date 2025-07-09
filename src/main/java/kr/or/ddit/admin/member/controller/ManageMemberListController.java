package kr.or.ddit.admin.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.admin.member.service.ManageMemberService;
import kr.or.ddit.util.page.PaginationInfo;
// import kr.or.ddit.util.page.SimpleSearch; // 사용하지 않으면 제거해도 됩니다.
import kr.or.ddit.util.renderer.DefaultPaginationRenderer;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.MemberSearchVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin")
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
    @GetMapping("/member/list")
    @PreAuthorize("hasRole('ADMIN')")
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
     * 회원 상태 업데이트를 위한 POST 엔드포인트
     * @param membersToUpdate 업데이트할 회원 정보 목록 (JSON 배열 형태)
     * @return 성공/실패 메시지
     */
    @PostMapping("/member/updateStatus")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public String updateMemberStatus(@RequestBody List<MemberVO> membersToUpdate) {
        log.info("회원 상태 업데이트 요청 수신. 업데이트 대상 회원 수: {}", membersToUpdate.size());
        try {
            int successCount = 0;
            for (MemberVO member : membersToUpdate) {
                service.updateMemberStatus(member.getMbrCd(), member.getMbrStatusCode());
                successCount++;
            }
            return successCount + "명의 회원 상태가 성공적으로 업데이트되었습니다.";
        } catch (Exception e) {
            log.error("회원 상태 업데이트 중 오류 발생", e);
            return "회원 상태 업데이트 중 오류 발생: " + e.getMessage();
        }
    }
}