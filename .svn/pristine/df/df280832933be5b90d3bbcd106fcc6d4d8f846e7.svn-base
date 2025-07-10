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
package kr.or.ddit.broker.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import kr.or.ddit.broker.mapper.BrokerMapper;
import kr.or.ddit.broker.service.BrokerCommonCodeService;
import kr.or.ddit.vo.CommonCodeVO;
import lombok.RequiredArgsConstructor;
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
@Service
@RequiredArgsConstructor
public class BrokerCommonCodeServiceImpl implements BrokerCommonCodeService {
	
	private final BrokerMapper mapper;
	
	@Override
	public Map<String, List<CommonCodeVO>> sortCommonCodes(Map<String, String> codeGroupParams) {
		Map<String, List<CommonCodeVO>> commonCodeMap = new HashMap<>();
		 for (Map.Entry<String, String> entry : codeGroupParams.entrySet()) {
	        String alias = entry.getKey();        // 예: "bankList"
	        String codeGroup = entry.getValue();  // 예: "BANK"

	        List<CommonCodeVO> codeList = mapper.selectCommonCodeByGroup(codeGroup);

	        commonCodeMap.put(alias, codeList);
	    }
		log.info("----------------->> {}", commonCodeMap.toString());
		return commonCodeMap;
	}
	
	@Override
	public List<CommonCodeVO> readBankList() {
		return mapper.selectBankList();
	}

	@Override
	public List<CommonCodeVO> readLesserTypeList() {
		return mapper.selectLesserTypeList();
	}



}
