package kr.or.ddit.vo;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"rsdBrdId","bldgId"})
public class ResidentBoardVO {
	private String rsdBrdId;
	private String rsdBrdTitl;
	private String rsdBrdCont;
	private LocalDateTime rsdBrdPblsDtm;
	private Integer rsdBrdCnt;
	private LocalDateTime rsdBrdModDtm;
	private String rsdBrdDelYn;
	private String brdCode;
	private String mbrCd;
	
	private String bldgId;
	
	public Date getRsdBrdPblsDate() {
		if(rsdBrdPblsDtm ==null) return null;
		return Date.from(rsdBrdPblsDtm.atZone(ZoneId.systemDefault()).toInstant());
	}
	
}
