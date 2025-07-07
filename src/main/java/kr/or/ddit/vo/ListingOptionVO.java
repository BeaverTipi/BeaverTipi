package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of={"lstgId","facOptId"})
public class ListingOptionVO implements Serializable{
	private String lstgId;
	private String facOptId;
}

