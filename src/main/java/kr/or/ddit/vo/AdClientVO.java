package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="adId")
public class AdClientVO implements Serializable {
	private String adId;
	private String adNm;
	private String adPoto;
	private String adTel;
	private String adOwn;
	private String adCont;
}
