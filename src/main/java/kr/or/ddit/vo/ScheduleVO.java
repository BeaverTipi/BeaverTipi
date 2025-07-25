package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "scdId")
public class ScheduleVO implements Serializable{
	
	private String scdId;
	private String mbrCd;
	private String scdTitlNm;
	private String scdCont;
	private LocalDateTime scdStrDtm;
	private LocalDateTime scdEndDtm;
	private String scdRptSetCont;
	private boolean scdDelYn;
	private String scdLevel;
}
