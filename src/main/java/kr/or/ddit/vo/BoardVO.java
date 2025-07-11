package kr.or.ddit.vo;

import java.io.Serializable;
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
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime brdPblsDtm;
	private Integer brdVwCnt;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime brdModDtm;
	@NotBlank(groups = {InsertGroup.class, DeleteGroup.class})
	private String brdDelYn;
	
	private List<NoticeVO> notice;
	private BoardCartegoryVO boardCartegory;
	private List<FAQVO> faq;
	private List<QnAVO> qna;
	
	private List<FileVO> attachFiles;	// 첨부파일 목록 필드
	
	
	
	// REPORT 테이블 관련 필드
	private String reportId;       		// 신고 번호
    private String rptCode;        		// 신고 유형: 'MEMB' - 회원, 'LSTG' - 매물 'BLDG' - '관리주택'
    private String rptTargetId;     	// 신고 대상 ID, ex) MBR_CD? MBR_ID?, BRD_NO
    private String rptTargetMbrCd;		// 신고 대상 회원의 실제 고유 코드 근데 ID 쓸거임 (MBR_ID)
	private String rptTargetMbrStatus;	// 신고 대상 회원의 현재 상태를 담을 필드
    private String rptStatusCode;  		// 신고 처리 상태 코드: '등록', '접수처리중', '처리완료'
    private String rptDelYn;        	// 신고 삭제 여부
    private String lstgDel;				// 매물 삭제 여부 필드

    // 검색 조건을 위한 필드
    private String searchTitle;     // 제목 검색
    private String searchWriter;    // 작성자 ID (MBR_CD) 검색
    private String searchRptStatusCode; // 신고 처리 상태 검색
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime brdPblsDtmFrom; // 게시글 게시일시 (신고일자로 사용) 시작
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime brdPblsDtmTo;   // 게시글 게시일시 (신고일자로 사용) 종료
    
    private MemberVO member;
    
    private String searchRptCode;
    
    // ReportUserList를 위한 메서드
    public String getFormattedBrdPblsDtm() {
        if (this.brdPblsDtm == null) {
            return "";
        }
        // JSP에서 원하는 "yyyy-MM-dd HH:mm" 포맷에 맞춰 포맷터 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return this.brdPblsDtm.format(formatter);
    }
}
