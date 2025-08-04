package kr.or.ddit.broker.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerIntroCardService;
import kr.or.ddit.vo.FileVO;
import kr.or.ddit.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/rest/broker/introcard")
public class RestBrokerIntroCardController {

    @Autowired
    private BrokerIntroCardService introCardService;

    @Autowired
    private BrokerAuthUnpackingService authService;

    // 1. 소개카드 단건 조회 (최신)
    @GetMapping("/user")
    public Map<String, Object> getIntroCardByUser(Principal principal) {
        Map<String, Object> result = new HashMap<>();
        MemberVO memberVO = authService.getRealUser(principal);
        if (memberVO == null) {
            result.put("result", "fail");
            result.put("message", "로그인 정보 없음");
            return result;
        }
        String mbrCd = memberVO.getMbrCd();
        Map<String, Object> param = new HashMap<>();
        param.put("mbrCd", mbrCd);
        param.put("docTypeCd", "DESC_" + mbrCd);
        FileVO fileVO = introCardService.selectIntroCardByMember(param);
        result.put("result", "success");
        result.put("introCard", fileVO);
        return result;
    }

    // 2. 소개카드 이미지 저장 (기존 파일 있으면 교체, 없으면 새로 저장)
    @PostMapping("/save")
    public Map<String, Object> saveIntroCard(
            Principal principal,
            @RequestPart("file") MultipartFile file,
            @RequestParam("sourceRef") String sourceRef
            // 더 이상 sourceId, docTypeCd 안 받아도 됨!
    ) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 👇 여기서 로그인 정보로부터 mbrCd 추출!
            MemberVO memberVO = authService.getRealUser(principal);
            if (memberVO == null) {
                result.put("result", "fail");
                result.put("message", "로그인 정보 없음");
                return result;
            }
            String mbrCd = memberVO.getMbrCd();
            String docTypeCd = "DESC_" + mbrCd;

            Map<String, Object> param = new HashMap<>();
            param.put("mbrCd", mbrCd);
            param.put("docTypeCd", docTypeCd);

            FileVO oldFile = introCardService.selectIntroCardByMember(param);
            log.info("컨트롤러 진입: mbrCd={}, docTypeCd={}", mbrCd, docTypeCd);

            if (oldFile != null && oldFile.getFileId() != null) {
                introCardService.replaceAndSaveIntroCard(oldFile.getFileId(), file);
                FileVO saved = introCardService.selectIntroCardByMember(param);
                result.put("result", "success");
                result.put("file", saved);
            } else {
                FileVO saved = introCardService.uploadAndSaveIntroCard(
                    file, "public/broker/introcard", sourceRef, mbrCd, docTypeCd
                );
                result.put("result", "success");
                result.put("file", saved);
            }
        } catch (Exception e) {
            log.error("인트로카드 저장 실패", e);
            result.put("result", "fail");
            result.put("message", e.getMessage());
        }
        return result;
    }


    // 3. 소개카드 이미지 삭제
    @DeleteMapping("/delete")
    public Map<String, Object> deleteIntroCard(@RequestParam String fileId, @RequestParam Integer fileAttachSeq) {
        Map<String, Object> param = new HashMap<>();
        param.put("fileId", fileId);
        param.put("fileAttachSeq", fileAttachSeq);
        log.info("[삭제 요청] 인트로카드 fileId={}, fileAttachSeq={}", fileId, fileAttachSeq);
        int cnt = introCardService.deleteIntroCardFile(param);
        Map<String, Object> result = new HashMap<>();
        result.put("result", cnt > 0 ? "success" : "fail");
        return result;
    }
}
