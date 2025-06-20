package kr.or.ddit.vo;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "memId")
public class MemberVO implements Serializable {
	private String memId;
	
	private String memPassword;
	
	@Email
	private String memEmail;
	
	private boolean memDelete;
	private String memRole;
	public boolean isMemDelete() {
		// TODO Auto-generated method stub
		return memDelete;
	}
}
