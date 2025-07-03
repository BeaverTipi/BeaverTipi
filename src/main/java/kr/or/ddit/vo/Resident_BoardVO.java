package kr.or.ddit.vo;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "rsdBrdId")
public class Resident_BoardVO {
	private String rsdBrdId;
	private String rsdBrdTitl;
	private String rsdBrdCont;
	private LocalDateTime rsdBrdPblsDtm;
	private Integer rsdBrdCnt;
	private LocalDateTime rsdBrdModDtm;
	private String rsdBrdDelYn;
	private String brdCode;
	private String mbrCd;
}
