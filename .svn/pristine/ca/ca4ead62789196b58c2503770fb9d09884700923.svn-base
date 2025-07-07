package kr.or.ddit.vo;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of="brdCode")
public class BoardCartegoryVO implements Serializable{
	@NotBlank
	private String brdCode;
	@NotBlank
	private String brdCtgry;
	private String brdCmntYn;
	
	public String getBrdCode() {
		return brdCode != null ? brdCode: "-";
	}
	
	public String getBrdCtgry() {
		return brdCtgry != null ? brdCtgry: "-";
	}
	
	public String getBrdCmntYn() {
		return brdCmntYn != null ? brdCmntYn: "-";
	}
}
