package kr.or.ddit.building.product.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.FacilityOptionVO;
import kr.or.ddit.vo.ListingSearchFormVO;
import kr.or.ddit.vo.ListingVO;

public interface RentalOwnerProductService {

	// 등록
	public void insertProduct(ListingVO listing, List<String> brokerIds, List<MultipartFile> imageFiles );


    // 상세 조회
    public ListingVO selectProductById(String lstgId);

    // 수정
    public Integer updateProduct(ListingVO listing);

    // 삭제
    public Integer deleteProduct(String lstgId);

    // 시설 옵션 전체 조회
    public List<FacilityOptionVO> selectAllFacilityOptions();

    // 매물등록 > 매물유형 목록 가져오기
    public List<CommonCodeVO> commonCodeLstg1List();
	public List<CommonCodeVO> commonCodeLstg2List();

	// 목록 조회
	public Map<String, Object> readPagingAndListing(ListingSearchFormVO searchForm, int currentPage);


	public List<ListingVO> readRoomsList(ListingVO listing);


	public List<BrokerVO> findNearbyBrokers(double lat, double lng, double radiusKm);


	public List<BuildingVO> readBuildingUnitList(String bldgId ,String rentalPtyId);
	public List<BuildingVO> readBuildingList(String rentalPtyId);
}
