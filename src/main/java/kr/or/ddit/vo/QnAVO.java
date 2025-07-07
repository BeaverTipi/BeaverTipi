package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of={"qnaId","brdNo"})
public class QnAVO extends BoardVO implements Serializable{
	private String brdNo;
	private String qnaId;
	private String qnaCtgry;
	private String qnaTitl;
	private String answerCont;
	private String qnaYn;
	private LocalDate qnaCreDtm;
	private LocalDate qnaModDtm;
	private String qnaStatus;
	private String qnaCont;
	private LocalDate qnaAnsweredAt;
	private String mbrCd;
	private String answerAdminId;
	private String qnaCtgryGrpCd;
	private String qnaStatusGrpCd;
	private String qnaYnGrpCd;
}
