/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. ?.     			김찬영            최초 생성
 * 2025. 7. 11.     		김찬영            패키지 고침.
 *
 * </pre>
 */
package kr.or.ddit.broker.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.broker.mapper.BrokerMapper;
import kr.or.ddit.broker.service.BrokerListingService;
import kr.or.ddit.building.mapper.RentalOwnerProductMapper;
import kr.or.ddit.util.file.service.FileService;
import kr.or.ddit.util.validate.exception.ListingException;
import kr.or.ddit.util.validate.exception.ListingOptionException;
import kr.or.ddit.vo.FacilityOptionVO;
import kr.or.ddit.vo.ListingOptionVO;
import kr.or.ddit.vo.ListingVO;
import lombok.RequiredArgsConstructor;

/**
 * @author developer_KCY
 */
@Service
@RequiredArgsConstructor
public class BrokerListingServiceImpl implements BrokerListingService {

	private final BrokerMapper mapper;
	private final RentalOwnerProductMapper productMapper;
	private final FileService fileService;

	@Override
	public List<ListingVO> readLstgList(String mbrCd) {
		List<ListingVO> lstgList = mapper.selectLstgList(mbrCd);
		return lstgList;
	}

	public ListingVO readLstgDetails(ListingVO listing) {
		ListingVO lstg = mapper.selectLstgDetails(listing);
		return lstg;
	}

	@Override
	public List<FacilityOptionVO> readFacilityOptionList() {
		// TODO Auto-generated method stub
		return productMapper.selectAllFacilityOptions();
	}

	@Override
	@Transactional
	public void createListing(ListingVO listing, List<MultipartFile> imageFiles, List<ListingOptionVO> optionList) {
		// TODO Auto-generated method stub productMapper.insertProduct(listing);
		String lstgId = productMapper.selectNextLstgId();
		listing.setLstgId(lstgId);
		if (productMapper.insertProduct(listing) < 1) {
			throw new ListingException();
		}
		if (optionList != null && !optionList.isEmpty()) {
			for (ListingOptionVO option : optionList) {
				option.setLstgId(lstgId);
			}
			if (productMapper.insertOptionList(optionList) < 1) {
				throw new ListingOptionException();
			}
			;
		}

		if (imageFiles != null && imageFiles.size() > 0) {
			fileService.uploadMultipleFiles(imageFiles, "public/broker/listing", "LISTING", lstgId, "listingIMG");
		}
	}

	@Override
	@Transactional
	public void modifyListing(ListingVO listing, List<MultipartFile> imageFiles, List<ListingOptionVO> optionList) {
		// 매물 정보 수정
		if (productMapper.updateProduct(listing) < 1) {
			throw new ListingException("매물 수정 처리중 오류 발생");
		}

		// 기존 옵션 삭제
		if (productMapper.deleteOptionByLstgId(listing.getLstgId()) < 1) {
			throw new ListingOptionException("옵션 삭제 처리중 오류 발생");
		}
		;

		// 새로운 옵션 등록
		if (optionList != null && !optionList.isEmpty()) {
			if (productMapper.insertOptionList(optionList) < 1) {
				throw new ListingOptionException();
			}
			;
		}
		// 이미지 처리 (기존 이미지 삭제는 선택적으로)
		if (imageFiles != null && !imageFiles.isEmpty()) {
			fileService.updateMultipleFiles(imageFiles, "public/broker/listing", "LISTING", listing.getLstgId(),
					"listingIMG");
		}
	}

	@Override
	public void removeListing(ListingVO listing) {
		if(productMapper.updateDelYnListing(listing)<1) {
			throw new ListingException("매물 삭제 중 오류가 발생했습니다.");
		}
		
	}

}
