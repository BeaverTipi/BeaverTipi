package kr.or.ddit.vo;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="faqId")
public class FAQVO {
	private String faqId;
	private String brdNo;
	private String faqCtgry;
	private String faqTitl;
	private String faqCont;
	private Integer faqCnt;
	private String faqYn;
	private String faqMbrId;
	private LocalDate faqCreDtm;
	private LocalDate faqModDtm;
	private String faqCtgryGrpCd;
	private String faqYnGrpCd;
}
=======
package kr.or.ddit.vo;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="faqId")
public class FAQVO {
	private String faqId;
	private String brdNo;
	private String faqCtgry;
	private String faqTitl;
	private String faqCont;
	private Integer faqCnt;
	private String faqYn;
	private String faqMbrId;
	private LocalDate faqCreDtm;
	private LocalDate faqModDtm;
	private String faqCtgryGrpCd;
	private String faqYnGrpCd;
}
