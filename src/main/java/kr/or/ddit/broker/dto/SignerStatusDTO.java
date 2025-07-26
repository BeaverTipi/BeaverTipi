package kr.or.ddit.broker.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 전자서명 페이지에서 각 서명자의 실시간 상태를 표현하는 DTO
 * - 클라이언트와 WebSocket 및 REST 응답에 사용됨
 * - DB의 ContractDigitalSignVO에서 파생된 구조
 * - 각 역할별 서명자 상태(board) 및 검증 결과(validity)에 활용
 * - 실시간으로 변화하는 데이터 구조를 위한 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignerStatusDTO {

	/**참여자 상태
	 * - NOT_JOINED: 아직 접속하지 않음
	 * - JOINED: WebSocket 접속 중
	 * - SIGNED: 서명 완료
	 * - REJECTED: 거절
	 * - DISCONNECTED: 접속했지만 현재 연결 해제됨
	 */
	private String status;
	
	/**서명자 역할
     * - LESSEE
     * - LESSOR
     * - AGENT
     */
    private String role;

    /**서명자 회원코드
     * mbrCd
     */
    private String mbrCd;
    
    /**서명자 이름
     */
    private String mbrNm;
    
    /**서명자 전화번호
     */
    private String telno;
    
    /**서명 완료 시각
     * contDtSignDtm
     */
    private LocalDateTime signedAt;

    /**생성한 hash 값
     * contDtSignHashVal
     */
    private String hashVal;

    /**클라이언트 접속 IP
     * contDtIpAddr
     */
    private String ipAddr;
    
    /**hash 검증 성공 여부
     */
    private Boolean isValid;
}
