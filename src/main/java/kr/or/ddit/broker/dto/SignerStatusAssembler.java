package kr.or.ddit.broker.dto;

import kr.or.ddit.vo.ContractDigitalSignVO;
import kr.or.ddit.vo.ContractVO;
import kr.or.ddit.broker.dto.SignerStatusDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

/**
 * ContractDigitalSignVO → SignerStatusDTO 변환 유틸리티 클래스
 * - Telno는 ContractVO에서 역할에 따라 선택적으로 추출
 * - hash 검증 결과(isValid)는 호출자에서 주입하는 것을 권장
 */
@Component
public class SignerStatusAssembler {
	
	public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
	/**
     * ContractDigitalSignVO와 ContractVO를 바탕으로 DTO 구성
     */
    public SignerStatusDTO toDTO(ContractDigitalSignVO vo, ContractVO contract) {
        String role = vo.getContDtSignType();

        String telno = switch (role) {
            case "LESSEE" -> contract.getContLesseeTelno();
            case "LESSOR" -> contract.getContTenancyTelno();
            case "AGENT" -> contract.getContBrokerTelno();
            default -> null;
        };
        
        Boolean isRejected = "REJECTED".equalsIgnoreCase(vo.getContDtSignStat()) ? true : null;
        
        return SignerStatusDTO.builder()
                .role(role)
                .mbrCd(vo.getMbrCd())
                .signedAt(LocalDateTime.parse(vo.getContDtSignDtm(), formatter))
                .hashVal(vo.getContDtSignHashVal())
                .ipAddr(vo.getContDtIpAddr())
                .mbrNm(vo.getMbrCd()) // 또는 JOIN된 mbrNm
                .telno(telno)
//                .isRejected(isRejected)
//                .tempPdfUrl(vo.getContDtSignImg())
//                .connected(false)
                .build();
    }

    public List<SignerStatusDTO> toDTOList(List<ContractDigitalSignVO> voList, ContractVO contract) {
        return voList.stream()
                .map(vo -> toDTO(vo, contract))
                .collect(Collectors.toList());
    }
    
    public SignerStatusDTO makeDefaultSigner(String role, String telno, String name, String mbrCd, String ipAddr) {
        return SignerStatusDTO.builder()
            .role(role)
            .mbrNm(name) // 이름 미정
            .telno(telno)
            .mbrCd(mbrCd)
            .ipAddr(ipAddr)
            .signedAt(null)
            .isValid(null)
//            .isRejected(false)
            .hashVal(null)
//            .tempPdfUrl(null)
            .build();
    }
}
