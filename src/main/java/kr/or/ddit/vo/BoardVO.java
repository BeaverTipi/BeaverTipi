package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import kr.or.ddit.util.validate.DeleteGroup;
import kr.or.ddit.util.validate.InsertGroup;
import kr.or.ddit.util.validate.UpdateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="brdNo")
public class BoardVO implements Serializable{
	private String brdDelYnGrpCd;
	private String brdCtgryGrpCd;
	@NotBlank(groups = {UpdateGroup.class, DeleteGroup.class})
	private String brdNo;
	@NotBlank
	private String brdCode;
	@NotBlank(groups = InsertGroup.class)
	private String mbrCd;
	private String mbrId; // 작성자 ID
	@NotBlank
	private String brdTitlNm;
	@NotBlank
	private String brdCont;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime brdPblsDtm;
	private Integer brdVwCnt;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime brdModDtm;
	@NotBlank(groups = {InsertGroup.class, DeleteGroup.class})
	private String brdDelYn;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime brdEndDtm;
	
	private String noticeType;
	private String faqCtgry;
	private String qnaCtgry;
	private String noticeTypeName;	
	private List<NoticeVO> notice;
	private BoardCartegoryVO boardCartegory;
	private List<FAQVO> faq;
	private List<QnAVO> qna;
	
	private List<FileVO> attachFiles;	// 첨부파일 목록 필드
	
	// 시스템 관리자 관련
	private String brdCtgryValue;
    
	private ReportVO reportVO;
	
    private MemberVO member;
    
    // ReportUserList를 위한 메서드
    public String getFormattedBrdPblsDtm() {
        if (this.brdPblsDtm == null) {
            return "";
        }
        // JSP에서 원하는 "yyyy-MM-dd HH:mm" 포맷에 맞춰 포맷터 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return this.brdPblsDtm.format(formatter);
    }
    
    private AdsClientVO adsClientVO;	// BoardVO에 AdsClientVO 객체를 포함시켜주는 필드

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate adsReqPblsStartDtFrom; // 광고 요청 게재 시작날짜 From
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate adsReqPblsEndDtTo;     // 광고 요청 게재 종료날짜 To
    
 
}
