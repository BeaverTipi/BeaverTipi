package kr.or.ddit.broker.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.BrokerVO;

@Mapper
public interface BrokerAuthUnpackingMapper {
	public String selectMbrCdByUsername(String username);
	public BrokerVO selectBrokerByUsername(String mbrId);
}
