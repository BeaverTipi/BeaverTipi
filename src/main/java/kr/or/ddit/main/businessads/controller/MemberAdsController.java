package kr.or.ddit.main.businessads.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart; // 파일 처리를 위해 RequestPart 사용
import org.springframework.web.multipart.MultipartFile; // 파일 처리를 위해 MultipartFile 사용

import jakarta.inject.Inject;
import kr.or.ddit.main.businessads.service.MemberAdsService; // 새로운 서비스 인터페이스 주입
import kr.or.ddit.vo.BoardVO; // BoardVO (광고 게시글 정보)
import kr.or.ddit.vo.AdsClientVO; // AdsClientVO (광고주 정보)
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/member/ads") // 회원용 광고 요청 경로
public class MemberAdsController {

    @Inject
    private MemberAdsService memberAdsService; // 회원용 광고 서비스 주입

    /**
     * 광고 요청 작성 폼 페이지를 반환합니다.
     * @param model Model 객체
     * @return 광고 요청 작성 폼 JSP 경로
     */
    @GetMapping("/requestForm.do")
    public String adsRequestForm(Model model) {
        // 폼 초기화를 위해 빈 VO 객체를 모델에 추가할 수 있습니다.
        // 예를 들어, commandObject 형태로 BoardVO와 AdsClientVO를 미리 설정하여 폼에 바인딩할 수 있습니다.
        model.addAttribute("boardVO", new BoardVO());
        model.addAttribute("adsClientVO", new AdsClientVO());
        log.info("광고 요청 폼 페이지 로드");
        return "main/ads/adsForm"; // 예시 JSP 경로
    }

    /**
     * 회원이 작성한 광고 요청 데이터를 처리하고 저장합니다.
     * @param boardVO 광고 게시글 정보 (제목, 내용 등)
     * @param adsClientVO 광고주 정보 (사업장명, 담당자 등)
     * @param attachFiles 첨부된 파일 목록 (Optional)
     * @param model Model 객체
     * @return 리다이렉트 경로 또는 결과 페이지
     */
    @PostMapping("/request.do")
    public String processAdsRequest(
            @ModelAttribute("boardVO") BoardVO boardVO, // 폼 데이터 바인딩
            @ModelAttribute("adsClientVO") AdsClientVO adsClientVO, // 폼 데이터 바인딩
            @RequestPart(value = "attachFiles", required = false) List<MultipartFile> attachFiles, // 첨부 파일 리스트
            Model model
    ) {
        log.info("광고 요청 데이터 접수: BoardVO={}, AdsClientVO={}", boardVO, adsClientVO);
        if (attachFiles != null && !attachFiles.isEmpty()) {
            log.info("첨부 파일 개수: {}", attachFiles.size());
            // 여기서 파일 처리 로직도 memberAdsService로 위임할 것입니다.
        }

        // 실제 광고 요청 저장 로직은 Service 계층으로 위임합니다.
        // 서비스에서 brdNo 생성 및 AdsClientVO와 FileVO 연결까지 처리할 것입니다.
        boolean success = memberAdsService.createAdsRequest(boardVO, adsClientVO, attachFiles);

        if (success) {
            model.addAttribute("message", "광고 요청이 성공적으로 접수되었습니다.");
            return "redirect:/member/ads/requestSuccess"; // 성공 시 리다이렉트할 페이지 (예시)
        } else {
            model.addAttribute("message", "광고 요청 처리 중 오류가 발생했습니다.");
            // 오류 발생 시 다시 폼 페이지로 돌아가거나 오류 메시지를 표시할 수 있습니다.
            return "main/businessads/adsRequestForm"; // 예시 JSP 경로
        }
    }
}