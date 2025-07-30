
package kr.or.ddit.building.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.BuildingSearchFormVO;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.TenancyAccountVO;


@Mapper
public interface BuildingManagedMapper {
	 // 건물 등록
    int insertBuilding(BuildingVO building);

    // 목록조회
    List<BuildingVO> selectBuildingListByRentalPtyId(String rentalPtyId);

    // 단건만
    BuildingVO selectBuildingById(String bldgId);

    // 흠... 이게 맞나?
    int updateBuilding(BuildingVO building);

    // 건물 세대 삭제
    int deleteBuilding(@Param("bldgId") String bldgId, @Param("rentalPtyId") String rentalPtyId);
    
    //계좌연동할거얌 ㅋ
    
    List<TenancyAccountVO> selectAccountsByRentalPtyId(String rentalPtyId);
    
    //매물에서 가져와볼거얌....
    public List<ListingVO> selectListingsByRentalPtyId(String rentalPtyId);
    
    public ListingVO selectListingById(String lstgId);
    
    List<BuildingVO> searchBuildingList(BuildingSearchFormVO searchForm);


    List<TenancyAccountVO> searchAccountsByRentalPtyId(@Param("rentalPtyId") String rentalPtyId);
    
}

