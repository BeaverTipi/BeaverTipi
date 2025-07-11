package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper=true, of={"lstgId","facOptId"})
public class ListingOptionVO extends FacilityOptionVO implements Serializable{
	private String lstgId;
	private String facOptId;
}

