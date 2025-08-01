package kr.or.ddit.building.managed.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.building.mapper.BuildingManagedMapper;
import kr.or.ddit.building.mapper.UnitManagedMapper;
import kr.or.ddit.util.file.service.FileService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.renderer.DefaultPaginationRenderer;
import kr.or.ddit.util.validate.exception.BuildingException;
import kr.or.ddit.util.validate.exception.BuildingUnitException;
import kr.or.ddit.vo.BuildingSearchFormVO;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.FileVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.TenancyAccountVO;
import kr.or.ddit.vo.UnitVO;

@Service
public class BuildingManagedServiceImpl implements BuildingManagedService {

	@Autowired
	private BuildingManagedMapper buildingManagedMapper;
	
	@Autowired
	private CommonCodeService codeService;
	 @Autowired
	    private UnitManagedMapper unitMapper;
	 
	@Autowired
	private FileService fileService; // 파일업로드 서비스 추가

	@Override
	@Transactional
	public void insertBuilding(BuildingVO buildingVO, MultipartFile bldgImgFile) {

		// 2. 이미지 업로드 (첨부한 경우에만)
		if (bldgImgFile != null && !bldgImgFile.isEmpty()) {
			String dir = "building/" + buildingVO.getBldgId();
			String changedName = UUID.randomUUID() + "_" + bldgImgFile.getOriginalFilename();

			// FileService 이용 S3에 업로드 & DB 저장
			FileVO fileVO = fileService.uploadAndSave(bldgImgFile, dir, // building/{bldgId}
					"BUILDING", // 출처
					buildingVO.getBldgId(), // sourceId = bldgId
					"IMG" // 문서유형
			);

			// 건물 테이블에 이미지 경로 업데이트
			buildingVO.setBldgImgPath(fileVO.getFilePathUrl());
		}
		String buildingId = buildingManagedMapper.selectNextBuildingId();

		buildingVO.setBldgId(buildingId);
		buildingVO.setDelYn("N");

		if (buildingManagedMapper.insertBuilding(buildingVO) < 1) {
			throw new BuildingException(String.format("[%s] 건물 정보를 등록중 오류가 발생했습니다.", buildingVO.getBldgNm()));
		}
		List<UnitVO> unitList = buildingVO.getUnitList();
		if (unitList == null || unitList.size() < 1) {
			throw new BuildingUnitException("세대 정보가 없습니다.");
		}
		for (UnitVO unit : unitList) {
			unit.setRentalPtyId(buildingVO.getRentalPtyId());
			unit.setBldgId(buildingId);
			if(unitMapper.insertUnit(unit)<1) {
				throw new BuildingUnitException(String.format("[%s] 세대 정보를 등록중 오류가 발생했습니다.", unit.getUnitRoom()));
			}
		}
		
	}

	@Override
	public List<BuildingVO> selectBuildingListByRentalPtyId(String rentalPtyId) {
		return buildingManagedMapper.selectBuildingListByRentalPtyId(rentalPtyId);
	}

	@Override
	public BuildingVO selectBuildingById(String bldgId) {
		return buildingManagedMapper.selectBuildingById(bldgId);
	}

	@Override
	public int updateBuilding(BuildingVO buildingVO) {
		return buildingManagedMapper.updateBuilding(buildingVO);
	}

	@Override
	public int deleteBuilding(String bldgId, String rentalPtyId) {
		return buildingManagedMapper.deleteBuilding(bldgId, rentalPtyId);
	}

	@Override
	public List<TenancyAccountVO> selectAccountsByRentalPtyId(String rentalPtyId) {
		return buildingManagedMapper.selectAccountsByRentalPtyId(rentalPtyId);
	}

	// 어려웡...
	@Override
	public List<ListingVO> selectListingsByRentalPtyId(String rentalPtyId) {
		return buildingManagedMapper.selectListingsByRentalPtyId(rentalPtyId);
	}

	@Override
	public ListingVO selectListingById(String lstgId) {
		return buildingManagedMapper.selectListingById(lstgId);
	}

	@Override
	public Map<String, Object> readPagingAndBuilding(BuildingSearchFormVO form, int currentPage) {
	    Map<String, Object> result = new HashMap<>();

	    PaginationInfo<BuildingSearchFormVO> pagingVO = new PaginationInfo<>();
	    pagingVO.setCurrentPageNo(currentPage);
	    pagingVO.setDetailSearch(form);

	    int total = buildingManagedMapper.selectBuildingCount(pagingVO);
	    pagingVO.setTotalRecordCount(total);

	    List<BuildingVO> buildingList = List.of();
	    if (total > 0) {
	        buildingList = buildingManagedMapper.searchBuildingList(pagingVO);

	        // ✅ 각 건물에 계좌 리스트 바인딩
	        for (BuildingVO building : buildingList) {
	            List<TenancyAccountVO> accList = buildingManagedMapper.selectAccountsByRentalPtyId(building.getRentalPtyId());
	            building.setAccList(accList);
	        }
	    }


	    result.put("pagingVO", pagingVO);
	    result.put("buildingList", buildingList);
	    result.put("pagingHTML", new DefaultPaginationRenderer().renderPagination(pagingVO, "fn_paging"));

	    return result;
	}


	@Override
	public List<TenancyAccountVO> searchAccountsByRentalPtyId(String rentalPtyId) {
		return buildingManagedMapper.selectAccountsByRentalPtyId(rentalPtyId);
	}

	// 이미지 bldgid
	@Override
	public void updateBuildingImagePath(String bldgId, String imgUrl) {
		buildingManagedMapper.updateBuildingImagePath(bldgId, imgUrl);
	}

	@Override
	public List<BuildingVO> searchBuildingList(String rentalPtyId, BuildingSearchFormVO searchForm) {
		// TODO Auto-generated method stub
		return null;
	}

}
