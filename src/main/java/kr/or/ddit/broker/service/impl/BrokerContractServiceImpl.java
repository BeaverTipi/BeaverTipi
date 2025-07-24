/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 9.     			김찬영          최초 생성
 * 2025. 7. 11.     		김찬영          패키지 고침.
 * 2025. 7. 17.				김찬영			계약등록 프로세스 완료^0^
 *
 * </pre>
 */
package kr.or.ddit.broker.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import kr.or.ddit.broker.mapper.BrokerMapper;
import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerContractService;
import kr.or.ddit.util.crypto.AES256Util;
import kr.or.ddit.util.file.Base64DecodedMultipartFile;
import kr.or.ddit.util.file.FileToMultipartFileUtil;
import kr.or.ddit.util.file.service.FileService;
import kr.or.ddit.util.parse.SafeParse;
import kr.or.ddit.util.pdf.service.PDFService;
import kr.or.ddit.vo.ContractVO;
import kr.or.ddit.vo.FileVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.ListingWishlistVO;
import kr.or.ddit.vo.StandardLeaseFormDTO;
import kr.or.ddit.vo.TenancyVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author developer_KCY
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BrokerContractServiceImpl implements BrokerContractService {

	private final BrokerMapper mapper;

	@Autowired
	private PDFService pdfService;
	@Autowired
	private BrokerAuthUnpackingService authUnpack;
	@Autowired
	private FileService fileService;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private Validator validator;
	@Autowired
	private AES256Util aes256Util;
	
	/**
	 * @param principal 내에서 불러온 Broker의 mbrCd
	 * @return Broker가 가진 매물(LSTG)의 리스트
	 */
	@Override
	public List<ListingVO> readLstgListForContract(String mbrCd) {
		List<ListingVO> lstgList = mapper.selectLstgListForContract(mbrCd);
		return lstgList;
	}

	/**
	 * @param partyInfoParams :Map.of("lstgId",lstgId,"lesseeCd",lesseeCd);
	 * @return 중개인, 임대인, 임차인 세 명에 대한 정보를 담은 Map
	 */
	@Override
	public Map<String, Object> readContractPartyInfo(Map<String, String> partyInfoParams) {
		Map<String, Object> contractPartyInfo = null;
		return contractPartyInfo;
	}

	/**
	 *
	 */
	@Override
	public List<ListingWishlistVO> readLesseeVolunteerList(String lstgId) {
		List<ListingWishlistVO> lesseeVolunteerList = null;
		lesseeVolunteerList = mapper.selectWishlistForLessee(lstgId);
		return lesseeVolunteerList;
	}

	@Override
	public List<TenancyVO> readTenancyList(String rentalPtyId) {
		List<TenancyVO> tenancyList = null;
		tenancyList = mapper.selectTenancyInfo(rentalPtyId);
		return tenancyList;
	};

	/**
	 * @param principal 내에서 불러온 Broker의 mbrCd
	 * @return Broker가 가진 계약(CONTRACT)의 리스트
	 */
	@Override
	public List<ContractVO> readContractList(String mbrCd) {
		List<ContractVO> contractList = null;

		return contractList;
	}

	/**
	 * @return 계약ID를 돌려줘서 그걸로 계약파일 이름을 짓는 게 낫지 않나
	 */
	@Override
	public String createProceedingContract(ContractVO contract) {
		int rec = mapper.insertProceedingContract(contract);
		if (rec == 0)
			return "failed";
		return contract.getContId();
	};

	/**
	 * @param contId: 방금 계약 등록된 매물의 상태 비활성화로 변경
	 * @return
	 */
	public String modifyListingProdStat(String contId) {
		int rec = mapper.updateListingProdStat(contId);
		if (rec == 1)
			return "SUCCESS";
		else
			return "FAILED";
	}

	@Override
	public List<ContractVO> readProceedingContractsList(String mbrCd) {
		List<ContractVO> proceedingContractsList = null;
		proceedingContractsList = mapper.selectProceedingContractsList(mbrCd);
		return proceedingContractsList;
	}

	@Transactional
	@Override
	public ResponseEntity<?> processOfCreatingContract(String decryptedJson, Principal principal) throws JsonProcessingException {
		// TODO 컨트롤러 다이어트 들어가야지...
		try {
			/** 2. JSON -> POJO 매핑 */
			Map<String, Object> parsedRequest = objectMapper.readValue(decryptedJson, new TypeReference<>() {
			});
			StandardLeaseFormDTO contractInfo = objectMapper.convertValue(parsedRequest.get("contractInfo"),
					StandardLeaseFormDTO.class);
			Set<ConstraintViolation<StandardLeaseFormDTO>> violations = validator.validate(contractInfo);
	
			if (!violations.isEmpty()) {
			    StringBuilder errorMsg = new StringBuilder("검증 실패 항목:\n");
			    for (ConstraintViolation<StandardLeaseFormDTO> violation : violations) {
			        errorMsg.append("- ").append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("\n");
			    }
			    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg.toString());
			}
			
			List<Map<String, String>> base64Files = objectMapper.convertValue(parsedRequest.get("base64Files"),
					new TypeReference<List<Map<String, String>>>() {
					});
			List<FileVO> files = objectMapper.convertValue(parsedRequest.get("files"),
					new TypeReference<List<FileVO>>() {
					});
			log.info(">ㅂ< contractInfo {}", contractInfo);
			log.info(">ㅂ< files {}", files);

			/** 3. 계약서류 통합PDF 만들기 */
			List<MultipartFile> multipartFiles = new ArrayList<>();
			for (Map<String, String> fileMap : base64Files) {
				String fileName = fileMap.get("name");
				String content = fileMap.get("content");
				String savedFileName = "";

				// Base64 문자열에서 MIME 헤더(data URI scheme) 제거 후
				// -> BINARY 데이터로 디코딩
				// -> MultipartFile 객체로 감싸 파일화.
				String base64 = content.contains(",") ? content.split(",")[1] : content;
				byte[] data = Base64.getDecoder().decode(base64);
				MultipartFile multipartFile = new Base64DecodedMultipartFile(data, savedFileName, fileName,
						Files.probeContentType(Paths.get(fileName)));
				multipartFiles.add(multipartFile);
			}
			File merged = pdfService.mergeToSinglePdf(multipartFiles);
			MultipartFile multipartMerged = FileToMultipartFileUtil.convert(merged);

			/** 4. DB에 계약정보 레코드 입력 */
			ContractVO contract = ContractVO.builder()
					.mbrCdBrok(authUnpack.getMbrCd(principal.getName()))
					.lstgId(contractInfo.getListingId())
					.contTypeCode(contractInfo.getListingTypeSale())
					.contDeposit(/* deposit */
						SafeParse.safeParseLong(
								Optional.ofNullable(contractInfo.getListingTypeCode1())
								.map(type -> {
									// 전세 계약일 경우
									if ("001".equals(type)) return contractInfo.getListingLease(); // 전세금 (String)
									// 월세 계약일 경우
									if ("002".equals(type)) return contractInfo.getListingLeaseAmt(); // 보증금 (String)
									// 매칭되는 타입이 없으면 null 반환
									return null;
								}).orElse("0") // null이면 "0"으로 대체
						))
					.contTaxAmount(null)
					.contAmount(SafeParse.safeParseLong(contractInfo.getListingLeaseM()))
					.contStatCd("001")
					.contDtm(null)
					.contTypeGroupCd(null)
					.contStatGroupCd(null)
					.contLesseeTelno(contractInfo.getLesseeTelno())
					.contTenancyTelno(contractInfo.getLessorTelno())
					.contBrokerTelno(contractInfo.getAgentTelno())
					.build();
			log.debug("(ಥ﹏ಥ) {}", contractInfo.getListingTypeCode1());
			log.debug("(ಥ﹏ಥ) {}", contractInfo.getListingLease());
			log.debug("(ಥ﹏ಥ) {}", contractInfo.getListingLeaseAmt());
			log.debug("(ಥ﹏ಥ) {}", contractInfo.getListingLeaseM());
			log.debug("(ಥ﹏ಥ) {}", contract);
			int contRec = mapper.insertProceedingContract(contract);

			/** 5. S3 파일서버에 통합PDF 업로드 및 DB에 파일정보 레코드 입력 */
			String contId = contract.getContId();
			FileVO result = fileService.uploadAndSave(multipartMerged, "contract", "CONTR", contId,
					multipartMerged.getContentType());
			//S3 업로드 이후 예외 발생 시, 업로드 파일 삭제 로직도 고려해볼 것

			/** 6. 매물의 계약상태 정보 변경하기 */
			String lstgId = contract.getLstgId();
			int lstgRec = mapper.updateListingProdStat(lstgId);

//			/** DEBUG__병합된 파일 디렉토리에서 확인하기 ^0^ */
//			File debugCopy = new File("D:/debug/merged-" + System.currentTimeMillis() + ".pdf");
//			Files.copy(merged.toPath(), debugCopy.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

			/** 0. 응답신호 내보내기 */
			String resultJson = objectMapper.writeValueAsString(Map.of("success", true, "mergedPath", merged.getAbsolutePath(),
//					"debugPath", debugCopy.getAbsolutePath(),
					"contId", contId));
			
			return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(resultJson));
		} catch (Exception e) {
			e.printStackTrace();
			/* 0. 응답신호 내보내기 */
			String resultJson = objectMapper.writeValueAsString(Map.of("success", false, "error", e.getMessage()));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(aes256Util.encryptWithDynamicIV(resultJson));
		}

	}

	@Override
	public void readContractPDF(String contId) {
		// TODO Auto-generated method stub
		
	}

	@Transactional
	@Override
	public int removeProceedingContractBulk(List<String> selectedContractIds) {
		if (selectedContractIds == null || selectedContractIds.isEmpty())
		throw new IllegalArgumentException("삭제할 계약 ID 리스트가 비어 있습니다.");
		
		int expectedCount = selectedContractIds.size();
		int deletedCount = 0;
		
		for (String id : selectedContractIds) {
		    int result = mapper.deleteProceedingContract(id);
		    if (result == 0) {
		        throw new IllegalStateException("계약 ID " + id + "에 대한 삭제가 실패했습니다.");
		    }
		    deletedCount += result;
		}
		
		if (deletedCount != expectedCount)
		throw new IllegalStateException("일부 계약 삭제에 실패했습니다. 요청: " + expectedCount + ", 삭제: " + deletedCount);
		
		
		return deletedCount;
	}

	/**
	 * 서명페이지 개설 여부 확인
	 * 
	 */
	@Override
	public int openContractSignaturePage(String contId) {
		if (contId == null || contId.trim().isEmpty()) 
		    throw new IllegalArgumentException("계약 ID(contId)는 필수입니다.");
		
		int rec = mapper.updateProceedingContractSignYnToY(contId);
		
		if (rec == 0) 
		    throw new IllegalStateException("전자서명 상태 업데이트 실패: contId=" + contId);
		
		return rec;
	}
	/**
	 * 일정 시간 후 알아서 닫히게끔
	 */
	@Override
	public int expireContractSignaturePage(String contId) {
		if (contId == null || contId.trim().isEmpty())
			throw new IllegalArgumentException("계약 ID(contId)는 필수입니다.");
		int rec = mapper.updateProceedingContractSignYnToN(contId);
		if (rec == 0)
			throw new IllegalStateException("전자서명 상태 업데이트 실패: contId=" + contId);
		return rec;
	}

	@Override
	public String isSignPageOpened(String contId) {
		if(contId == null || contId.trim().isEmpty())
			throw new IllegalArgumentException("계약 ID(contId)는 필수입니다.");
		String isSignPageOpened = mapper.selectContractSignatureYn(contId);
		if(isSignPageOpened == null || isSignPageOpened.isEmpty())
			throw new IllegalStateException("전자서명 상태 업데이트 실패: contId=" + contId);
		return isSignPageOpened;
	}
	
	
	@Override
	public ContractVO readContractInfo(String contId) {
		if(contId == null || contId.trim().isEmpty())
			throw new IllegalArgumentException("계약 ID(contId)는 필수입니다.");
		ContractVO contract = mapper.selectContractInfo(contId);
		if(contract == null)
			throw new IllegalStateException("계약 정보 조회 실패: 파라미터(contId)=" + contId);
		return contract;
	}

	@Override
	public boolean isContractExist(String contId) {
		return mapper.isContractExist(contId);
	}
}
