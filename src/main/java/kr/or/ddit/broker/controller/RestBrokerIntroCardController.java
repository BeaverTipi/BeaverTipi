/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 23.     			윤현식            최초 생성
 *
 * </pre>
 */
package kr.or.ddit.broker.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerIntroCardService;
import kr.or.ddit.vo.FileVO;
import kr.or.ddit.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author 
 * @since
 * @see
 *이학범 ㅋ
 *
 */
@Slf4j
@RestController
@RequestMapping("/rest/broker/introcard")
public class RestBrokerIntroCardController {
	 @Autowired
	    private BrokerIntroCardService introCardService;

	    @Autowired
	    private BrokerAuthUnpackingService authService;

	    // 1. 소개카드 단건 조회(가장 최근)
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
	        String docTypeCd = "DESC_" + mbrCd;
	        FileVO fileVO = introCardService.selectIntroCardByMember(mbrCd, docTypeCd);
	        result.put("result", "success");
	        result.put("introCard", fileVO);
	        return result;
	    }

	    // 2. 소개카드 이미지 저장
	    @PostMapping("/save")
	    public Map<String, Object> saveIntroCard(
	            @RequestPart("file") MultipartFile file,
	            @RequestParam("sourceRef") String sourceRef,
	            @RequestParam("sourceId") String sourceId,
	            @RequestParam("docTypeCd") String docTypeCd
	    ) {
	        Map<String, Object> result = new HashMap<>();
	        try {
	            FileVO savedFile = introCardService.uploadAndSaveIntroCard(
	                    file,
	                    "public/broker/introcard",
	                    sourceRef,
	                    sourceId,
	                    docTypeCd
	            );
	            result.put("result", "success");
	            result.put("file", savedFile);
	        } catch (Exception e) {
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
	        int cnt = introCardService.deleteIntroCardFile(param);
	        Map<String, Object> result = new HashMap<>();
	        result.put("result", cnt > 0 ? "success" : "fail");
	        return result;
	    }
	}
