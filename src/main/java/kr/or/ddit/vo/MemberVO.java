package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import kr.or.ddit.util.validate.InsertGroup;
import kr.or.ddit.util.validate.OAuth2UpdateGroup;
import kr.or.ddit.util.validate.UpdateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "mbrCd")
public class MemberVO implements Serializable {
	@NotBlank(groups = {UpdateGroup.class,OAuth2UpdateGroup.class})
	private String mbrCd;
	@NotBlank
	private String mbrId;
	@NotBlank(groups = {InsertGroup.class,UpdateGroup.class})
	private String mbrPw;
	@NotBlank
	private String mbrNm;
	private String mbrNnm;
//	@NotBlank
	private String mbrTelno;
	private String mbrEmlAddr;
	private String mbrBasicAddr;
	private String mbrDetailAddr;
	private String mbrProfilImage;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate mbrFrstRegDt;
	private String mbrStatusCode;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String mbrDeleteDate;
	private String mbrZip;
	private List<RoleAchievedVO> memRoleList;
	private List<UnitResidentVO> residentList;
	private TenancyVO tenancy;
	private BrokerVO broker;
	
}
