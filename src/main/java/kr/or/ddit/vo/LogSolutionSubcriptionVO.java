package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(of= {"subsLog"})
public class LogSolutionSubcriptionVO implements Serializable{
	private String lsbstStatusGroupCd;
	private String subsLog;
	private String subsId;
	private String subsStatus;
	private String subsUpdatedDate;
}
