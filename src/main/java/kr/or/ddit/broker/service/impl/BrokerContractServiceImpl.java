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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import kr.or.ddit.broker.mapper.BrokerMapper;
import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerContractService;
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
	PDFService pdfService;
	@Autowired
	BrokerAuthUnpackingService authUnpack;
	@Autowired
	FileService fileService;

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

	@Override
	public ResponseEntity<?> processOfCreatingContract(String decryptedJson, Principal principal) {
		// TODO 컨트롤러 다이어트 들어가야지...
		try {
			/** 2. JSON -> POJO 매핑 */
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			objectMapper.registerModule(new JavaTimeModule());
			objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

			Map<String, Object> parsedRequest = objectMapper.readValue(decryptedJson, new TypeReference<>() {
			});
			StandardLeaseFormDTO contractInfo = objectMapper.convertValue(parsedRequest.get("contractInfo"),
					StandardLeaseFormDTO.class);
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
					.mbrCd(String.valueOf(contractInfo.getLesseeMbrCd()))
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

			/** 6. 매물의 계약상태 정보 변경하기 */
			String lstgId = contract.getLstgId();
			int lstgRec = mapper.updateListingProdStat(lstgId);

//			/** DEBUG__병합된 파일 디렉토리에서 확인하기 ^0^ */
//			File debugCopy = new File("D:/debug/merged-" + System.currentTimeMillis() + ".pdf");
//			Files.copy(merged.toPath(), debugCopy.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

			/** 0. 응답신호 내보내기 */
			return ResponseEntity.ok(Map.of("success", true, "mergedPath", merged.getAbsolutePath(),
//					"debugPath", debugCopy.getAbsolutePath(),
					"contId", contId));
		} catch (Exception e) {
			e.printStackTrace();
			/* 0. 응답신호 내보내기 */
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("success", false, "error", e.getMessage()));
		}

	}
}
