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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.function.Function;
import org.apache.commons.codec.digest.DigestUtils;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import kr.or.ddit.broker.mapper.BrokerMapper;
import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerContractService;
import kr.or.ddit.main.mapper.MemberMapper;
import kr.or.ddit.main.member.service.MemberService;
import kr.or.ddit.util.calc.CalcOnContract;
import kr.or.ddit.util.crypto.AES256Util;
import kr.or.ddit.util.file.Base64DecodedMultipartFile;
import kr.or.ddit.util.file.ToMultipartFileUtil;
import kr.or.ddit.util.file.mapper.FileMapper;
import kr.or.ddit.util.file.service.FileService;
import kr.or.ddit.util.parse.SafeParse;
import kr.or.ddit.util.pdf.service.PDFService;
import kr.or.ddit.vo.ContractDigitalSignVO;
import kr.or.ddit.vo.ContractVO;
import kr.or.ddit.vo.FileVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.ListingWishlistVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.broker.dto.SignerDTO;
import kr.or.ddit.broker.dto.SignerStatusDTO;
import kr.or.ddit.broker.dto.StandardLeaseFormDTO;
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
	private FileMapper fileMapper;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private Validator validator;
	@Autowired
	private AES256Util aes256Util;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private MemberService memberService;

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
	 * @param List<signers>: 서명페이지에서 쓸 signers 객체(리스트) 초기값 로딩
	 * @return 계약참여자의 상태 관리를 위한 signers의 초기값
	 */
	@Override
	public List<Map<String, Object>> readContractPartyInfo(Map<String, String> partyTelnoParam) {

		String lesseeTelno = partyTelnoParam.get("lesseeTelno");
		String lessorTelno = partyTelnoParam.get("lessorTelno");
		String agentTelno = partyTelnoParam.get("agentTelno");
		String userRole = partyTelnoParam.get("userRole");

		MemberVO lesseeInfo = mapper.selectMemberByTelno(lesseeTelno);
		MemberVO lessorInfo = mapper.selectMemberByTelno(lessorTelno);
		MemberVO agentInfo = mapper.selectMemberByTelno(agentTelno);

		if (lesseeInfo == null || lessorInfo == null || agentInfo == null) {
			throw new IllegalArgumentException("계약 참여자 중 일부 정보를 찾을 수 없습니다.");
		}

		/// MAP.of로는 null값 불가능. 이렇게 만든 Map은 immutable(값도 수정 못함)
//		Map<String, Object> lessee = Map.of(
//				"role", "LESSEE",
//				"name", lesseeInfo.getMbrNm(),
//				"telno", lesseeInfo.getMbrTelno(),
//				"connected", false,
//				"signedAt", false,
//				"isValid", false,
//				"isRejected", false,
//				"tempPdfUrl", null
//				);
//		Map<String, Object> lessor = Map.of(
//				"role", "LESSOR",
//				"name", lessorInfo.getMbrNm(),
//				"telno", lessorInfo.getMbrTelno(),
//				"connected", false,
//				"signedAt", false,
//				"isValid", false,
//				"isRejected", false,
//				"tempPdfUrl", null
//				);
//		Map<String, Object> agent = Map.of(
//				"role", "AGENT",
//				"name", agentInfo.getMbrNm(),
//				"telno", agentInfo.getMbrTelno(),
//				"connected", false,
//				"signedAt", false,
//				"isValid", false,
//				"isRejected", false,
//				"tempPdfUrl", null
//				);

		Map<String, Object> lessee = new HashMap<>();
		lessee.put("role", "LESSEE");
		lessee.put("code", lesseeInfo.getMbrCd());
		lessee.put("id", lesseeInfo.getMbrId());
		lessee.put("name", lesseeInfo.getMbrNm());
		lessee.put("telno", lesseeInfo.getMbrTelno());
		lessee.put("connected", false);
		lessee.put("signedAt", null);
		lessee.put("isValid", null);
		lessee.put("isRejected", false);

		Map<String, Object> lessor = new HashMap<>();
		lessor.put("role", "LESSOR");
		lessor.put("code", lessorInfo.getMbrCd());
		lessor.put("id", lessorInfo.getMbrId());
		lessor.put("name", lessorInfo.getMbrNm());
		lessor.put("telno", lessorInfo.getMbrTelno());
		lessor.put("connected", false);
		lessor.put("signedAt", null);
		lessor.put("isValid", null);
		lessor.put("isRejected", false);

		Map<String, Object> agent = new HashMap<>();
		agent.put("role", "AGENT");
		agent.put("code", agentInfo.getMbrCd());
		agent.put("id", agentInfo.getMbrId());
		agent.put("name", agentInfo.getMbrNm());
		agent.put("telno", agentInfo.getMbrTelno());
		agent.put("connected", false);
		agent.put("signedAt", null);
		agent.put("isValid", null);
		agent.put("isRejected", false);

//		if("LESSEE".equals(userRole))lessee.put("connected", true);
//		if("LESSOR".equals(userRole))lessor.put("connected", true);
//		if("AGENT".equals(userRole))agent.put("connected", true);

		switch (userRole) {
		case "LESSEE" -> lessee.put("connected", true);
		case "LESSOR" -> lessor.put("connected", true);
		case "AGENT" -> agent.put("connected", true);
		}

		List<Map<String, Object>> signers = List.of(lessee, lessor, agent);
		return signers;
	}

	@Override
	public Map<String, SignerDTO> readContractPartyInfo2(Map<String, String> partyTelnoParam,
			HttpServletRequest request) {
		String lesseeTelno = partyTelnoParam.get("lesseeTelno");
		String lessorTelno = partyTelnoParam.get("lessorTelno");
		String agentTelno = partyTelnoParam.get("agentTelno");
		String userRole = partyTelnoParam.get("userRole");
		String contId = partyTelnoParam.get("contId");

		MemberVO lesseeInfo = mapper.selectMemberByTelno(lesseeTelno);
		MemberVO lessorInfo = mapper.selectMemberByTelno(lessorTelno);
		MemberVO agentInfo = mapper.selectMemberByTelno(agentTelno);

		if (lesseeInfo == null || lessorInfo == null || agentInfo == null) {
			throw new IllegalArgumentException("계약 참여자 중 일부 정보를 찾을 수 없습니다.");
		}

		SignerDTO lessor = SignerDTO.builder().contId(contId).role("LESSOR").code(lessorInfo.getMbrCd())
				.id(lessorInfo.getMbrId()).name(lessorInfo.getMbrNm()).telno(lessorInfo.getMbrTelno()).signerStatus("")
				.ipAddr("").isJoined(false).signedAt(null).isSigned(false).hashVal("").isValid(false).base64("")
				.build();
		SignerDTO lessee = SignerDTO.builder().contId(contId).role("LESSEE").code(lesseeInfo.getMbrCd())
				.id(lesseeInfo.getMbrId()).name(lesseeInfo.getMbrNm()).telno(lesseeInfo.getMbrTelno()).signerStatus("")
				.ipAddr("").isJoined(false).signedAt(null).isSigned(false).hashVal("").isValid(false).base64("")
				.build();
		SignerDTO agent = SignerDTO.builder().contId(contId).role("AGENT").code(agentInfo.getMbrCd())
				.id(agentInfo.getMbrId()).name(agentInfo.getMbrNm()).telno(agentInfo.getMbrTelno()).signerStatus("")
				.ipAddr("").isJoined(false).signedAt(null).isSigned(false).hashVal("").isValid(false).base64("")
				.build();
		switch (userRole) {
		case "LESSOR" -> {
			lessor.setIsJoined(true);
			lessor.setIpAddr(request.getRemoteAddr());
		}
		case "LESSEE" -> {
			lessee.setIsJoined(true);
			lessee.setIpAddr(request.getRemoteAddr());
		}
		case "AGENT" -> {
			agent.setIsJoined(true);
			agent.setIpAddr(request.getRemoteAddr());
		}
		}

		Map<String, SignerDTO> signers = new HashMap<>();
		signers.put("LESSOR", lessor);
		signers.put("LESSEE", lessee);
		signers.put("AGENT", agent);
		return signers;
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
	public List<ContractVO> readMngContractsList(String mbrCd) {
		List<ContractVO> mngContractsList = null;
		mngContractsList = mapper.selectMngContractsList(mbrCd);
		return mngContractsList;
	}

	@Override
	public List<ContractVO> readProceedingContractsList(String mbrCd) {
		List<ContractVO> proceedingContractsList = null;
		proceedingContractsList = mapper.selectProceedingContractsList(mbrCd);
		return proceedingContractsList;
	}

	@Transactional
	@Override
	public ResponseEntity<?> processOfCreatingContract(String decryptedJson, Principal principal)
			throws JsonProcessingException {
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
					errorMsg.append("- ").append(violation.getPropertyPath()).append(": ")
							.append(violation.getMessage()).append("\n");
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
			MultipartFile multipartMerged = ToMultipartFileUtil.convert(merged);

			/** 4. DB에 계약정보 레코드 입력 */

			Long deposit = SafeParse.safeParseLong(Optional.ofNullable(contractInfo.getListingTypeSale()).map(type -> {
				// 전세 계약일 경우
				if ("001".equals(type))
					return contractInfo.getListingLease(); // 전세금 (String)
				// 월세 계약일 경우
				if ("002".equals(type))
					return contractInfo.getListingLeaseAmt(); // 보증금 (String)
				// 매칭되는 타입이 없으면 null 반환
				return null;
			}).orElse("0") // null이면 "0"으로 대체
			);

			ContractVO contract = ContractVO.builder().mbrCdBrok(authUnpack.getMbrCd(principal.getName()))
					.lstgId(contractInfo.getListingId()).contTypeCode(contractInfo.getListingTypeSale())
					.contDeposit(deposit).contTaxAmount(CalcOnContract.getTaxAmount(deposit))
					.contAmount(SafeParse.safeParseLong(contractInfo.getListingLeaseM())).contStatCd("001")
					.contDtm(null).contTypeGroupCd(null).contStatGroupCd(null)
					.contLesseeTelno(contractInfo.getLesseeTelno()).contTenancyTelno(contractInfo.getLessorTelno())
					.contBrokerTelno(contractInfo.getAgentTelno()).build();
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
			// S3 업로드 이후 예외 발생 시, 업로드 파일 삭제 로직도 고려해볼 것

			/** 6. 매물의 계약상태 정보 변경하기 */
			String lstgId = contract.getLstgId();
			int lstgRec = mapper.updateListingProdStat(lstgId);

//			/** DEBUG__병합된 파일 디렉토리에서 확인하기 ^0^ */
//			File debugCopy = new File("D:/debug/merged-" + System.currentTimeMillis() + ".pdf");
//			Files.copy(merged.toPath(), debugCopy.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

			/** 0. 응답신호 내보내기 */
			String resultJson = objectMapper
					.writeValueAsString(Map.of("success", true, "mergedPath", merged.getAbsolutePath(),
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
	public FileVO readContractPDFFile(String contId) {
		Integer fileAttachSeq = fileMapper.selectTempContrMaxAttachSeq(contId);
		if (fileAttachSeq == null || fileAttachSeq == 0) {
			fileAttachSeq = 1;
		}

		FileVO file = fileMapper.selectTempContractFile(contId, fileAttachSeq);
		if (file == null) {
			file = fileMapper.selectContractFile(contId, fileAttachSeq);
		}
		return file;
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
		if (contId == null || contId.trim().isEmpty())
			throw new IllegalArgumentException("계약 ID(contId)는 필수입니다.");
		String isSignPageOpened = mapper.selectContractSignatureYn(contId);
		if (isSignPageOpened == null || isSignPageOpened.isEmpty())
			throw new IllegalStateException("전자서명 상태 업데이트 실패: contId=" + contId);
		return isSignPageOpened;
	}

	@Override
	public ContractVO readContractInfo(String contId) {
		if (contId == null || contId.trim().isEmpty())
			throw new IllegalArgumentException("계약 ID(contId)는 필수입니다.");
		ContractVO contract = mapper.selectContractInfo(contId);
		if (contract == null)
			throw new IllegalStateException("계약 정보 조회 실패: 파라미터(contId)=" + contId);
		return contract;
	}

	@Override
	public boolean isContractExist(String contId) {
		return mapper.isContractExist(contId);
	}

	@Override
	public List<Map<String, Object>> validateSignerStatus(String contId) {

		// 계약 레코드에서는 계약 참여자의 전화번호가 식별자
		ContractVO contract = mapper.selectContractInfo(contId);
		List<Map<String, Object>> defaultSigners = new ArrayList<>();

		// 계약 참여자의 기본 정보
		defaultSigners
				.add(Map.of("role", "AGENT", "name", contract.getContBrokerTelno(), "signedAt", null, "isValid", null));
		defaultSigners.add(
				Map.of("role", "LESSOR", "name", contract.getContTenancyTelno(), "signedAt", null, "isValid", null));
		defaultSigners.add(
				Map.of("role", "LESSEE", "name", contract.getContLesseeTelno(), "signedAt", null, "isValid", null));

		// 계약 참여자의 서명 데이터
		// 제네릭 타입 추론의 실패 유의
		/*
		 * return defaultSigners.stream().map(participant -> { String role = (String)
		 * participant.get("role"); String name = (String) participant.get("name");
		 * 
		 * ContractDigitalSignVO sign = signedMap.get(role); if (sign != null) { String
		 * raw = sign.getContDtBaseData() + sign.getMbrCd() + sign.getContId() +
		 * sign.getContDtSignType() + sign.getContDtSignDtm();
		 * 
		 * String serverHash = DigestUtils.sha256Hex(raw); boolean isValid =
		 * serverHash.equals(sign.getContDtSignHashVal());
		 * 
		 * return Map.of( "role", role, "name", name, "signedAt",
		 * sign.getContDtSignDtm(), "isValid", isValid, "isRejected",
		 * sign.getIsRejected(), "tempPdfUrl", sign.getTempPdfUrl() ); } else { return
		 * participant; // 서명되지 않음 } }).collect(Collectors.toList());
		 * 
		 * map(...) 안에서 Function<...> 제네릭 타입 추론에 실패. why? -
		 * stream().map(...).collect(Collectors.toList()) 부분이 복잡 - 연산식이 복잡해지면 타입 추론에 실패
		 * why? - Map<String, Object> 형태는 Java 타입추론에 있어 금쪽이. - 그냥 타입 안정성이라곤 쥐뿔도 없는 형태 -
		 * Stream 내에서 처리하기 위해선 캐스팅을 명확하게 하거나 DTO로 전환.
		 * 
		 * 
		 * To.. 1. 람다식은 명확한 입력 타입 선언 list.stream().map(item -> { ... }) // 🚫 sometimes
		 * fails list.stream().map((Function<MyType, MyReturnType>) item -> { ... }) //
		 * ✅ good - `map((Function<Target, Result> lambda)` 처럼 람다 타입을 명시한다!
		 * 
		 * 2. .collect(Collectors.toMap(...)) 반환 타입에 유의 Map<String, User> map =
		 * list.stream() .collect(Collectors.toMap(User::getId, Function.identity())); -
		 * key 또는 value mapper가 null 반환 시, IllegalStateException 발생 - 키의 중복에 대응하거나(1),
		 * 명시적 타입을 지정(2)한다.
		 * 
		 * 3. Map<String, Object>는 제네릭 타입 추론의 금쪽이 - String role = (String)
		 * participant.get("role");
		 * 
		 * 4. .stream().map(...).collect(...) 반환타입을 변수로 고정시킨다. List<Map<String, Object>>
		 * result = list.stream() .map(...) // ↔ 컴파일러가 여기서 타입 추론
		 * .collect(Collectors.toList()); // 🔐 변수가 타입을 고정해줌 - 익명으로 선언하지 말고, 변수 선언을 통해
		 * 타입을 못 박아버린다.
		 * 
		 * 5. Function/Predicate/Supplier 함수형 인터페이스 사용 Function<T, R> mapper = t -> ...
		 * list.stream().map(mapper).collect(...) - 람다 함수의 로직이 복잡해도 컴파일러의 타입추론이 수월해짐
		 * 
		 * 6. 어지간한 컴파일 오류의 근본 원인은 람다식의 타입추론 실패. - 그러니 람다에 꼭 타입 붙인다. Function<T, R>
		 * 
		 * 7. 금쪽이 Map<String, Object> 대신 DTO 활용 - Map은 코드가 항상 복잡해지는데다, IDE도 타입 추적을 못함.
		 * 
		 * | 구간 | 설명 | 대처 방법 | | --------------------------- |
		 * --------------------------- | ----------------------------------------- | |
		 * `stream().map(...)` | 람다 복잡할 때 | 람다에 타입명시 or Function 객체 사용 | |
		 * `Collectors.toMap(...)` | 키 중복, 타입 추론 불가 | 타입 지정 + mergeFunction 지정 | |
		 * `List<Map<String, Object>>` | 캐스팅 필요, 추론 불가 | DTO 전환 권장 | |
		 * `Optional.map(...)` | Optional 내부 타입 미확정 | 명시적 람다 또는 타입 고정 변수 사용 |
		 * 
		 */
		List<ContractDigitalSignVO> signs = mapper.selectDtSignList(contId);

		// role 기준으로 서명된 데이터 매핑
		Map<String, ContractDigitalSignVO> signedMap = signs.stream()
				.collect(Collectors.toMap(ContractDigitalSignVO::getContDtSignType, Function.identity()));

		return defaultSigners.stream().map((Function<Map<String, Object>, Map<String, Object>>) participant -> {
			String role = (String) participant.get("role");
			String name = (String) participant.get("name");

			ContractDigitalSignVO sign = signedMap.get(role);
			if (sign != null) {
				String raw = sign.getContDtBaseData() + sign.getMbrCd() + sign.getContId() + sign.getContDtSignType()
						+ sign.getContDtSignDtm();

				String serverHash = DigestUtils.sha256Hex(raw);
				boolean isValid = serverHash.equals(sign.getContDtSignHashVal());

				return Map.of("role", role, "name", name, "signedAt", sign.getContDtSignDtm(), "isValid", isValid);
			} else {
				return participant; // 서명되지 않은 상태 그대로 유지
			}
		}).collect(Collectors.toList());
	}

	/**
	 * contId에 대한 전자서명 정보를 조회, List로 반환하는 단순 조회
	 * 
	 * @param contId
	 * @return List<ContractDigitalSignVO>
	 */
	@Override
	public List<ContractDigitalSignVO> readSignatureList(String contId) {
		return mapper.selectDtSignList(contId);
	}

	@Override
	public FileVO readLatestSignedContractPdf(String contId) {
		FileVO file = mapper.selectLatestSignedContractPdf(contId);
		if (file == null)
			file = fileMapper.selectContractFile(contId, 1);
		return file;
	}

	@Override
	public void conclusionContract(String contId) {
		mapper.updateConclusedContract(contId);
	}

}
