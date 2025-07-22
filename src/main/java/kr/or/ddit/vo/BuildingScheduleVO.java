package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(of="bscId")
public class BuildingScheduleVO implements Serializable{
	private String bscId;
	private String bldgId;
	private String bscTitlNm;
	private String bscCont;
	private LocalDateTime bscStrDtm;
	private LocalDateTime bscEndDtm;
	private String bscRptSetCont;
	private String rentalPtyId;
	private String bscDelYn;
}
