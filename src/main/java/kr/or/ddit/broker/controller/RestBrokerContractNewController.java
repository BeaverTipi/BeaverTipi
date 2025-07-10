/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7.  9.     		김찬영            최초 생성
 * 2025. 7. 10.     		김찬영            수정.
 *
 * </pre>
 */
package kr.or.ddit.broker.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerContractService;
import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.ListingWishlistVO;
import kr.or.ddit.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author developer_KCY
 * @since
 * @see
 *
 *
 */
@Slf4j
@RestController
@RequestMapping("/rest/broker/myoffice/cont/new")
public class RestBrokerContractNewController {
	
	@Autowired
	BrokerAuthUnpackingService authService;
	@Autowired
	BrokerContractService contService;
	
	@GetMapping("/listing")
	public List<ListingVO> lstgListForContract(Principal principal) {
		BrokerVO broker = authService.getRealUser(principal);
		log.error("{}", broker);
		List<ListingVO> lstgList = contService.readLstgListForContract(broker.getMbrCd());
		return lstgList;
	}
	
	@PostMapping("/lessee")
	public List<ListingWishlistVO> lesseeForContract(
		@RequestBody Map<String, String> requestBody
	) {
		String lstgId = requestBody.get("lstgId");
		log.info("--------------> {}", lstgId);
		List<ListingWishlistVO> wishlist = contService.readLesseeVolunteerList(lstgId);
		
		return wishlist;
	}
}
