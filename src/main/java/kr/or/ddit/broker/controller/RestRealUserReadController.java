/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7.  ?.     		김찬영            최초 생성
 * 2025. 7. 11.     		김찬영            패키지 고침.
 *
 * </pre>
 */
package kr.or.ddit.broker.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.main.member.service.MemberService;
import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author developer_KCY
 */
@Slf4j
@RestController
@RequestMapping("/rest/broker/myoffice/member")
public class RestRealUserReadController {

	@Autowired
	private BrokerAuthUnpackingService authUnpack;
	@Autowired
	private MemberService memService;
	
	@GetMapping("/read")
	public MemberVO realUser(
//		@AuthenticationPrincipal RealUserWrapper<MemberVO> principal
		Principal principal
	) {
		
//		MemberVO broker  = principal.getRealUser();
		String username = principal.getName();
//		BrokerVO broker = authUnpack.getBroker(username);
		MemberVO member = memService.readMember(username);
		log.info("회원정보 toString(): {} ", /*broker*/ member);
		return /*broker*/ member;
	}	
}
