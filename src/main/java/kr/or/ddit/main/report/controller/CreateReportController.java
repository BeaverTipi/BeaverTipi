package kr.or.ddit.main.report.controller;

import java.util.HashMap;
import java.util.List; // List 임포트 추가
import java.util.Map;

import org.springframework.security.core.Authentication; // Authentication 임포트
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import kr.or.ddit.main.report.service.CreateReportService;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.ReportVO;
import kr.or.ddit.util.security.auth.RealUserWrapper; // RealUserWrapper 임포트 추가

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/main/report")
public class CreateReportController {

    @Inject
    private CreateReportService createReportService;

    
    /**
     * 신고 작성 폼 페이지를 반환합니다.
     * @param targetId 신고 대상의 ID
     * @param type 신고 유형
     * @param model Model 객체
     * @return 신고 작성 폼 JSP 경로
     */
    @GetMapping("/createForm")
    public String createReportForm(
            @RequestParam(value = "targetId", required = false) String targetId,
            @RequestParam(value = "type", required = false) String type,
            Model model
    ) {
        log.info("신고 작성 폼 요청. targetId: {}, type: {}", targetId, type);

        ReportVO reportVO = new ReportVO();
        if (targetId != null && !targetId.isEmpty()) {
            reportVO.setRptTargetId(targetId);
        }
        if (type != null && !type.isEmpty()) {
            reportVO.setRptCode(type);
        }

        model.addAttribute("reportVO", reportVO); // ReportVO를 modelAttribute로 사용
        return "main/report/createReport";
    }

    /**
     * 신고 작성 폼 제출을 처리하고, 새로운 신고를 생성합니다.
     * 성공/실패 여부를 JSON으로 반환하여 클라이언트에서 alert 창을 띄웁니다.
     * @param reportVO 신고 게시글 및 신고 상세 정보를 담은 ReportVO 객체
     * @param attachFiles 첨부된 파일 목록 (광고 요청 컨트롤러와 동일하게 List 사용)
     * @param auth 인증 객체 (MemberAdsController와 동일)
     * @param principal 사용자 상세 정보 (MemberAdsController와 동일)
     * @return Map<String, String> 형태의 JSON 응답 (status, message)
     */
    @PostMapping("/create")
    @ResponseBody
    public Map<String, String> createReport(
            @ModelAttribute @Valid ReportVO reportVO, // ReportVO가 BoardVO를 상속하므로 그대로 사용
            BindingResult bindingResult,
            @RequestPart(value = "attachFiles", required = false) List<MultipartFile> attachFiles, // List<MultipartFile>로 변경
            
            Authentication auth, // Authentication 추가
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal // RealUserWrapper 추가
    ) {
        log.info("신고 생성 요청 - ReportVO: {}", reportVO);
        log.info("첨부 파일 수: {}", attachFiles != null ? attachFiles.size() : 0);

        Map<String, String> response = new HashMap<>();

        // 1. 로그인 사용자 확인 및 작성자 정보 설정 (MemberAdsController 방식 참조)
        if (principal != null) {
            MemberVO member = principal.getRealUser();
            if (member != null && member.getMbrCd() != null) {
                reportVO.setMbrCd(member.getMbrCd()); // 신고자 회원 코드 설정
                log.info("로그인 사용자 MBR_CD: {}", member.getMbrCd());
            } else {
                log.warn("로그인 사용자 MBR_CD를 가져올 수 없거나 member 객체가 null입니다.");
                response.put("status", "error");
                response.put("message", "로그인 정보가 올바르지 않습니다. 다시 로그인 해주세요.");
                return response;
            }
        } else {
            log.warn("로그인하지 않은 사용자의 신고 요청 시도.");
            response.put("status", "error");
            response.put("message", "로그인이 필요합니다.");
            return response;
        }

        reportVO.setBrdCode("RPT"); // 게시글 구분 코드를 '신고'로 명시

        // 2. 서비스 호출하여 신고 생성 및 파일 처리
        try {
            // Service 메소드에 ReportVO와 List<MultipartFile>을 함께 전달
            boolean success = createReportService.createReport(reportVO, attachFiles); // 반환 타입 boolean으로 변경
            if (success) {
                response.put("status", "success");
                response.put("message", "신고가 성공적으로 접수되었습니다.");
            } else {
                response.put("status", "error");
                response.put("message", "신고 접수에 실패했습니다.");
            }
        } catch (Exception e) {
            log.error("신고 생성 중 오류 발생", e);
            response.put("status", "error");
            response.put("message", "신고 접수 중 서버 오류가 발생했습니다: " + e.getMessage());
        }
        return response;
    }
}