package kr.or.ddit.vo;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="brdNo")
public class BoardVO {
	private String brdNo;
	private String brdCode;
	private String mbrCd;
	private String brdTitlNm;
	private String brdCont;
	private LocalDateTime brdPblsDtm;
	private Integer brdVmCnt;
	private LocalDateTime brdModDtm;
	private String brdDelYn;
	
	private MemberVO member;
}
