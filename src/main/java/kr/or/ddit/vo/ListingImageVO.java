package kr.or.ddit.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of= {"lstgImgId"})
public class ListingImageVO implements Serializable{
	private String lstgImgId;
	private String lstgId;
	private String lstgImgUrl;
}
