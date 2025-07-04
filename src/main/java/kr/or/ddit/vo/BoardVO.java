package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import kr.or.ddit.util.validate.DeleteGroup;
import kr.or.ddit.util.validate.InsertGroup;
import kr.or.ddit.util.validate.UpdateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="brdNo")
public class BoardVO implements Serializable{
	@NotBlank(groups = {UpdateGroup.class, DeleteGroup.class})
	private String brdNo;
	@NotBlank
	private String brdCode;
	@NotBlank(groups = InsertGroup.class)
	private String mbrCd;
	@NotBlank
	private String brdTitlNm;
	@NotBlank
	private String brdCont;
	private LocalDateTime brdPblsDtm;
	private int brdVmCnt;
	private LocalDateTime brdModDtm;
	@NotBlank(groups = {InsertGroup.class, DeleteGroup.class})
	private String brdDelYn;
	
	private MemberVO member;
	private BoardCartegoryVO boardCartegory;
	private List<NoticeVO> notice;
	private List<FAQVO> faq;
	private List<QnAVO> qna;
}
