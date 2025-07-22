package kr.or.ddit.broker.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerNameCardService;

import kr.or.ddit.vo.FileVO;
import kr.or.ddit.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/rest/broker/namecard")
public class RestBrokerNameCardController {

    @Autowired
    private BrokerNameCardService nameCardService;

    @Autowired
    BrokerAuthUnpackingService authService;

    @GetMapping("/user")
    public Map<String, Object> getBrokerInfo(Principal principal) {
        log.info(">>> [Controller 진입] /user API called");
        Map<String, Object> result = new HashMap<>();
        log.info(">>> principal: {}", principal);

        // Principal을 통한 로그인 정보 추출
        MemberVO memberVO = authService.getRealUser(principal);
        if (memberVO == null) {
            result.put("result", "fail");
            result.put("message", "로그인 정보 없음");
            return result;
        }
        String mbrCd = memberVO.getMbrCd();
        result.put("result", "success");
        result.put("mbrCd", mbrCd);
        return result;
    }

    // 1. 회원별 명함 리스트 조회
    @GetMapping("/list/{mbrCd}")
    public List<FileVO> getNameCardList(@PathVariable("mbrCd") String mbrCd) {
        return nameCardService.selectNameCardListByMember(mbrCd);
    }

    // 2. 명함 이미지 저장
    @PostMapping("/save")
    public Map<String, Object> saveNameCard(
            @RequestPart("file") MultipartFile file,
            @RequestParam("sourceRef") String sourceRef,
            @RequestParam("sourceId") String sourceId,
            @RequestParam("docTypeCd") String docTypeCd
    ) {
        Map<String, Object> result = new HashMap<>();
        try {
        	log.info(docTypeCd);
            FileVO savedFile = nameCardService.uploadAndSave(file, "public/broker/namecard", sourceRef, sourceId, docTypeCd);
            result.put("result", "success");
            result.put("file", savedFile);
        } catch (Exception e) {
            result.put("result", "fail");
            result.put("message", e.getMessage());
        }
        return result;
    }

    // 3. 명함 이미지 삭제
    @DeleteMapping("/delete")
    public Map<String, Object> deleteNameCard(@RequestParam String fileId, @RequestParam Integer fileAttachSeq) {
        Map<String, Object> param = new HashMap<>();
        param.put("fileId", fileId);
        param.put("fileAttachSeq", fileAttachSeq);
        int cnt = nameCardService.deleteFileById(param);
        Map<String, Object> result = new HashMap<>();
        result.put("result", cnt > 0 ? "success" : "fail");
        return result;
    }

    // 4. 명함 상세 조회
    @GetMapping("/detail")
    public FileVO getNameCardDetail(@RequestParam String fileId, @RequestParam Integer fileAttachSeq) {
        Map<String, Object> param = new HashMap<>();
        param.put("fileId", fileId);
        param.put("fileAttachSeq", fileAttachSeq);
        return nameCardService.selectNameCardDetail(param);
    }

    // 대표명함 설정 API
    @PostMapping("/set-main")
    public Map<String, Object> setMainNameCard(
            @RequestParam("nameCardId") String nameCardId,
            Principal principal
    ) {
        Map<String, Object> result = new HashMap<>();
        MemberVO memberVO = authService.getRealUser(principal);
        if (memberVO == null) {
            result.put("result", "fail");
            result.put("message", "로그인 정보가 없습니다.");
            return result;
        }
        String mbrCd = memberVO.getMbrCd();
        log.info("[setMainNameCard] nameCardId={}, mbrCd={}", nameCardId, mbrCd);
        nameCardService.setMainNameCard(mbrCd, nameCardId);
        result.put("result", "success");
        return result;
    }
}
