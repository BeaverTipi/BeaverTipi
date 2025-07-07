package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"codeGroup","codeValue"})
public class CommonCodeVO implements Serializable{
	private String codeGroup;
	private String codeValue;
	private String codeName;
	private String description;
	private String parentCodeGroup;
	private String parentCodeValue;
}
