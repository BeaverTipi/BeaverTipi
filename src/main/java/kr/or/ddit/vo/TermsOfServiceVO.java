package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(of= {"tofsCd"})
public class TermsOfServiceVO implements Serializable {
	private String tofsCd;
	private String tofsTitl;
	private String tofsComt;
	private String tofsDtm;
	private String tofsUdtm;
}
