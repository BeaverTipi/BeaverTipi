/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 10.     		김찬영            최초 생성
 *
 * </pre>
 */
package kr.or.ddit.vo;
import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author developer_KCY
 * @since
 * @see
 *
 *
 */
@SuppressWarnings("serial")
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true, of = {"lstgId", "mbrCd"})
public class ListingWishlistVO extends MemberVO implements Serializable {
	@NotBlank @Size(max = 11)
	private String mbrCd;
	@NotBlank @Size(max = 11)
    private String lstgId;
	private String wishDt;
}
