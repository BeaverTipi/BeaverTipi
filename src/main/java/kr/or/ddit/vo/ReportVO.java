package kr.or.ddit.vo;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(of= {""})
public class ReportVO implements Serializable {
	private String rptId;
	private String brdNo;
	private String rptCode;
	private String rptTargetId;
	private String rptStatusCode;
	private String rptDelYn;
}
