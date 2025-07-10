package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import kr.or.ddit.util.validate.InsertGroup;
import kr.or.ddit.util.validate.OAuth2InsertGroup;
import kr.or.ddit.util.validate.OAuth2UpdateGroup;
import kr.or.ddit.util.validate.UpdateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "mbrCd")
public class MemberVO implements Serializable {

	// 수정 시
	@NotBlank(groups = {UpdateGroup.class, OAuth2UpdateGroup.class})
	private String mbrCd;

	// 일반 회원가입에서만 검증 (중개사 등록 시에는 제외)
	@NotBlank(groups = {InsertGroup.class, UpdateGroup.class, OAuth2UpdateGroup.class, OAuth2InsertGroup.class})
	private String mbrId;

	// 일반 회원가입/수정
	@NotBlank(groups = {InsertGroup.class, UpdateGroup.class})
	private String mbrPw;

	// 일반 회원가입/수정
	@NotBlank(groups = {InsertGroup.class, UpdateGroup.class, OAuth2UpdateGroup.class, OAuth2InsertGroup.class})
	private String mbrNm;

	// 선택 항목이므로 그룹 없음
	private String mbrNnm;

	@NotBlank(groups = {InsertGroup.class, UpdateGroup.class, OAuth2UpdateGroup.class, OAuth2InsertGroup.class})
	private String mbrTelno;

	// 이메일은 선택 항목
	@Email
	private String mbrEmlAddr;

	// 주소 관련 필드도 선택적 사용으로 처리
	private String mbrBasicAddr;
	private String mbrDetailAddr;
	private String mbrProfilImage;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate mbrFrstRegDt;

	private String mbrStatusCode;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String mbrDeleteDate;

	private String mbrZip;

	// 하위 리스트 및 연관 VO
	private List<RoleAchievedVO> memRoleList;
	private List<UnitResidentVO> residentList;
	private TenancyVO tenancy;
	private BrokerVO broker;
}
