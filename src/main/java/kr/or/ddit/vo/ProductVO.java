package kr.or.ddit.vo;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"mbrCd", "lstgId"})
public class ProductVO implements Serializable{

	private String lstgTrdTypeGroupCd;
	private String lstgTypeGroupCd;
	private String lstgId;
	private String mbrCd;
	private String rentalPtyId;
	private String lstgTypeCode;
	private Integer lstgRoom;
	private Integer lstgCmar;
	private Integer lstgXuar;
	private String lstgTrdTypeCode;
	private Integer lstgDepositAmount;
	private Integer lstgMontMngFee;
	private String lstgStatCode;
	private String lstgDtlDesc;
	private String lstgImgId;
	private Integer lstgVwCnt;
	private String lstgPblsYn;
	private String lstgNm;
	private String lstgSellProd;
	private Integer lstgPrice;
    
}
