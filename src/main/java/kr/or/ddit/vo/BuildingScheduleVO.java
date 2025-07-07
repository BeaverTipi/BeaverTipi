package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(of="bscId")
public class BuildingScheduleVO implements Serializable{
	private String bscId;
	private String bldgId;
	private String bscTitlNm;
	private String bscCont;
	private String bscStrDtm;
	private String bscEndDtm;
	private String bscRptSetCont;
	private String rentalPtyId;
}
