package kr.or.ddit.vo;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="QnAId")
public class QnAVO {
	private String qnaId;
	private String qnaCtgry;
	private String qnaTitl;
	private String answerCont;
	private String qnaYn;
	private LocalDate qnaCreDtm;
	private LocalDate qnaModDtm;
	private String qnaStatus;
	private String qnaCont;
	private String qnaAnsweredAt;
	private String mbrCd;
	private LocalDate answerAdminId;
	private String brdCode;
	private String qnaCtgryGrpCd;
	private String qnaStatusGrpCd;
	private String qnaYnGrpCd;
}
