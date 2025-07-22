package kr.or.ddit.vo;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data

@SuppressWarnings("serial")
public class UnitResidentVO implements Serializable {
	@NotBlank
	private String unitId;
	@NotBlank
	private String bldgId;
	private String rentalPtyId;
	private String moveInDt;
	private String moveOutDt;
	private String mbrCd;
	
	
	private String bldgNm;
	
	private BuildingVO building;
	private MemberVO member;
	private UnitVO unit;
}

