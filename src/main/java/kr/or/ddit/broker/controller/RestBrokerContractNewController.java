/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7.  9.     		김찬영            최초 생성
 * 2025. 7. 10.     		김찬영            수정.
 * 2025. 7. 11.     		김찬영            패키지 고침.
 *
 * </pre>
 */
package kr.or.ddit.broker.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerContractService;
import kr.or.ddit.util.crypto.AES256Util;
import kr.or.ddit.util.file.Base64DecodedMultipartFile;
import kr.or.ddit.util.file.FileToMultipartFileUtil;
import kr.or.ddit.util.file.service.FileService;
import kr.or.ddit.util.pdf.service.PDFService;
import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.ContractVO;
import kr.or.ddit.vo.FileVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.ListingWishlistVO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author developer_KCY
 */
@Slf4j
@RestController
@RequestMapping("/rest/broker/myoffice/cont/new")
public class RestBrokerContractNewController {
	
	@Autowired
	BrokerAuthUnpackingService authService;
	@Autowired
	BrokerContractService contService;
	@Autowired
	AES256Util aes256Util;
	@Autowired
	PDFService pdfService;
	@Autowired
	FileService fileService;
	@Autowired
	BrokerAuthUnpackingService authUnpack;
	
	@GetMapping("/listing")
	public List<ListingVO> lstgListForContract(Principal principal) {
		BrokerVO broker = authService.getRealUser(principal);
		log.error("{}", broker);
		List<ListingVO> lstgList = contService.readLstgListForContract(broker.getMbrCd());
		return lstgList;
	}
	
	@PostMapping("/lessee")
	public List<ListingWishlistVO> lesseeForContract(
		@RequestBody Map<String, String> requestBody
	) {
		String lstgId = requestBody.get("lstgId");
		log.info("--------------> {}", lstgId);
		List<ListingWishlistVO> wishlist = contService.readLesseeVolunteerList(lstgId);
		
		return wishlist;
	}
	
	@PostMapping("/submit")
	public ResponseEntity<?> handleContractSubmit(
			Principal principal
			, @RequestBody Map<String, Object> payload
	) {
		
	    try {
	        List<Map<String, String>> base64Files = (List<Map<String, String>>) payload.get("base64Files");
	        Map<String, Object> contractInfo = (Map<String, Object>) payload.get("contract");
	        ContractVO.builder()
	        		  .mbrCd(String.valueOf(contractInfo.get("lesseeCd")))
	        		  .mbrCdBrok(authUnpack.getMbrCd(principal.getName()))
	        		  .lstgId(String.valueOf(contractInfo.get("listingId")))
	        		  .contTypeCode(String.valueOf(contractInfo.get("listingContTypeCode1")))
	        		  .contDeposit(Long.parseLong((String)contractInfo.get("")))
	        		  .build();
	        List<MultipartFile> multipartFiles = new ArrayList<>();

	        for (Map<String, String> fileMap : base64Files) {
	            String fileName = fileMap.get("name");
	            String content = fileMap.get("content");

	            String savedFileName = "";
	            // MIME 헤더 제거
	            String base64 = content.contains(",") ? content.split(",")[1] : content;

	            byte[] data = Base64.getDecoder().decode(base64);

	            MultipartFile multipartFile = new Base64DecodedMultipartFile(
	            	data,
	            	savedFileName,
	                fileName,
	                Files.probeContentType(Paths.get(fileName))
	            );
	            multipartFiles.add(multipartFile);
	        }

	        File merged = pdfService.mergeToSinglePdf(multipartFiles);
	        MultipartFile multipartMerged = FileToMultipartFileUtil.convert(merged);
	        FileVO result = fileService.uploadAndSave(multipartMerged, "contract", "CONTR", "contractId", "CONTRACT_DOC");
	        
	     // 병합된 파일을 디버깅용 디렉토리로 복사
//	        File debugCopy = new File("D:/eGovFrameDev-4.3.1-64bit/workspace-egov/BeaverTipi-React/public/merged-" + System.currentTimeMillis() + ".pdf");
	        File debugCopy = new File("D:/debug/merged-" + System.currentTimeMillis() + ".pdf");
	        Files.copy(merged.toPath(), debugCopy.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

	        
	        return ResponseEntity.ok(Map.of(
	            "success", true,
	            "mergedPath", merged.getAbsolutePath(),
	            "debugPath", debugCopy.getAbsolutePath()
	        ));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(Map.of("success", false, "error", e.getMessage()));
	    }
	}

}
