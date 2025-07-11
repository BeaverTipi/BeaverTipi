/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7.  ?.     		김찬영            최초 생성
 *
 * </pre>
 */
package kr.or.ddit.broker.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.BrokerVO;

/**
 * 	@author developer_KCY
 */
@Mapper
public interface BrokerAuthUnpackingMapper {
	public String selectMbrCdByUsername(String username);
	public BrokerVO selectBrokerByUsername(String mbrId);
}
