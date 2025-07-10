package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author developer_LHB
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(of = "lstgId")
public class ListingVO implements Serializable {
	@NotBlank
    private String lstgId;

    @NotBlank
    private String mbrCd;

    @Size(max = 13)
    private String rentalPtyId;

    @NotNull
    private Integer lstgTypeCode1; // 대분류

    @NotNull
    private Integer lstgTypeCode2; // 소분류

    @NotBlank
    @Size(max = 100)
    private String lstgAdd;

    @Size(max = 100)
    private String lstgAdd2;

    @DecimalMin("0.0")
    private Double lstgExArea; // 전용면적

    @DecimalMin("0.0")
    private Double lstgGrArea; // 공급면적

    private Integer lstgRoomCnt;

    @NotBlank
    @Size(max = 10)
    private String lstgRoomNum;

    @NotBlank
    @Size(max = 200)
    private String lstgNm;

    @Size(max = 4000)
    private String lstgDst;

    @NotNull
    private Integer lstgTypeSale; // 거래종류 (1=전세, 2=월세, 3=매매)

    @PositiveOrZero
    private Long lstgLease;

    @PositiveOrZero
    private Long lstgLeaseM;

    @PositiveOrZero
    private Long lstgLeaseAmt;

    private Integer lstgFloor;

    @Pattern(regexp = "[YN]")
    private String lstgParkYn;

    @DecimalMin("0.0")
    private Double lstgLat;

    @DecimalMin("0.0")
    private Double lstgLng;

    @Pattern(regexp = "[YN]")
    private String lstgDel = "N";

    private LocalDateTime lstgRegDate;

    private Integer lstgProdStat = 1;
    
    @NotNull
    @Size(max = 10)
    private String lstgPostal;

    // 시설 옵션
    private List<FacilityOptionVO> facOptions;

    // 연관 정보
    private transient TenancyVO tenancyInfo;
    private transient BrokerVO brokerInfo;
	
}
