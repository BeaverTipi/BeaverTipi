package kr.or.ddit.vo;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="brdNo")
public class BoardVO {
	public String brdNo;
	public String brdCode;
	public String mbrCd;
	public String brdTitlNm;
	public String brdCont;
	public LocalDateTime brdPblsDtm;
	public Integer brdVmCnt;
	public LocalDateTime brdModDtm;
	public String brdDelYn;
}
