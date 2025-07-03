package kr.or.ddit.broker.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.ListingPackVO;

/**
 * @author developer_KCY
 */
@Mapper
public interface BrokerListingMapper {

	
	/**
	 * BROKER 한 명의 MBR_CD 값으로, 담당하는 매물 정보를 불러오는 메소드
	 * @param String mbrCd
	 * @return List<ListingVO> lstgList;
	 */
	public List<ListingPackVO> selectLstgListByMbrCd(String mbrCd);
}
