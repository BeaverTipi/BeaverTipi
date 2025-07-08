package kr.or.ddit.admin.member.controller; // 패키지명 확인

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize; // 권한 관리를 위해 추가
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
import kr.or.ddit.util.page.SimpleSearch;          // SimpleSearch 임포트 (통합 검색이 필요하면 사용)
import kr.or.ddit.util.renderer.DefaultPaginationRenderer; // 페이징 HTML 렌더러 임포트 추가
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.MemberSearchVO;               // MemberSearchVO 임포트 (상세 검색을 위해 사용)
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
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 접근 가능하도록 설정
    public String listHandler(
            Model model,
            @RequestParam(required = false, defaultValue = "1") int page, // 페이지 번호 파라미터
            @ModelAttribute("search") MemberSearchVO search // 상세 검색 조건 파라미터
    ) {
        log.info("회원 목록 조회 요청. 현재 페이지: {}, 검색 조건: {}", page, search);

        // 1. PaginationInfo 객체 생성 및 초기화
        // ⭐ 이 부분의 제네릭 타입을 MemberSearchVO로 변경합니다.
        PaginationInfo<MemberSearchVO> paging = new PaginationInfo<>();
        paging.setCurrentPageNo(page);

        // MemberSearchVO를 detailSearch 필드에 설정
        paging.setDetailSearch(search);

        // MemberSearchVO의 userRoleId 리스트가 비어있지 않으면 count 설정 (XML 조건에서 필요)
        if (search.getUserRoleId() != null && !search.getUserRoleId().isEmpty()) {
            search.setUserRoleCount(search.getUserRoleId().size());
        } else {
            search.setUserRoleCount(0); // 선택된 역할이 없으면 0으로 설정
        }

        // 2. 전체 회원 수 조회
        int totalRecord = service.getTotalRecord(paging); // 서비스의 getTotalRecord 호출
        paging.setTotalRecordCount(totalRecord);       // 전체 레코드 수를 설정하면 페이징 관련 모든 계산 완료

        // 3. 현재 페이지에 해당하는 회원 목록 조회
        List<MemberVO> memberList = service.getMemberList(paging); // 서비스의 getMemberList 호출

        // 4. 페이징 HTML 생성
        // renderPagination 메서드의 두 번째 인자는 JavaScript에서 페이지 이동을 처리할 함수 이름입니다.
        String pagingHTML = new DefaultPaginationRenderer().renderPagination(paging, "fnPaging");

        // 5. 모델에 데이터 바인딩
        model.addAttribute("memberList", memberList);
        model.addAttribute("pagingHTML", pagingHTML);
        model.addAttribute("pagingInfo", paging); // 페이징 정보 객체도 함께 전달 (JS에서 활용 가능)
        // @ModelAttribute("search")로 이미 MemberSearchVO가 model에 추가되었음

        log.info("총 회원 수: {}", totalRecord);

        return "admin/memberManagement/memberList";
    }

    /**
     * 회원 상태 업데이트를 위한 POST 엔드포인트
     * @param membersToUpdate 업데이트할 회원 정보 목록 (JSON 배열 형태)
     * @return 성공/실패 메시지
     */
    @PostMapping("/member/updateStatus")
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 접근 가능하도록 설정
    @ResponseBody // 이 메서드의 반환 값이 HTTP 응답 본문에 직접 쓰여질 것임을 나타냅니다.
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