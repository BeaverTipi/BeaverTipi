package kr.or.ddit.vo;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"rptId"}) // rptId가 PK이므로 EquqlsAndHashCode는 rptId로 유지
public class ReportVO extends BoardVO implements Serializable { // BoardVO 상속 추가
	private String rptId;             // 신고 번호
	private String rptCode;           // 신고 유형: 'MEMB' - 회원, 'LSTG' - 매물, 'BLDG' - 관리주택
	private String rptTargetId;       // 신고 대상 ID, ex) MBR_CD? MBR_ID?, LSTG_ID
	private String rptTargetNm;		  // 신고 대상 이름 (매물명, 회원명)
	private String rptStatusCode;     // 신고 처리 상태 코드: '등록', '접수처리중', '처리완료'
	private String rptDelYn;          // 신고 삭제 여부

	
	private String rptTargetMbrStatus; // 신고 대상 회원의 현재 상태 (Member 테이블에서 가져옴)
	
	private String rptTargetMbrCd; 	   // 신고 대상 회원의 실제 고유 코드 (Member 테이블에서 가져옴. MBR_ID로 사용)
	
	private String lstgDel;			   // 매물 삭제 여부 (Listing 테이블에서 가져옴)
	
	private String mbrId;			   // 신고자 ID
	
	private List<FileVO> attachFiles;
}