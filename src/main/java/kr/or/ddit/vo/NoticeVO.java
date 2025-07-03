package kr.or.ddit.vo;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="noticeNo")
public class NoticeVO {
	private String noticeNo;
	private String faq;
	private String noticeType;
	private String noticeTop;
	private LocalDate noticeEndDtm;
	private String noticeDelYn;
}
