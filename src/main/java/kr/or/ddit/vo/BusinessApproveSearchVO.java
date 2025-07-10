package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author 김아린
 * 비즈니스 계정 목록 검색 조건 VO
 */
@Data
public class BusinessApproveSearchVO implements Serializable {

	// 회원 관련
	private String mbrCd;     // 회원 코드
	private String mbrId;     // 아이디
	private String mbrNm;     // 이름
	private String authApprYn;

	// 구독 관련
	private String role;           // 유형 (broker / landlord 등)
}
