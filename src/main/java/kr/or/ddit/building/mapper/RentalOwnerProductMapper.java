package kr.or.ddit.building.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.FacilityOptionVO;
import kr.or.ddit.vo.ListingOptionVO;
import kr.or.ddit.vo.ListingSearchFormVO;
import kr.or.ddit.vo.ListingVO;

@Mapper
public interface RentalOwnerProductMapper {

    // 매물 등록
	public Integer insertProduct(ListingVO listing);


    // 매물 상세 조회
	public ListingVO selectProductById(String lstgId);

    // 매물 수정
	public Integer updateProduct(ListingVO listing);

    // 매물 삭제
	public Integer deleteProduct(String lstgId);
    
    // 시설 옵션 전체 조회
	public List<FacilityOptionVO> selectAllFacilityOptions();

	public List<CommonCodeVO> commonCodeLstg1List();
	public List<CommonCodeVO> commonCodeLstg2List();
	public String selectNextLstgId();
	public Integer insertOptionList(@Param("list") List<ListingOptionVO> optionList);

	public Integer deleteOptionByLstgId(String lstgId);

	public Integer updateDelYnListing(ListingVO listing);
	// 매물 목록 조회
	public Integer selectProductCount(@Param("pagingVO")PaginationInfo<ListingSearchFormVO> pagingVO);

	public List<ListingVO> selectProductList(@Param("pagingVO") PaginationInfo<ListingSearchFormVO>  pagingVO);
	public List<ListingVO> selectTenancyProductList(@Param("pagingVO") PaginationInfo<ListingSearchFormVO>  pagingVO);
	public Integer selectTenancyProductCount(@Param("pagingVO") PaginationInfo<ListingSearchFormVO>  pagingVO);


	public List<ListingVO> selectRoomsList(ListingVO listing);


	public List<BrokerVO> selectNearbyBrokers(@Param("lat")double lat, @Param("lng")double lng, @Param("radiusKm")double radiusKm);


	public List<BuildingVO> selectBuildingList(String rentalPtyId);


	public List<BuildingVO> selectBuildingUnitList(BuildingVO building);
}
