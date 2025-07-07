package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(of={"scIds"})
public class UserScopeVO implements Serializable {
	private String scId;
	private String scNm;
}
