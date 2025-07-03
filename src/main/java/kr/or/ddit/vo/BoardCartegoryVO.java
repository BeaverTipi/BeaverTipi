package kr.or.ddit.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="brdCode")
public class BoardCartegoryVO {
	private String brdCode;
	private String ctgry;
	private String brdCmntYn;
}
