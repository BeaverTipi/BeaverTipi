package kr.or.ddit.vo;

import java.io.Serializable;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author developer_KCY
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(of = "lstgId")
public class ListingVO implements Serializable {
	@NotBlank
		private String lstgId;
	@NotBlank
		private String mbrCd;
	@Size(max = 10)
		private String rentalPtyId;
	@Size(max = 5)
		private String lstgTypeCode;
	@Min(0) @Max(9999)
		private Long lstgRoom;
	@DecimalMin("0.0") //@Digits(integer = 10, fraction = 9)
		private Long lstgCmar;
	@DecimalMin("0.0") //@Digits(integer = 10, fraction = 9)
		private Long lstgXuar;
	@Size(max = 20)
		private String lstgTrdTypeCode;
	@PositiveOrZero //@Digits(integer = 12, fraction = 0)
		private Long lstgDepositAmount;
	@PositiveOrZero //@Digits(integer = 12, fraction = 0)
		private Long lstgMontMngFee;
	@Size(max = 20)
		private String lstgStatCode;
	@Size(max = 4000)
		private String lstgDtlDesc;
	@Size(max = 8)
		private String lstgImgId;
	@Min(0) //@Digits(integer = 10, fraction = 0)
		private Long lstgVwCnt;
	@Pattern(regexp = "[YN]")
		private String lstgPblsYn;
	@Size(max = 200)
		private String lstgNm;
	@Size(max = 5)
		private String lstgSellProd;
	@PositiveOrZero //@Digits(integer = 12, fraction = 0)
		private Long lstgPrice;
	@Size(max = 5)
		private String lstgTypeGroupCd;
	@Size(max = 5)
		private String lstgTrdTypeGroupCd;
	
	/** 매물 한 건에 대한 임대인의 정보; 있을수도 없을수도 있음. **/
	private transient TenancyVO tenancyInfo;
	private transient BrokerVO brokerInfo;
	
}
