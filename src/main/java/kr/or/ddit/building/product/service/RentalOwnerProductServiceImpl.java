package kr.or.ddit.building.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.building.mapper.RentalOwnerProductMapper;
import kr.or.ddit.util.file.service.FileService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.renderer.DefaultPaginationRenderer;
import kr.or.ddit.util.validate.exception.ListingException;
import kr.or.ddit.util.validate.exception.ListingOptionException;
import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.FacilityOptionVO;
import kr.or.ddit.vo.ListingOptionVO;
import kr.or.ddit.vo.ListingSearchFormVO;
import kr.or.ddit.vo.ListingVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RentalOwnerProductServiceImpl implements RentalOwnerProductService {

	@Autowired
	private RentalOwnerProductMapper productMapper;
	@Autowired
	private CommonCodeService codeService;
	@Autowired
	private FileService fileService;

	@Override
	@Transactional
	public void insertProduct(ListingVO listing, List<String> brokerIds, List<MultipartFile> imageFiles) {
		// TODO Auto-generated method stub productMapper.insertProduct(listing);
		for (String broker : brokerIds) {
			String lstgId = productMapper.selectNextLstgId();
			listing.setLstgId(lstgId);
			listing.setMbrCd(broker);
			if (productMapper.insertProduct(listing) < 1) {
				throw new ListingException(String.format("매물 등록중 %s 부터 오류 발생.", broker));
			}
			List<FacilityOptionVO> optionList = listing.getFacOptions();
			List<ListingOptionVO> listingOptionList = optionList.stream().filter(opt -> opt.getFacOptId() != null)
					.map(opt -> {
						ListingOptionVO listingOption = new ListingOptionVO();
						listingOption.setLstgId(lstgId);
						listingOption.setFacOptId(opt.getFacOptId());
						return listingOption;
					}).collect(Collectors.toList());

			if (productMapper.insertOptionList(listingOptionList) < 1) {
				throw new ListingOptionException();
			}
			if (imageFiles != null && imageFiles.size() > 0) {
				fileService.uploadMultipleFiles(imageFiles, "public/broker/listing", "LISTING", lstgId, "listingIMG");
			}
		}
	}

	@Override
	public ListingVO selectProductById(String lstgId) {
		return productMapper.selectProductById(lstgId);
	}

	@Override
	public Integer updateProduct(ListingVO listing) {
		return productMapper.updateProduct(listing);
	}

	@Override
	public Integer deleteProduct(String lstgId) {
		return productMapper.deleteProduct(lstgId);
	}

	@Override
	public List<FacilityOptionVO> selectAllFacilityOptions() {
		return productMapper.selectAllFacilityOptions();
	}

	@Override
	public List<CommonCodeVO> commonCodeLstg1List() {
		return productMapper.commonCodeLstg1List();
	}

	@Override
	public List<CommonCodeVO> commonCodeLstg2List() {
		return productMapper.commonCodeLstg2List();
	}

	@Override
	public Map<String, Object> readPagingAndListing(ListingSearchFormVO form, int currentPage) {
		Map<String, Object> result = new HashMap<>();

		PaginationInfo<ListingSearchFormVO> pagingVO = new PaginationInfo();
		pagingVO.setCurrentPageNo(currentPage);
		pagingVO.setDetailSearch(form);

		int total = productMapper.selectTenancyProductCount(pagingVO);
		pagingVO.setTotalRecordCount(total);

		List<ListingVO> listingList = List.of();
		if (total > 0) {
			listingList = productMapper.selectTenancyProductList(pagingVO);
		}

		List<CommonCodeVO> statusCodeList = codeService.readCommonCodeList("PRDST");
		List<CommonCodeVO> typeSaleCodeList = codeService.readCommonCodeList("TRDST");


		result.put("pagingVO", pagingVO);
		result.put("dataList", listingList);
		result.put("statusCodeList", statusCodeList);
		result.put("typeSaleCodeList", typeSaleCodeList);
		result.put("pagingHTML", new DefaultPaginationRenderer().renderPagination(pagingVO, "fn_paging"));

		return result;
	}

	@Override
	public List<ListingVO> readRoomsList(ListingVO listing) {
		// TODO Auto-generated method stub
		return productMapper.selectRoomsList(listing);
	}

	@Override
	public List<BrokerVO> findNearbyBrokers(double lat, double lng, double radiusKm) {
		return productMapper.selectNearbyBrokers(lat, lng, radiusKm);
	}

	@Override
	public List<BuildingVO> readBuildingUnitList(String bldgId, String rentalPtyId) {
		// TODO Auto-generated method stub
		BuildingVO building = new BuildingVO();
		building.setBldgId(bldgId);
		building.setRentalPtyId(rentalPtyId);
		return productMapper.selectBuildingUnitList(building);
	}

	@Override
	public List<BuildingVO> readBuildingList(String rentalPtyId) {
		// TODO Auto-generated method stub
		return productMapper.selectBuildingList(rentalPtyId);
	}

}
